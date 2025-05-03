package de.lorenz.restfullapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.UUID;

@Entity
public class Testtable {

    @Id
    @Column(name = "uuid", nullable = false, length = 36)
    private String uuid;

    @Column(nullable = false)
    private int coins;

    public Testtable() {
    }

    public Testtable(String uuid, int coins) {
        this.uuid = uuid;
        this.coins = coins;
    }

    public String getUuid() { return uuid; }
    public void setUuid(String uuid) { this.uuid = uuid; }

    public int getCoins() { return coins; }
    public void setCoins(int coins) { this.coins = coins; }
}
