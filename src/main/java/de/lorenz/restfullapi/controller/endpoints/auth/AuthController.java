package de.lorenz.restfullapi.controller.endpoints.auth;

import de.lorenz.restfullapi.dto.TokenRequest;
import de.lorenz.restfullapi.model.LoginCreds;
import de.lorenz.restfullapi.repository.LoginCredsRepository;
import de.lorenz.restfullapi.service.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final TokenService tokenService;
    private final LoginCredsRepository loginCredsRepository;

    public AuthController(TokenService tokenService, LoginCredsRepository loginCredsRepository) {
        this.tokenService = tokenService;
        this.loginCredsRepository = loginCredsRepository;
    }

    @PostMapping("/token")
    public ResponseEntity<?> createToken(@RequestBody TokenRequest tokenRequest) {
        Optional<LoginCreds> creds = loginCredsRepository.findByEmailAndClientIdAndClientSecret(
                tokenRequest.getEmail(),
                tokenRequest.getClientId(),
                tokenRequest.getClientSecret()
        );

        if (creds.isEmpty()) {
            return ResponseEntity.status(401).body("Ungültige Anmeldedaten");
        }

        Optional<String> existingToken = tokenService.getValidToken(tokenRequest.getEmail());

        Map<String, String> response = new HashMap<>();
        if (existingToken.isPresent()) {
            response.put("token", existingToken.get());
            return ResponseEntity.ok(response);
        }

        String newToken = tokenService.generateToken(tokenRequest.getEmail());
        response.put("token", newToken);
        return ResponseEntity.ok(response);

    }

}
