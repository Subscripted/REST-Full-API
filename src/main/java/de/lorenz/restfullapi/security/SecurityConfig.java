package de.lorenz.restfullapi.security;

import de.lorenz.restfullapi.auth.AuthEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.beans.factory.annotation.Autowired;


@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Autowired
    private AuthEntryPoint customAuthEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().permitAll()
                )
                .httpBasic(basic -> basic
                        .authenticationEntryPoint(customAuthEntryPoint)
                );

        return http.build();
    }
}
