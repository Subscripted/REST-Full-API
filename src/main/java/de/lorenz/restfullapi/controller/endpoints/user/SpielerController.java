package de.lorenz.restfullapi.controller.endpoints.user;

import de.lorenz.restfullapi.dto.wrapper.ResponseWrapper;
import de.lorenz.restfullapi.model.UserData;
import de.lorenz.restfullapi.service.SpielerdataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/spieler")
public class SpielerController {

    private final SpielerdataService spielerdatenService;

    @GetMapping("/uuid/{uuid}")
    public ResponseEntity<?> getSpielerdatenByUUID(@PathVariable String uuid, @RequestHeader(value = "Authorization", required = false) String authHeader) {

        List<UserData> daten = spielerdatenService.getDatenByUuid(uuid);
        return ResponseEntity.ok(ResponseWrapper.ok(daten));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<?> getSpielerdatenByName(@PathVariable String name, @RequestHeader(value = "Authorization", required = false) String authHeader) {

        List<UserData> daten = spielerdatenService.getDatenByName(name);
        return ResponseEntity.ok(ResponseWrapper.ok(daten));
    }

    @GetMapping("/ip/{ip}")
    public ResponseEntity<?> getSpielerdatenByIp(@PathVariable String ip, @RequestHeader(value = "Authorization", required = false) String authHeader) {

        List<UserData> daten = spielerdatenService.getDatenByIp(ip);
        return ResponseEntity.ok(ResponseWrapper.ok(daten));
    }
}
