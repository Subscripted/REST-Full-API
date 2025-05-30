package de.lorenz.restfullapi.controller.endpoints.forum;

import de.lorenz.restfullapi.dto.*;
import de.lorenz.restfullapi.dto.get.AntraegeRequestGet;
import de.lorenz.restfullapi.dto.post.AntragRequestCreate;
import de.lorenz.restfullapi.dto.post.ChatMessageRequestCreate;
import de.lorenz.restfullapi.dto.wrapper.ResponseWrapper;
import de.lorenz.restfullapi.global.exception.GlobalExceptionMsg;
import de.lorenz.restfullapi.model.Antrag;
import de.lorenz.restfullapi.model.ChatMessage;
import de.lorenz.restfullapi.repository.AntragRepository;
import de.lorenz.restfullapi.repository.ForumChatMessageRepository;
import de.lorenz.restfullapi.repository.ForumUserRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/entbannungs-antrag")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatController {

    final ForumChatMessageRepository forumChatMessageRepository;
    final AntragRepository antragRepository;
    final ForumUserRepository forumUserRepository;

    Map<String, Object> json;
    /**
     * @Usage {
     * "userId":Long
     * }
     * @Description Erstellt einen neuen Antrag
     * @Response Antwort:
     * - antragsId (Long)
     * - message (String)
     * @see #createChat(AntragRequestCreate, String)
     */
    @PostMapping("/new/chat")
    public ResponseWrapper<?> createChat(@RequestBody AntragRequestCreate request, @RequestHeader(value = "Authorization", required = false) String authHeader) {

        json = new HashMap<>();
        var userOpt = forumUserRepository.findById(request.userId());


        json.put("message", GlobalExceptionMsg.USER_NOT_FOUND.getExceptionMsg());
        if (userOpt.isEmpty()) {
            return ResponseWrapper.error(json, GlobalExceptionMsg.USER_NOT_FOUND.getExceptionMsg());
        }

        var user = userOpt.get();
        var antrag = new Antrag();
        antrag.setUser(user);
        antrag.setTeamler(null);
        antrag.setStatus(false);
        antrag.setTitle("Entbannungsantrag - " + antrag.getUser().getUsername() + " " + antrag.getAntragsId());
        antrag.setAntragsId(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);

        var savedAntrag = antragRepository.save(antrag);

        json = new HashMap<>();
        json.put("message", "Entbannungsantrag - " + antrag.getUser().getUsername() + " " + antrag.getAntragsId() + " Erstellt!");
        json.put("antragsId", savedAntrag.getAntragsId());
        return ResponseWrapper.ok(json);
    }

    /**
     * @Usage POST /api/v1/entbannungs-antrag/chat/{antragsId}
     * Header:
     * Authorization: Bearer {token}
     * Body:
     * {
     * "senderId": Long,
     * "message": String
     * }
     * @Description Sendet eine neue Nachricht in den Antrag-Chat. Der Antrag muss existieren,
     * und der Sender muss ein bekannter Nutzer sein. Die Nachricht wird mit einer zufälligen
     * Message-ID und dem aktuellen Zeitstempel gespeichert.
     * @Response Antwort:
     * - chatId (Long)
     * - messageId (Long)
     * - message (String)
     * - senderId (Long)
     * - title (String)
     * - username (String)
     * - rank (String)
     * - time (DateTime)
     * @see #sendMessage(Long, ChatMessageRequestCreate, String)
     */
    @PostMapping("/chat/{antragsId}")
    public ResponseEntity<?> sendMessage(@PathVariable Long antragsId, @RequestBody ChatMessageRequestCreate request, @RequestHeader(value = "Authorization", required = false) String authHeader) {
        var antragOpt = antragRepository.findById(antragsId);
        if (antragOpt.isEmpty()) {
            return ResponseEntity.status(404).body("{\"error\": \"Antrag nicht gefunden\"}");
        }
        Antrag antrag = antragOpt.get();

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
     * @Usage DELETE /api/v1/entbannungs-antrag/antrag/{antragsId}
     * @Description Löscht einen Antrag inklusive aller zugehörigen Chat-Nachrichten.
     * Wenn der Antrag nicht existiert, wird eine Fehlermeldung zurückgegeben.
     * @Response Antwort:
     * - antragsId (Long)
     * - message (String)
     * @see #deleteAntrag(Long)
     */

    @DeleteMapping("/antrag/{antragsId}")
    public ResponseWrapper<Object> deleteAntrag(@PathVariable Long antragsId) {
        var antragOpt = antragRepository.findById(antragsId);

        if (antragOpt.isEmpty()) {
            return ResponseWrapper.error("Not found", "Antrag mit ID " + antragsId + " wurde nicht gefunden.");
        }

        forumChatMessageRepository.deleteByAntrag_AntragsId(antragsId);
        antragRepository.deleteById(antragsId);
        return ResponseWrapper.ok(antragsId, "Antrag erfolgreich gelöscht.");
    }

    /**
     * @Usage GET /api/v1/entbannungs-antrag/{antragsId}/chat
     * Header:
     * Authorization: Bearer {token}
     * @Description Gibt alle Chat-Nachrichten für den angegebenen Antrag zurück.
     * Die Nachrichten sind nach Zeit aufsteigend sortiert. Wenn ein Sender nicht mehr existiert,
     * wird "Unbekannt" angezeigt.
     * @Response Antwort:
     * Liste von Objekten mit:
     * - chatId (Long)
     * - messageId (Long)
     * - message (String)
     * - senderId (Long oder 0)
     * - title (String)
     * - username (String)
     * - rank (String)
     * - time (DateTime)
     * @see #getChatMessages(Long, String)
     */

    @GetMapping("/{antragsId}/chat")
    public ResponseEntity<?> getChatMessages(@PathVariable Long antragsId, @RequestHeader(value = "Authorization", required = false) String authHeader) {
        List<Chat> chats = forumChatMessageRepository.findByAntrag_AntragsIdOrderByTimeAsc(antragsId)
                .stream()
                .map(chat -> new Chat(
                        chat.getChatId(),
                        chat.getMessageId(),
                        chat.getMessage(),
                        chat.getSender() != null ? chat.getSender().getUserId() : 0,
                        chat.getAntrag().getTitle(),
                        chat.getSender() != null ? chat.getSender().getUsername() : "Unknown",
                        chat.getSender() != null ? chat.getSender().getRank() : "Unknown",
                        chat.getTime()
                ))
                .toList();
        return ResponseEntity.ok(chats);
    }


    /**
     * @Usage GET /api/v1/entbannungs-antrag/report/{chatid}/{messageid}
     * Header:
     * Authorization: Bearer {token}
     * @Description Markiert eine Chat-Nachricht als "gemeldet". Die Nachricht muss existieren,
     * die `messageId` muss zur Chat-Nachricht passen, und die Nachricht darf noch nicht gemeldet worden sein.
     * @Response Antwort:
     * - {"success": "Nachricht wurde gemeldet"}
     * oder Fehler:
     * - {"error": "..."}
     * @see #report(Long, Long, String)
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

    /**
     * @Usage GET /api/v1/entbannungs-antrag/antraege
     * Header:
     * Authorization: Bearer {token}
     * @Description Holt alle gespeicherten Entbannungsanträge mit grundlegenden Informationen
     * wie Antrag-ID, Nutzer-ID, Teamler-ID und Antragstitel.
     * @Response Antwort:
     * Liste von Objekten mit:
     * - antragsId (Long)
     * - userId (Long oder null)
     * - username (String)
     * - teamlerId (Long oder null)
     * - title (String)
     * @see #getAllAntraege(String)
     */

    @GetMapping("/antraege")
    public ResponseEntity<?> getAllAntraege(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        List<AntraegeRequestGet> result = antragRepository.findAll().stream()
                .map(a -> {
                    Long userId = a.getUser() != null ? a.getUser().getUserId() : null;
                    String username = a.getUser() != null && a.getUser().getUsername() != null ? a.getUser().getUsername() : "Unbekannt";
                    Long teamlerId = a.getTeamler() != null ? a.getTeamler().getUserId() : null;
                    String title = a.getTitle() != null ? a.getTitle() : "";

                    return new AntraegeRequestGet(
                            a.getAntragsId(),
                            userId,
                            username,
                            teamlerId,
                            title,
                            a.getInsertDate(),
                            a.getLastUpdated()
                    );
                })
                .toList();

        return ResponseEntity.ok(result);
    }

    private boolean isReported(ChatMessage chatMessage) {
        return chatMessage.getReported().equals(true);
    }

}

