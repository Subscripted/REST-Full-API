package de.lorenz.restfullapi.controller.endpoints;

import de.lorenz.restfullapi.service.EconomyService;
import de.lorenz.restfullapi.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/economy")
public class EconomyController {

    private final EconomyService economyService;
    private final TokenService tokenService;

    @Autowired
    public EconomyController(EconomyService economyService, TokenService tokenService) {
        this.economyService = economyService;
        this.tokenService = tokenService;
    }

    private boolean isAuthorized(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            return tokenService.isTokenValid(token);
        }
        return false;
    }

    @GetMapping("/{uuid}/coins")
    public ResponseEntity<?> getUserCoins(@PathVariable String uuid, @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (!isAuthorized(authHeader)) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        return ResponseEntity.ok(economyService.getUserCoins(uuid));
    }


    @PostMapping("/{uuid}/coins/{coins}")
    public ResponseEntity<?> setUserCoins(@PathVariable String uuid, @PathVariable int coins,
                                          @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (!isAuthorized(authHeader)) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        return ResponseEntity.ok(economyService.setUserCoins(uuid, coins));
    }


}
