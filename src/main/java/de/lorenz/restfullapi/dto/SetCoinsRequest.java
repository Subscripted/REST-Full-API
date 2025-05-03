package de.lorenz.restfullapi.dto;

import lombok.Getter;
import lombok.Setter;

public class SetCoinsRequest {

    @Getter
    @Setter
    private int coins;

    public SetCoinsRequest() {}

    public SetCoinsRequest(int coins) {
        this.coins = coins;
    }
}
