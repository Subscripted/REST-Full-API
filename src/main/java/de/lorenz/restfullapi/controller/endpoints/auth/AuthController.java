package de.lorenz.restfullapi.controller.endpoints.auth;

import de.lorenz.restfullapi.dto.TokenRequest;
import de.lorenz.restfullapi.dto.wrapper.ResponseWrapper;
import de.lorenz.restfullapi.global.exception.GlobalExceptionMsg;
import de.lorenz.restfullapi.model.LoginCreds;
import de.lorenz.restfullapi.repository.LoginCredsRepository;
import de.lorenz.restfullapi.service.TokenService;
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
    Map<String, String> response;

    public AuthController(TokenService tokenService, LoginCredsRepository loginCredsRepository) {
        this.tokenService = tokenService;
        this.loginCredsRepository = loginCredsRepository;
    }

    @PostMapping("/token")
    public ResponseWrapper<Map<String, String>> getToken(@RequestBody TokenRequest tokenRequest) {
        Optional<LoginCreds> creds = loginCredsRepository.findByEmailAndClientIdAndClientSecret(
                tokenRequest.getEmail(),
                tokenRequest.getClientId(),
                tokenRequest.getClientSecret()
        );

        response = new HashMap<>();
        if (creds.isEmpty()) {
            response.put("message", GlobalExceptionMsg.UNAUTHORIZED.getExceptionMsg());
            return ResponseWrapper.unauthorized(response, GlobalExceptionMsg.WRONG_LOGIN_CREDS.getExceptionMsg());
        }

        Optional<String> existingToken = tokenService.getValidToken(tokenRequest.getEmail());

        response = new HashMap<>();
        if (existingToken.isPresent()) {
            response.put("message", GlobalExceptionMsg.TOKEN_RESPONSE.getExceptionMsg());
            response.put("token", existingToken.get());
            return ResponseWrapper.ok(response);
        }

        String newToken = tokenService.generateToken(tokenRequest.getEmail());
        response.put("token", newToken);
        return ResponseWrapper.ok(response);

    }
}
