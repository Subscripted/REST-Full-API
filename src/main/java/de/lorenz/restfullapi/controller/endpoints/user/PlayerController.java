package de.lorenz.restfullapi.controller.endpoints.user;

import de.lorenz.restfullapi.dto.wrapper.ResponseWrapper;
import de.lorenz.restfullapi.global.exception.GlobalExceptionMsg;
import de.lorenz.restfullapi.service.PlayerdataService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/spieler")
public class PlayerController {

    private final PlayerdataService spielerdatenService;

    @GetMapping("/uuid/{uuid}")
    public ResponseWrapper<?> getSpielerdatenByUUID(@PathVariable String uuid, @RequestHeader(value = "Authorization", required = false) String authHeader) {
        Map<String, Object> daten = spielerdatenService.getDatenByUuid(uuid);
        if (!datenExists(daten)) {
            return ResponseWrapper.notFound(daten, String.format(GlobalExceptionMsg.SPIELER_DATEN_BY_UUID_EMPTY.getExceptionMsg(), uuid));
        }
        daten.put("message", GlobalExceptionMsg.PLAYER_DATA_BY_NAME.getExceptionMsg());

        return ResponseWrapper.ok(daten, "User Data Send Successfully");
    }


    @GetMapping("/name/{name}")
    public ResponseWrapper<?> getSpielerdatenByName(@PathVariable String name, @RequestHeader(value = "Authorization", required = false) String authHeader) {
        Map<String, Object> daten = spielerdatenService.getDatenByName(name);
        if (!datenExists(daten)) {
            return ResponseWrapper.notFound(daten, String.format(GlobalExceptionMsg.SPIELER_DATEN_BY_NAME_EMPTY.getExceptionMsg(), name));
        }
        daten.put("message", "User data fetched by name");
        return ResponseWrapper.ok(daten, "User Data Send Successfully");
    }


    @GetMapping("/ip/{ip}")
    public ResponseWrapper<?> getSpielerdatenByIp(@PathVariable String ip, @RequestHeader(value = "Authorization", required = false) String authHeader) {
        Map<String, Object> daten = spielerdatenService.getDatenByIp(ip);

        if (!datenExists(daten)) {
            return ResponseWrapper.notFound(daten, String.format(GlobalExceptionMsg.SPIELER_DATEN_BY_IP_EMPTY.getExceptionMsg(), ip));
        }
        daten.put("message", "User data fetched by IP");
        return ResponseWrapper.ok(daten, "User Data Send Successfully");
    }

    private boolean datenExists(Map<String, Object> daten) {
        Object d = daten.get("daten");
        return d != null && (!(d instanceof List) || !((List<?>) d).isEmpty());
    }
}
