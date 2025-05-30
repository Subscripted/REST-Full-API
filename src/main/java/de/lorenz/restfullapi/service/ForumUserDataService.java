package de.lorenz.restfullapi.service;

import de.lorenz.restfullapi.model.Antrag;
import de.lorenz.restfullapi.model.ForumUser;
import de.lorenz.restfullapi.repository.AntragRepository;
import de.lorenz.restfullapi.repository.ForumChatMessageRepository;
import de.lorenz.restfullapi.repository.ForumUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        antragRepository.nullifyUserInAntrag(userId);
        antragRepository.nullifyTeamlerInAntrag(userId);
        forumChatMessageRepository.nullifySenderByUserId(userId);
        repository.deleteForumUserByUserId(userId);
    }



    public void createUser(ForumUser forumUser) {;
        repository.save(forumUser);
    }

    public void saveForumUser(ForumUser forumUser) {
        repository.save(forumUser);
    }

}
