package de.lorenz.restfullapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;


public record Chat(Long chatId, Long messageId, String message, Long senderId, String username, String rank, LocalDateTime time) {}



