package de.lorenz.restfullapi.service;

import de.lorenz.restfullapi.model.UserData;
import de.lorenz.restfullapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserdataService {

    private final UserRepository repository;

    public UserdataService(UserRepository repository) {
        this.repository = repository;
    }

    public List<UserData> getDatenByUuid(String uuid) {
        return repository.findSpielerdatenByUuid(uuid);
    }

    public List<UserData> getDatenByName(String name) {
        return repository.findSpielerdatenByName(name);
    }
    public List<UserData> getDatenByIp(String ip) {
        return repository.findSpielerdatenByIp(ip);
    }

    /**
     public boolean updateNameByUuid(String uuid, String name) {
     Optional<UserData> optionalData = repository.findByUuid(uuid);
     if (optionalData.isPresent()) {
     UserData data = optionalData.get();
     data.setName(name);
     repository.save(data);
     return true;
     } else {
     return false;
     }
     }
     `*/
}
