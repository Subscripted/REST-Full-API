package de.lorenz.restfullapi.dto.put;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

public record ForumUserUpdateResponse(long userId, Map<String, Object> data) {
}
