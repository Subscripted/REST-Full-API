package de.lorenz.restfullapi.controller.endpoints.forum;

import de.lorenz.restfullapi.dto.Chat;
import de.lorenz.restfullapi.dto.CreateChatMessageRequest;
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

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/entbannungs-antrag")
public class ChatController {

    private final ForumChatMessageRepository forumChatMessageRepository;
    private final TokenService tokenService;
    private final AntragRepository antragRepository;
    private final ForumUserRepository forumUserRepository;


    private boolean isAuthorized(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            return tokenService.isTokenValid(token);
        }
        return false;
    }


    @PostMapping("/chat/{antragsId}")
    public ResponseEntity<?> createChatMessage(@PathVariable Long antragsId, @RequestBody CreateChatMessageRequest request, @RequestHeader(value = "Authorization", required = false) String authHeader) {

        if (!isAuthorized(authHeader)) {
            return ResponseEntity.status(401).body("{\"error\": \"Unauthorized\"}");
        }

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
        chatMessage.setTime(LocalDateTime.now());
        chatMessage.setReported(false);

        ChatMessage saved = forumChatMessageRepository.save(chatMessage);

        Chat dto = new Chat(
                saved.getChatId(),
                saved.getMessageId(),
                saved.getMessage(),
                saved.getSender().getUserId(),
                saved.getSender().getUsername(),
                saved.getSender().getRank(),
                saved.getTime()
        );
        return ResponseEntity.ok(dto);
    }


    @GetMapping("/{antragsId}/chat")
    public ResponseEntity<?> getChatMessages(
            @PathVariable Long antragsId,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        if (!isAuthorized(authHeader)) {
            return ResponseEntity.status(401).body("{\"error\": \"Unauthorized\"}");
        }

        List<Chat> chats = forumChatMessageRepository.findByAntrag_AntragsIdOrderByTimeAsc(antragsId)
                .stream()
                .map(chat -> new Chat(
                        chat.getChatId(),
                        chat.getMessageId(),
                        chat.getMessage(),
                        chat.getSender().getUserId(),
                        chat.getSender().getUsername(),
                        chat.getSender().getRank(),
                        chat.getTime()
                ))
                .toList();

        return ResponseEntity.ok(chats);
    }

    @GetMapping("/report/{chatid}/{messageid}")
    public ResponseEntity<?> report(@PathVariable Long chatid, @PathVariable Long messageid, @RequestHeader(value = "Authorization", required = false) String authHeader) {

        if (!isAuthorized(authHeader)) {
            return ResponseEntity.status(401).body("{\"error\": \"Unauthorized\"}");
        }

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
}

