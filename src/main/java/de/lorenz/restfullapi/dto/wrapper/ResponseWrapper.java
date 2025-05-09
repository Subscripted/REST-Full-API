package de.lorenz.restfullapi.dto.wrapper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collection;

@RequiredArgsConstructor
public class ResponseWrapper<T> {

    @Getter
    private T response;

    @Getter
    private int count;

    public ResponseWrapper(T response) {
        this.response = response;
        if (response instanceof Collection<?>) {
            this.count = ((Collection<?>) response).size();
        } else {
            this.count = 1;
        }
    }
}
