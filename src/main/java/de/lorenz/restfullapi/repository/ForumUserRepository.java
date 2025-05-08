package de.lorenz.restfullapi.repository;

import de.lorenz.restfullapi.model.ForumUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForumUserRepository extends JpaRepository<ForumUser, Long> {}