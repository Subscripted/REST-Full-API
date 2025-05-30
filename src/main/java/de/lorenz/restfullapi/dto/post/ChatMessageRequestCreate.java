package de.lorenz.restfullapi.dto.post;

public record ChatMessageRequestCreate(
        Long senderId,
        String message
) {}
