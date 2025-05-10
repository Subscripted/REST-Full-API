package de.lorenz.restfullapi.repository;

import de.lorenz.restfullapi.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ForumChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findByAntrag_AntragsIdOrderByTimeAsc(Long antragsId);

    @Transactional
    void deleteBySender_UserId(Long userId);

    @Transactional
    void deleteByAntrag_AntragsId(Long antragsId);

    @Modifying
    @Query("UPDATE ChatMessage c SET c.sender = NULL WHERE c.sender.userId = :userId")
    void nullifySenderByUserId(@Param("userId") Long userId);

}
