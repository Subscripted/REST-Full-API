package de.lorenz.restfullapi.controller.endpoints;

import de.lorenz.restfullapi.dto.TokenRequest;
import de.lorenz.restfullapi.model.LoginCreds;
import de.lorenz.restfullapi.repository.LoginCredsRepository;
import de.lorenz.restfullapi.service.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

        // ⏳ 1. Prüfen: Gibt es schon einen gültigen Token?
        Optional<String> existingToken = tokenService.getValidToken(tokenRequest.getEmail());

        if (existingToken.isPresent()) {
            // ✔️ 2. Falls ja: einfach bestehenden Token zurückgeben
            return ResponseEntity.ok(existingToken.get());
        }

        // 🛠️ 3. Falls nein: neuen Token erstellen
        String newToken = tokenService.generateToken(tokenRequest.getEmail());

        return ResponseEntity.ok(newToken);
    }

}
