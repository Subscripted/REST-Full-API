package de.lorenz.restfullapi.dto.wrapper;

import de.lorenz.restfullapi.global.exception.GlobalHttpStatusCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collection;

@Getter
public class ResponseWrapper<T> {

    private final T response;
    private final String message;
    private final String statusCode;
    private final int count;

    private ResponseWrapper(T response, String message, String statusCode) {
        this.response = response;
        this.message = message;
        this.statusCode = statusCode;
        if (response instanceof Collection<?>) {
            this.count = ((Collection<?>) response).size();
        } else {
            this.count = response != null ? 1 : 0;
        }
    }

    public static <T> ResponseWrapper<T> ok(T data) {
        return new ResponseWrapper<>(data, "OK", GlobalHttpStatusCode.OK.getCode());
    }

    public static <T> ResponseWrapper<T> ok(T data, String message) {
        return new ResponseWrapper<>(data, message, GlobalHttpStatusCode.OK.getCode());
    }

    public static <T> ResponseWrapper<T> error(String message) {
        return new ResponseWrapper<>(null, message, GlobalHttpStatusCode.INTERNAL_ERROR.getCode());
    }

    public static <T> ResponseWrapper<T> error(T data, String message) {
        return new ResponseWrapper<>(data, message, GlobalHttpStatusCode.INTERNAL_ERROR.getCode());
    }

    public static <T> ResponseWrapper<T> badRequest(String message) {
        return new ResponseWrapper<>(null, message, GlobalHttpStatusCode.BAD_REQUEST.getCode());
    }

    public static <T> ResponseWrapper<T> badRequest(T data, String message) {
        return new ResponseWrapper<>(data, message, GlobalHttpStatusCode.BAD_REQUEST.getCode());
    }

    public static <T> ResponseWrapper<T> unauthorized(String message) {
        return new ResponseWrapper<>(null, message, GlobalHttpStatusCode.UNAUTHORIZED.getCode());
    }

    public static <T> ResponseWrapper<T> unauthorized(T data, String message) {
        return new ResponseWrapper<>(data, message, GlobalHttpStatusCode.UNAUTHORIZED.getCode());
    }

    public static <T> ResponseWrapper<T> forbidden(String message) {
        return new ResponseWrapper<>(null, message, GlobalHttpStatusCode.FORBIDDEN.getCode());
    }

    public static <T> ResponseWrapper<T> forbidden(T data, String message) {
        return new ResponseWrapper<>(data, message, GlobalHttpStatusCode.FORBIDDEN.getCode());
    }

    public static <T> ResponseWrapper<T> notFound(String message) {
        return new ResponseWrapper<>(null, message, GlobalHttpStatusCode.NOT_FOUND.getCode());
    }

    public static <T> ResponseWrapper<T> notFound(T data, String message) {
        return new ResponseWrapper<>(data, message, GlobalHttpStatusCode.NOT_FOUND.getCode());
    }

}
