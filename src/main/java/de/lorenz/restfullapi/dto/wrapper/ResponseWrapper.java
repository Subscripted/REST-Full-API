package de.lorenz.restfullapi.dto.wrapper;

import de.lorenz.restfullapi.model.UserData;
import lombok.Getter;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

@Getter
public class ResponseWrapper<T> {

    private final T response;
    private final String message;
    private final String statusCode;
    private final int count;

    public ResponseWrapper(T response, String message, String statusCode) {
        this.message = message;
        this.statusCode = statusCode;
        this.response = response;
        if (response instanceof Collection<?>) {
            this.count = ((Collection<?>) response).size();
        } else {
            this.count = response != null ? 1 : 0;
        }
    }



    public static <T> ResponseWrapper<T> ok(T data) {
        return new ResponseWrapper<>(data, "OK", "200");
    }

    public static <T> ResponseWrapper<T> ok(T data, String message) {
        return new ResponseWrapper<>(data, message, "200");
    }

    public static <T> ResponseWrapper<T> error(String message) {
        return new ResponseWrapper<>(null, message, "500");
    }

    public static <T> ResponseWrapper<T> error(T data, String message) {
        return new ResponseWrapper<>(data, message, "500");
    }

    public static <T> ResponseWrapper<T> withStatus(T data, String message, String statusCode) {
        return new ResponseWrapper<>(data, message, statusCode);
    }

    public static <T> ResponseWrapper<T> empty() {
        return new ResponseWrapper<>(null, "No Content", "204");
    }
}
