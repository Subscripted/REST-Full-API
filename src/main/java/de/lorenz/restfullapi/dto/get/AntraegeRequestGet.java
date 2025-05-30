package de.lorenz.restfullapi.dto.get;

import java.time.LocalDateTime;

public record AntraegeRequestGet(
        Long antragsId,
        Long userId,
        String username,
        Long teamlerId,
        String title,
        LocalDateTime insertDate,
        LocalDateTime lastUpdated
) {
}
