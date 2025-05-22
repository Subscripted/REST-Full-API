package de.lorenz.restfullapi.global.exception;

import lombok.RequiredArgsConstructor;
import lombok.experimental.StandardException;

@StandardException
public class MalformedJsonException extends RuntimeException {

    public MalformedJsonException(String s) {
        super(s);
    }
}

