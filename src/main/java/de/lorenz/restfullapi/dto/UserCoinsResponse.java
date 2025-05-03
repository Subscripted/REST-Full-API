// dto/UserCoinsResponse.java
package de.lorenz.restfullapi.dto;

import java.util.UUID;

public class UserCoinsResponse {

    private String uuid;
    private int coins;

    public UserCoinsResponse(String uuid, int coins) {
        this.uuid = uuid;
        this.coins = coins;
    }

    public String getUuid() { return uuid; }
    public int getCoins() { return coins; }
}
