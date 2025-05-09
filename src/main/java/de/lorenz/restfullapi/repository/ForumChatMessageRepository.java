package de.lorenz.restfullapi.repository;

import de.lorenz.restfullapi.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ForumChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findByAntrag_AntragsIdOrderByTimeAsc(Long antragsId);

    @Transactional
    void deleteBySender_UserId(Long userId);

    @Transactional
    void deleteByAntrag_AntragsId(Long antragsId);

}
