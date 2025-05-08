package de.lorenz.restfullapi.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TokenRequest {

    private String email;
    private String clientId;
    private String clientSecret;
}
