package de.lorenz.restfullapi.controller.endpoints.auth;

import de.lorenz.restfullapi.dto.get.TokenRequestGet;
import de.lorenz.restfullapi.dto.wrapper.ResponseWrapper;
import de.lorenz.restfullapi.global.exception.GlobalExceptionMsg;
import de.lorenz.restfullapi.model.LoginCreds;
import de.lorenz.restfullapi.repository.LoginCredsRepository;
import de.lorenz.restfullapi.service.TokenService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthController {

    private final TokenService tokenService;
    private final LoginCredsRepository loginCredsRepository;

    @PostMapping("/token")
    public ResponseWrapper<Map<String, String>> getToken(@RequestBody TokenRequestGet tokenRequest) {

        Optional<LoginCreds> creds = loginCredsRepository.findByEmailAndClientIdAndClientSecret(

                tokenRequest.email(),
                tokenRequest.clientId(),
                tokenRequest.clientSecret()
        );

        Map<String, String> response = new HashMap<>();
        if (creds.isEmpty()) {
            response.put("message", GlobalExceptionMsg.UNAUTHORIZED.getExceptionMsg());
            return ResponseWrapper.unauthorized(response, GlobalExceptionMsg.WRONG_LOGIN_CREDS.getExceptionMsg());
        }

        Optional<String> existingToken = tokenService.getValidToken(tokenRequest.email());

        response = new HashMap<>();
        if (existingToken.isPresent()) {
            response.put("token", existingToken.get());
            return ResponseWrapper.ok(response);
        }

        String newToken = tokenService.generateToken(tokenRequest.email());
        response.put("message", GlobalExceptionMsg.TOKEN_RESPONSE.getExceptionMsg());
        response.put("token", newToken);
        return ResponseWrapper.ok(response);

    }
}
