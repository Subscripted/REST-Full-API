package de.lorenz.restfullapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "logincreds")
@AllArgsConstructor
public class LoginCreds {

    @Getter
    @Setter
    @Id
    @Column(name = "email", nullable = false)
    private String email;

    @Getter
    @Setter
    @Column(name = "client_id", nullable = false)
    private String clientId;

    @Getter
    @Setter
    @Column(name = "client_secret", nullable = false)
    private String clientSecret;

    public LoginCreds() {
    }
}
