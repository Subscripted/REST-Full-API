package de.lorenz.restfullapi.service;

import de.lorenz.restfullapi.dto.AllUserCoinsResponse;
import de.lorenz.restfullapi.model.Testtable;
import de.lorenz.restfullapi.repository.EconomyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EconomyService {

    private final EconomyRepository economyRepository;

    @Autowired
    public EconomyService(EconomyRepository economyRepository) {
        this.economyRepository = economyRepository;
    }

    public int getUserCoins(String uuid) {
        Optional<Testtable> economy = economyRepository.findByUuid(uuid);
        return economy.map(Testtable::getCoins).orElse(0);
    }

    public List<AllUserCoinsResponse> getCoinsOfAll() {
        Iterable<Testtable> allEconomies = economyRepository.findAll();
        List<AllUserCoinsResponse> list = new ArrayList<>();
        for (Testtable entry : allEconomies) {
            list.add(new AllUserCoinsResponse(entry.getUuid(), entry.getCoins()));
        }
        return list;
    }

    public Testtable setUserCoins(String uuid, int coins) {
        Testtable eco = economyRepository.findById(uuid)
                .orElseThrow(() -> new RuntimeException("User not found"));
        eco.setCoins(coins);
        return economyRepository.save(eco);
    }
}
