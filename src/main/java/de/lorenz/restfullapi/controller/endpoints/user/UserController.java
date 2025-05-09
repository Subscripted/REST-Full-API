package de.lorenz.restfullapi.controller.endpoints.user;

import de.lorenz.restfullapi.dto.wrapper.ResponseWrapper;
import de.lorenz.restfullapi.model.UserData;
import de.lorenz.restfullapi.service.UserdataService;
import de.lorenz.restfullapi.service.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserdataService spielerdatenService;
    private final TokenService tokenService;

    public UserController(UserdataService spielerdatenService, TokenService tokenService) {
        this.spielerdatenService = spielerdatenService;
        this.tokenService = tokenService;
    }

    @GetMapping("/uuid/{uuid}")
    public ResponseEntity<?> getSpielerdatenByUUID(@PathVariable String uuid, @RequestHeader(value = "Authorization", required = false) String authHeader) {

        List<UserData> daten = spielerdatenService.getDatenByUuid(uuid);
        return ResponseEntity.ok(new ResponseWrapper<>(daten));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<?> getSpielerdatenByName(@PathVariable String name, @RequestHeader(value = "Authorization", required = false) String authHeader) {

        List<UserData> daten = spielerdatenService.getDatenByName(name);
        return ResponseEntity.ok(new ResponseWrapper<>(daten));
    }

    @GetMapping("/ip/{ip}")
    public ResponseEntity<?> getSpielerdatenByIp(@PathVariable String ip, @RequestHeader(value = "Authorization", required = false) String authHeader) {

        List<UserData> daten = spielerdatenService.getDatenByIp(ip);
        return ResponseEntity.ok(new ResponseWrapper<>(daten));
    }

    /** Kleine Methode zum Herumtesten von POST werten
     *
    @PostMapping("/set/{name}/{uuid}")
    public ResponseEntity<?> setName(@PathVariable String name,
                                     @PathVariable String uuid,
                                     @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (!isAuthorized(authHeader)) {
            return ResponseEntity.status(401).body("{\"error\": \"Unauthorized\"}");
        }

        boolean success = spielerdatenService.updateNameByUuid(uuid, name);
        if (success) {
            return ResponseEntity.ok("{\"success\": \"Name geändert\"}");
        } else {
            return ResponseEntity.status(404).body("{\"error\": \"Benutzer nicht gefunden\"}");
        }
    }
                                     */
}
