package de.lorenz.restfullapi.dto.put;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = false)

public record ForumUserRequestUpdate(Long user_id, String username, String email, String password, String firstname, String rank) {
}
