package de.lorenz.restfullapi.dto;

public record AntragOverview(
        Long antragsId,
        Long userId,
        String username,
        Long teamlerId,
        String title
) {
}
