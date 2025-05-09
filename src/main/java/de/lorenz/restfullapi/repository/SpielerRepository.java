package de.lorenz.restfullapi.repository;

import de.lorenz.restfullapi.model.UserData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpielerRepository extends JpaRepository<UserData, String> {

    // Optional<UserData> findByUuid(String uuid);
    // Auskommentiert da dies nur für einzelne Felder gut ist, da ich aber eine Response mit
    // allen Daten erwarte ist diese Methode vorerst nicht zu gebrauchen

    List<UserData> findSpielerdatenByUuid(String uuid);
    List<UserData> findSpielerdatenByName(String name);
    List<UserData> findSpielerdatenByIp(String ip);
}
