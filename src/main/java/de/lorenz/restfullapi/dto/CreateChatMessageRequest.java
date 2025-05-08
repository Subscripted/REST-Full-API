package de.lorenz.restfullapi.dto;

public record CreateChatMessageRequest(
        Long senderId,
        String message
) {}
