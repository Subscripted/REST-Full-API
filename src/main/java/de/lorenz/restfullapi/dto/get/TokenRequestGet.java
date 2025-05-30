package de.lorenz.restfullapi.dto.get;

public record TokenRequestGet(String email, String clientId, String clientSecret) {
}
