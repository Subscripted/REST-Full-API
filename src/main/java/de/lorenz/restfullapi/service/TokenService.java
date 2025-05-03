package de.lorenz.restfullapi.service;

import de.lorenz.restfullapi.model.ApiToken;
import de.lorenz.restfullapi.repository.ApiTokenRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class TokenService {

    private final ApiTokenRepository apiTokenRepository;

    public TokenService(ApiTokenRepository apiTokenRepository) {
        this.apiTokenRepository = apiTokenRepository;
    }

    public String generateToken(String email) {
        String tokenValue = UUID.randomUUID().toString();
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(5);
        ApiToken token = new ApiToken();
        token.setEmail(email);
        token.setExpiresAt(expiresAt);
        token.setToken(tokenValue);

        apiTokenRepository.save(token);
        return tokenValue;
    }

    public boolean isTokenValid(String tokenValue) {
        return apiTokenRepository.findByToken(tokenValue)
                .filter(t -> t.getExpiresAt().isAfter(LocalDateTime.now()))
                .isPresent();
    }

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void cleanExpiredTokens() {
        apiTokenRepository.deleteByExpiresAtBefore(LocalDateTime.now());
    }

    public Optional<String> getValidToken(String email) {
        LocalDateTime now = LocalDateTime.now();
        return apiTokenRepository.findByEmailAndExpiresAtAfter(email, now)
                .map(ApiToken::getToken); // nur Token-String zurückgeben
    }



}
