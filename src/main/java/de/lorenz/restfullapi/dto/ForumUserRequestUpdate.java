package de.lorenz.restfullapi.dto;

import lombok.Getter;
import lombok.Setter;

public record ForumUserRequestUpdate(Long user_id, String username, String email, String password, String firstname, String rank) {
}
