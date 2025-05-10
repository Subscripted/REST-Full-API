package de.lorenz.restfullapi.repository;

import de.lorenz.restfullapi.model.Antrag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AntragRepository extends JpaRepository<Antrag, Long> {


    //todo: ggf. auf eine Query umbauen wenn es zu viele Datensätze gibt.
    List<Antrag> findAll();

    List<Antrag> findByUser_UserId(Long userId);


    void deleteByUser_UserId(Long userUserId);

    void deleteByTeamler_UserId(Long teamlerUserId);


    @Modifying
    @Query("UPDATE Antrag a SET a.user = NULL WHERE a.user.userId = :userId")
    void nullifyUserInAntrag(@Param("userId") Long userId);

    @Modifying
    @Query("UPDATE Antrag a SET a.teamler = NULL WHERE a.teamler.userId = :userId")
    void nullifyTeamlerInAntrag(@Param("userId") Long userId);
}