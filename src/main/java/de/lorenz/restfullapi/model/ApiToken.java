package de.lorenz.restfullapi.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "API_Tokens")
public class ApiToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    private String email;
    @Getter
    @Setter
    private String token;
    @Getter
    @Setter
    private LocalDateTime expiresAt;
}
