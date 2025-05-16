package de.lorenz.restfullapi.service;

import de.lorenz.restfullapi.model.UserData;
import de.lorenz.restfullapi.repository.SpielerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SpielerdataService {

    private final SpielerRepository repository;

    public Map<String, Object> getDatenByUuid(String uuid) {
        List<UserData> daten = repository.findSpielerdatenByUuid(uuid);
        UserData user = daten.isEmpty() ? null : daten.get(0);

        Map<String, Object> response = new HashMap<>();
        response.put("daten", user);
        return response;
    }


    public Map<String, Object> getDatenByName(String name) {
        List<UserData> daten = repository.findSpielerdatenByName(name);
        UserData user = daten.isEmpty() ? null : daten.get(0);

        Map<String, Object> response = new HashMap<>();
        response.put("daten", user);
        return response;
    }

    public Map<String, Object> getDatenByIp(String ip) {
        List<UserData> daten = repository.findSpielerdatenByIp(ip);
        UserData user = daten.isEmpty() ? null : daten.get(0);

        Map<String, Object> response = new HashMap<>();
        response.put("daten", user);
        return response;
    }
}
