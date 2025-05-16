package de.lorenz.restfullapi.global.exception;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@RequiredArgsConstructor
public enum GlobalExceptionMsg {

    USER_NO_CREATION_MISSING_CREDENTIALS("User creation failed: missing required user information."),
    USER_NO_CREATION_ALREADY_EXISTS("User creation failed: user already exists."),
    USER_CREATED("User creation successful."),

    PLAYER_DATA_BY_NAME("Playerdata Fetched Successfully by Name"),
    PLAYER_DATA_BY_UUID("Playerdata Fetched Successfully by UUID"),
    PLAYER_DATA_BY_IP("Playerdata Fetched Successfully by IP"),

    SPIELER_DATEN_BY_UUID_EMPTY("Playerdata Fetch failed: no Playerdata was found with given uuid %s."),
    SPIELER_DATEN_BY_NAME_EMPTY("Playerdata Fetch failed: no Playerdata was found."),
    SPIELER_DATEN_BY_IP_EMPTY("Playerdata Fetch failed: No Playerdata found with given IP %s.");


    public final String ExceptionMsg;


}
