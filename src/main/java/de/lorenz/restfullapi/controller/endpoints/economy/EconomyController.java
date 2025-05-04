package de.lorenz.restfullapi.controller.endpoints.economy;

import de.lorenz.restfullapi.dto.AllUserCoinsResponse;
import de.lorenz.restfullapi.dto.ResponseWrapper;
import de.lorenz.restfullapi.dto.SetCoinsRequest;
import de.lorenz.restfullapi.dto.UserCoinsResponse;
import de.lorenz.restfullapi.service.EconomyService;
import de.lorenz.restfullapi.service.TokenService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            return ResponseEntity.status(401).body("{\"error\": \"Unauthorized\"}");
        }
        int coins = economyService.getUserCoins(uuid);
        return ResponseEntity.ok(new UserCoinsResponse(uuid, coins));
    }

    @PostMapping("/{uuid}/coins")
    public ResponseEntity<?> setUserCoins(@PathVariable String uuid, @RequestHeader(value = "Authorization", required = false) String authHeader, @RequestBody SetCoinsRequest request) {
        if (!isAuthorized(authHeader)) {
            return ResponseEntity.status(401).body("{\"error\": \"Unauthorized\"}");
        }
        return ResponseEntity.ok(economyService.setUserCoins(uuid, request.getCoins()));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllUserCoins(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (!isAuthorized(authHeader)) {
            return ResponseEntity.status(401).body("{\"error\": \"Unauthorized\"}");
        }

        List<AllUserCoinsResponse> coins = economyService.getCoinsOfAll();
        return ResponseEntity.ok(new ResponseWrapper<>(coins));
    }


    @PostConstruct
    public void init() {
        System.out.println(this.getClass().getSimpleName() + " wurde geladen!");
    }
}
