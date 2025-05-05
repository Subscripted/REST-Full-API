package de.lorenz.restfullapi.controller.endpoints.forum;

import de.lorenz.restfullapi.dto.Chat;
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
                        chat.getMessage(),
                        chat.getSender().getUserId(),
                        chat.getSender().getUsername(),
                        chat.getSender().getRank(),
                        chat.getTime()
                ))
                .toList();

        return ResponseEntity.ok(chats);
    }

}

