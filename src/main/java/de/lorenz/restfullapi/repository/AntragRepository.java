package de.lorenz.restfullapi.repository;

import de.lorenz.restfullapi.model.Antrag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AntragRepository extends JpaRepository<Antrag, Long> {}