package de.lorenz.restfullapi.dto;

import java.time.LocalDateTime;
import java.util.Date;

public record AntragOverview(
        Long antragsId,
        Long userId,
        String username,
        Long teamlerId,
        String title,
        LocalDateTime insertDate,
        LocalDateTime lastUpdated
) {
}
