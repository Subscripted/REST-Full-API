package de.lorenz.restfullapi.service;

import de.lorenz.restfullapi.model.ForumUser;
import de.lorenz.restfullapi.model.UserData;
import de.lorenz.restfullapi.repository.ForumUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ForumUserDataService {

    private final ForumUserRepository repository;

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

    public void deleteSingleByUserId(Long userId) {
        repository.deleteForumUserByUserId(userId);
    }



}
