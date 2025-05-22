package de.lorenz.restfullapi.security;

import de.lorenz.restfullapi.security.ratelimit.RateLimiter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {

    @Bean
    public FilterRegistrationBean<RateLimiter> rateLimitFilter() {
        FilterRegistrationBean<RateLimiter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new RateLimiter());
        registration.addUrlPatterns("/api/v1");
        return registration;
    }
}
