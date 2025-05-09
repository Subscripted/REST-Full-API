package de.lorenz.restfullapi.service;

import de.lorenz.restfullapi.model.Antrag;
import de.lorenz.restfullapi.model.ForumUser;
import de.lorenz.restfullapi.model.UserData;
import de.lorenz.restfullapi.repository.AntragRepository;
import de.lorenz.restfullapi.repository.ForumChatMessageRepository;
import de.lorenz.restfullapi.repository.ForumUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ForumUserDataService {

    private final ForumUserRepository repository;
    private final ForumChatMessageRepository forumChatMessageRepository;
    private final AntragRepository antragRepository;

    public List<ForumUser> getDatenByUserId(Long userId) {
        return repository.findForumUserByUserId(userId);
    }

    public List<ForumUser> getDatenByName(String username) {
        return repository.findForumUserByUsername(username);
    }

    public List<ForumUser> getDatenByEmail(String email) {
        return repository.findForumUserByEmail(email);
    }

    public List<ForumUser> getDatenByRank(String rank) {
        return repository.findForumUserByRank(rank);
    }

    @Transactional
    public void deleteSingleByUserId(Long userId) {
        // 1. Zuerst alle Chatnachrichten zu den Anträgen des Users löschen
        var antraege = antragRepository.findByUser_UserId(userId);
        for (Antrag antrag : antraege) {
            forumChatMessageRepository.deleteByAntrag_AntragsId(antrag.getAntragsId());
        }

        // 2. Anträge löschen
        antragRepository.deleteByUser_UserId(userId);
        antragRepository.deleteByTeamler_UserId(userId);

        // 3. Chatnachrichten, die direkt vom User gesendet wurden (außerhalb Antrag ggf.)
        forumChatMessageRepository.deleteBySender_UserId(userId);

        // 4. Den User selbst löschen
        repository.deleteForumUserByUserId(userId);
    }




}
