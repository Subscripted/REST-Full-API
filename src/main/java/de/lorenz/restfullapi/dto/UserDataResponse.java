package de.lorenz.restfullapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class UserDataResponse {

    @Getter
    private String uuid;
    @Getter
    private int coins;
    @Getter
    private String ip;
    @Getter
    private String name;
    @Getter
    private long playtime;

}
