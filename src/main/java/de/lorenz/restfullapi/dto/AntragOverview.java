package de.lorenz.restfullapi.dto;

public record AntragOverview(
        Long antragsId,
        Long userId,
        Long teamlerId,
        String title
) {
}
