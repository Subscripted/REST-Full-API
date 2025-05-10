package de.lorenz.restfullapi.controller.endpoints.forum;

import de.lorenz.restfullapi.dto.AntragOverview;
import de.lorenz.restfullapi.dto.Chat;
import de.lorenz.restfullapi.dto.CreateAntragRequest;
import de.lorenz.restfullapi.dto.CreateChatMessageRequest;
import de.lorenz.restfullapi.dto.wrapper.ResponseWrapper;
import de.lorenz.restfullapi.model.Antrag;
import de.lorenz.restfullapi.model.ChatMessage;
import de.lorenz.restfullapi.repository.AntragRepository;
import de.lorenz.restfullapi.repository.ForumChatMessageRepository;
import de.lorenz.restfullapi.repository.ForumUserRepository;
import de.lorenz.restfullapi.service.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/entbannungs-antrag")
public class ChatController {

    private final ForumChatMessageRepository forumChatMessageRepository;
    private final TokenService tokenService;
    private final AntragRepository antragRepository;
    private final ForumUserRepository forumUserRepository;

    /**
     * Erstellt einen neuen Antrag (Chat-Thread).
     * Benötigt:
     * - Authorization Header: Bearer {token}
     * - Body (JSON):
     * {
     * "userId": Long // ID des Users, der den Antrag stellt
     * }
     * Antwort:
     * - antragsId (Long)
     * - message (String)
     */
    @PostMapping("/new/chat")
    public ResponseEntity<?> createChat(
            @RequestBody CreateAntragRequest request,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {


        var userOpt = forumUserRepository.findById(request.userId());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body("{\"error\": \"User nicht gefunden\"}");
        }

        var user = userOpt.get();
        var antrag = new Antrag();
        antrag.setUser(user);
        antrag.setTeamler(null);
        antrag.setStatus(false);
        antrag.setTitle("Entbannungsantrag - " + antrag.getUser().getUsername() + " " + antrag.getAntragsId());
        antrag.setAntragsId(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);

        var savedAntrag = antragRepository.save(antrag);
        return ResponseEntity.ok(new ResponseWrapper<>(Map.of(
                "antragsId", savedAntrag.getAntragsId(),
                "message", "Entbannungsantrag - " + antrag.getUser().getUsername() + " " + antrag.getAntragsId() + " Erstellt!"
        )));
    }

