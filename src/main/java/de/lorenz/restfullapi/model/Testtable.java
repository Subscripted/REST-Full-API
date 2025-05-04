package de.lorenz.restfullapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@AllArgsConstructor
public class Testtable {

    @Id
    @Column(name = "uuid", nullable = false, length = 36)
    @Getter
    @Setter
    private String uuid;

    @Column(nullable = false)
    @Getter
    @Setter
    private int coins;

    public Testtable() {
    }
}
