package de.lorenz.restfullapi.dto;

import java.time.LocalDateTime;


public record Chat(Long chatId, Long messageId, String message, Long senderId,String title, String username, String rank, LocalDateTime time) {}



