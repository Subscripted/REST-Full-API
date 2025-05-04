package de.lorenz.restfullapi.dto;

import java.util.Collection;

public class ResponseWrapper<T> {

    private T data;
    private int count;

    public ResponseWrapper(T data) {
        this.data = data;
        if (data instanceof Collection<?>) {
            this.count = ((Collection<?>) data).size();
        } else {
            this.count = 1;
        }
    }

    public T getData() {
        return data;
    }

    public int getCount() {
        return count;
    }
}
