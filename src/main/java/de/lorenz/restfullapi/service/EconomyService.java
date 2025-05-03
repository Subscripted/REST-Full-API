package de.lorenz.restfullapi.service;

import de.lorenz.restfullapi.model.Testtable;
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
        Optional<Testtable> economy = economyRepository.findByUuid(uuid);
        return economy.map(Testtable::getCoins).orElse(0);
    }


    public Testtable setUserCoins(String uuid, int coins) {
        Testtable eco = economyRepository.findById(uuid)
                .orElseThrow(() -> new RuntimeException("User not found"));

        eco.setCoins(coins);
        return economyRepository.save(eco);
    }
}
