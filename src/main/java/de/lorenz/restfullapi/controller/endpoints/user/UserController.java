package de.lorenz.restfullapi.controller.endpoints.user;

import de.lorenz.restfullapi.dto.wrapper.ResponseWrapper;
import de.lorenz.restfullapi.model.ForumUser;
import de.lorenz.restfullapi.model.UserData;
import de.lorenz.restfullapi.repository.ForumUserRepository;
import de.lorenz.restfullapi.service.ForumUserDataService;
import de.lorenz.restfullapi.service.SpielerdataService;
import de.lorenz.restfullapi.utils.RestUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final ForumUserDataService forumUserDataService;
    private final ForumUserRepository repository;
    private final RestUtils restUtils;

    @DeleteMapping("/deleteUser/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        forumUserDataService.deleteSingleByUserId(userId);
        return ResponseEntity.ok("User deleted successfully");
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody ForumUser user) {
        user.setUserId(restUtils.generateUserId());
        //todo: Späteres Problem was auftreten könnte, das die LocalDatetime eine Stunde zurückliegt, Filter für Zeitzonen entwicklen
        user.setCreation_date(LocalDateTime.now());

        if (user.getEmail() == null) {
            return ResponseEntity.badRequest().body("Email is required");
        }

        if (user.getPassword() == null) {
            return ResponseEntity.badRequest().body("Password is required");
        }

        if (user.getUsername() == null) {
            return ResponseEntity.badRequest().body("Username is required");
        }

        if (repository.existsByEmail(user.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User with that Email already exists.");
        }
        if (repository.existsById(user.getUserId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User ID already exists.");
        }
        forumUserDataService.createUser(user);
        return ResponseEntity.ok("User created with ID: " + user.getUserId());
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
