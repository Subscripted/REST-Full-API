package de.lorenz.restfullapi.controller.endpoints.user;

import de.lorenz.restfullapi.dto.wrapper.ResponseWrapper;
import de.lorenz.restfullapi.model.ForumUser;
import de.lorenz.restfullapi.model.UserData;
import de.lorenz.restfullapi.service.ForumUserDataService;
import de.lorenz.restfullapi.service.SpielerdataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final ForumUserDataService forumUserDataService;

    @DeleteMapping("/deleteUser/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        forumUserDataService.deleteSingleByUserId(userId);
        return ResponseEntity.noContent().build();
    }

    /** Kleine Methode zum Herumtesten von POST werten
     *
     @PostMapping("/set/{name}/{uuid}") public ResponseEntity<?> setName(@PathVariable String name,
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
