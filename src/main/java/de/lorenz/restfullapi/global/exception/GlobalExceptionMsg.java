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
    USER_CREATED("User creation successful.");


    public final String ExceptionMsg;


}
