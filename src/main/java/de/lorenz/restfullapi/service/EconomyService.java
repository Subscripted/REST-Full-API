package de.lorenz.restfullapi.service;

import de.lorenz.restfullapi.model.Economy;
import de.lorenz.restfullapi.repository.EconomyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EconomyService {

    private final EconomyRepository economyRepository;

    @Autowired
    public EconomyService(EconomyRepository economyRepository) {
        this.economyRepository = economyRepository;
    }

    public int getUserCoins(String uuid) {
        Optional<Economy> economy = economyRepository.findByUuid(uuid);
        return economy.map(Economy::getCoins).orElse(0);
    }


    public Economy setUserCoins(String uuid, int coins) {
        Economy eco = economyRepository.findById(uuid)
                .orElseThrow(() -> new RuntimeException("User not found"));

        eco.setCoins(coins);
        return economyRepository.save(eco);
    }
}
