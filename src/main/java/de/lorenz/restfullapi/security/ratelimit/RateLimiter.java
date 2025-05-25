package de.lorenz.restfullapi.security.ratelimit;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.lorenz.restfullapi.dto.wrapper.ResponseWrapper;
import de.lorenz.restfullapi.global.exception.GlobalExceptionMsg;
import de.lorenz.restfullapi.global.exception.GlobalHttpStatusCode;
import de.lorenz.restfullapi.utils.json.JsonSimple;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimiter extends OncePerRequestFilter {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String ip = request.getRemoteAddr();
        Bucket bucket = buckets.computeIfAbsent(ip, this::newBucket);

        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(429);
            response.setContentType("application/json");

            Map<String, Object> innerResponse = new HashMap<>();
            innerResponse.put("message", GlobalExceptionMsg.TOO_MANY_REQUESTS.getExceptionMsg());

            ResponseWrapper<Object> wrapper = new ResponseWrapper<>(
                    innerResponse,
                    null,
                    GlobalHttpStatusCode.FORBIDDEN.getCode()
            );

            String json = objectMapper.writeValueAsString(wrapper);
            response.getWriter().write(json);
        }
    }

    private Bucket newBucket(String ip) {
        return Bucket.builder()
                .addLimit(Bandwidth.classic(10, Refill.intervally(10, Duration.ofMinutes(1))))
                .build();
    }
}
