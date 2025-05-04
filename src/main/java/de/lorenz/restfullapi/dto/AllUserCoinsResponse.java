package de.lorenz.restfullapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class AllUserCoinsResponse {

    @Getter
    private String uuid;

    @Getter
    private int coins;

}
