package de.lorenz.restfullapi.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

public class TokenRequest {

    @Getter
    @Setter
    @Id
    @Column(nullable = false)
    private String email;
    @Getter
    @Setter
    @Column(name = "client_id", nullable = false)
    private String clientId;
    @Getter
    @Setter
    @Column(name = "client_secret", nullable = false)
    private String clientSecret;
}