    /**
     * Erstellt eine neue Chat-Nachricht im angegebenen Antrag.
     * <p>
     * Benötigt:
     * - Authorization Header: Bearer {token}
     * - Path Variable: antragsId (Long) → ID des Antrags, zu dem die Nachricht gehört.
     * - Body (JSON):
     * {
     * "senderId": Long,   // ID des Users, der die Nachricht sendet
     * "message": String   // Inhalt der Nachricht
     * }
     * <p>
     * Antwort:
     * - Chat DTO mit ChatId, MessageId, Message, SenderId, Antrag-Title, Username, Rank und Zeit.
     */
    @PostMapping("/chat/{antragsId}")
    public ResponseEntity<?> sendMessage(@PathVariable Long antragsId, @RequestBody CreateChatMessageRequest request, @RequestHeader(value = "Authorization", required = false) String authHeader) {


        var antragOpt = antragRepository.findById(antragsId);
        if (antragOpt.isEmpty()) {
            return ResponseEntity.status(404).body("{\"error\": \"Antrag nicht gefunden\"}");
        }
        var antrag = antragOpt.get();

        var senderOpt = forumUserRepository.findById(request.senderId());
        if (senderOpt.isEmpty()) {
            return ResponseEntity.status(404).body("{\"error\": \"Sender nicht gefunden\"}");
        }
        var sender = senderOpt.get();

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setAntrag(antrag);
        chatMessage.setSender(sender);
        chatMessage.setMessage(request.message());
        chatMessage.setMessageId(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
        chatMessage.setTime(LocalDateTime.now());
        chatMessage.setReported(false);

        ChatMessage saved = forumChatMessageRepository.save(chatMessage);

        Chat dto = new Chat(saved.getChatId(), saved.getMessageId(), saved.getMessage(), saved.getSender().getUserId(), saved.getAntrag().getTitle(), saved.getSender().getUsername(), saved.getSender().getRank(), saved.getTime());
        return ResponseEntity.ok(dto);
    }


    /**
     * Holt alle Chat-Nachrichten für einen bestimmten Antrag.
     * <p>
     * Benötigt:
     * - Authorization Header: Bearer {token}
     * - Path Variable: antragsId (Long) → ID des Antrags.
     * <p>
     * Antwort:
     * - Liste von Chat DTOs mit ChatId, MessageId, Message, SenderId, Antrag-Title, Username, Rank und Zeit.
     */
    @GetMapping("/{antragsId}/chat")
    public ResponseEntity<?> getChatMessages(
            @PathVariable Long antragsId,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        List<Chat> chats = forumChatMessageRepository.findByAntrag_AntragsIdOrderByTimeAsc(antragsId)
                .stream()
                .map(chat -> new Chat(
                        chat.getChatId(),
                        chat.getMessageId(),
                        chat.getMessage(),
                        chat.getSender() != null ? chat.getSender().getUserId() : 0,
                        chat.getAntrag().getTitle(),
                        chat.getSender() != null ? chat.getSender().getUsername() : "Unbekannt",
                        chat.getSender() != null ? chat.getSender().getRank() : "Unbekannt",
                        chat.getTime()
                ))
                .toList();
        return ResponseEntity.ok(chats);
    }


    /**
     * Markiert eine Chat-Nachricht als gemeldet (reported).
     * <p>
     * Benötigt:
     * - Authorization Header: Bearer {token}
     * - Path Variable: chatid (Long) → Chat-ID der Nachricht.
     * - Path Variable: messageid (Long) → Message-ID der Nachricht (dient zur Verifikation).
     * <p>
     * Antwort:
     * - Erfolgsmeldung als JSON {"success": "Nachricht wurde gemeldet"} oder Fehler.
     */
    @GetMapping("/report/{chatid}/{messageid}")
    public ResponseEntity<?> report(@PathVariable Long chatid, @PathVariable Long messageid, @RequestHeader(value = "Authorization", required = false) String authHeader) {

        ChatMessage chatMessage = forumChatMessageRepository.findById(chatid).orElse(null);

        if (chatMessage == null) {
            return ResponseEntity.status(404).body("{\"error\": \"Chat-Nachricht nicht gefunden\"}");
        }

        if (chatMessage.getMessageId() == null || !chatMessage.getMessageId().equals(messageid)) {
            return ResponseEntity.status(400).body("{\"error\": \"Message-ID stimmt nicht überein\"}");
        }

        if (isReported(chatMessage)) {
            return ResponseEntity.status(400).body("{\"error\": \"Nachricht wurde bereits gemeldet\"}");
        }

        chatMessage.setReported(true);
        forumChatMessageRepository.save(chatMessage);

        return ResponseEntity.ok("{\"success\": \"Nachricht wurde gemeldet\"}");
    }


    private boolean isReported(ChatMessage chatMessage) {
        return chatMessage.getReported().equals(true);
    }


    /**
     * Holt alle Anträge mit ihrer antragsId, userId und teamlerId.
     * <p>
     * Benötigt:
     * - Authorization Header: Bearer {token}
     * <p>
     * Antwort:
     * - Liste von AntragOverview DTOs:
     * {
     * "antragsId": Long,
     * "userId": Long,
     * "teamlerId": Long (oder null)
     * }
     */
    @GetMapping("/antraege")
    public ResponseEntity<?> getAllAntraege(@RequestHeader(value = "Authorization", required = false) String authHeader) {

      /*  if (!isAuthorized(authHeader)) {
            return ResponseEntity.status(401).body("{\"error\": \"Unauthorized\"}");
        }

       */
        List<AntragOverview> result = antragRepository.findAll().stream()
                .map(a -> new AntragOverview(
                        a.getAntragsId(),
                        a.getUser() != null ? a.getUser().getUserId() : null,
                        a.getUser() != null && a.getUser().getUsername() != null ? a.getUser().getUsername() : "Unbekannt",
                        a.getTeamler() != null ? a.getTeamler().getUserId() : null,
                        a.getTitle() != null ? a.getTitle() : ""
                )).toList();

        return ResponseEntity.ok(result);
    }


}

