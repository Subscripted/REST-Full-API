package de.lorenz.restfullapi.repository;


import de.lorenz.restfullapi.model.Testtable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EconomyRepository extends JpaRepository<Testtable, String> {

    Optional<Testtable> findByUuid(String uuid);
}
