package de.lorenz.restfullapi.controller.endpoints.forum;

import de.lorenz.restfullapi.dto.Chat;
import de.lorenz.restfullapi.model.ChatMessage;
import de.lorenz.restfullapi.repository.ForumChatMessageRepository;
import de.lorenz.restfullapi.service.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/entbannungs-antrag")
public class ChatController {

    private final ForumChatMessageRepository forumChatMessageRepository;
    private final TokenService tokenService;

    public ChatController(ForumChatMessageRepository forumChatMessageRepository, TokenService tokenService) {
        this.forumChatMessageRepository = forumChatMessageRepository;
        this.tokenService = tokenService;
    }

    private boolean isAuthorized(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            return tokenService.isTokenValid(token);
        }
        return false;
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
    public ResponseEntity<?> report(
            @PathVariable Long chatid,
            @PathVariable Long messageid,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

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

        if (Boolean.TRUE.equals(chatMessage.getReported())) {
            return ResponseEntity.status(400).body("{\"error\": \"Nachricht wurde bereits gemeldet\"}");
        }

        chatMessage.setReported(true);
        forumChatMessageRepository.save(chatMessage);

        return ResponseEntity.ok("{\"success\": \"Nachricht wurde gemeldet\"}");
    }
}

