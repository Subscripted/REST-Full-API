package de.lorenz.restfullapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class UserDataResponse {

    @Getter
    private String uuid;
    @Getter
    private String name;
    @Getter
    private String ip;
    @Getter
    private boolean verified;
    @Getter
    private String discord_userid;
    @Getter
    private String verified_date;
    @Getter
    private long spielzeit;
    @Getter
    private int coins;

}
