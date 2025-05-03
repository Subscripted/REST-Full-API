package de.lorenz.restfullapi.controller.endpoints;

import de.lorenz.restfullapi.model.Economy;
import de.lorenz.restfullapi.service.EconomyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/economy")
public class EconomyController {

    private final EconomyService economyService;

    @Autowired
    public EconomyController(EconomyService economyService) {
        this.economyService = economyService;
    }

    @GetMapping("/{uuid}/coins")
    public int getUserCoins(@PathVariable String uuid) {
        return economyService.getUserCoins(uuid);
    }

    @PostMapping("/{uuid}/coins/{coins}")
    public Economy setUserCoins(@PathVariable String uuid, @PathVariable int coins) {
        return economyService.setUserCoins(uuid, coins);
    }
}
