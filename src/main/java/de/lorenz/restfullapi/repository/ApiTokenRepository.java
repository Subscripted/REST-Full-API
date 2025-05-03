package de.lorenz.restfullapi.repository;

import de.lorenz.restfullapi.model.ApiToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ApiTokenRepository extends JpaRepository<ApiToken, Long> {

    Optional<ApiToken> findByToken(String token);
    void deleteByExpiresAtBefore(LocalDateTime time);
    Optional<ApiToken> findByEmailAndExpiresAtAfter(String email, LocalDateTime now);

}
