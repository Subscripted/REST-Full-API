package de.lorenz.restfullapi.repository;

import de.lorenz.restfullapi.model.ForumUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ForumUserRepository extends JpaRepository<ForumUser, Long> {

    List<ForumUser> findForumUserByUserId(Long userId);

    List<ForumUser> findForumUserByEmail(String email);

    List<ForumUser> findForumUserByUsername(String username);

    List<ForumUser> findForumUserByRank(String rank);


    //Stand jetzt noch Fehler weil in der chat Tabelle noch ein User mit der id ist.
    @Transactional
    void deleteForumUserByUserId(Long userId);
}

