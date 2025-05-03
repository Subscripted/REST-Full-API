package de.lorenz.restfullapi.repository;

import de.lorenz.restfullapi.model.Economy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EconomyRepository extends JpaRepository<Economy, String> {

    Optional<Economy> findByUuid(String uuid);
}
