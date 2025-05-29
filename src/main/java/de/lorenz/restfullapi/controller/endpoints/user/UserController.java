package de.lorenz.restfullapi.controller.endpoints.user;

import de.lorenz.restfullapi.dto.ForumUserRequestUpdate;
import de.lorenz.restfullapi.dto.wrapper.ResponseWrapper;
import de.lorenz.restfullapi.global.exception.GlobalExceptionMsg;
import de.lorenz.restfullapi.model.ForumUser;
import de.lorenz.restfullapi.repository.ForumUserRepository;
import de.lorenz.restfullapi.service.ForumUserDataService;
import de.lorenz.restfullapi.utils.RestUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final ForumUserDataService forumUserDataService;
    private final ForumUserRepository repository;
    private final RestUtils restUtils;

    private Map<String, Object> json;

    @DeleteMapping("/deleteUser/{userId}")
    public ResponseWrapper<?> deleteUser(@PathVariable Long userId) {
        forumUserDataService.deleteSingleByUserId(userId);

        json = new HashMap<>();
        json.put("message", GlobalExceptionMsg.USER_DELETED_SUCCESSFULLY.getExceptionMsg());

        return ResponseWrapper.ok(json);
    }

    @PutMapping("/update/{userId}")
    public ResponseWrapper<?> updateUser(@PathVariable Long userId, @RequestBody ForumUserRequestUpdate request) {
        ForumUser existingUser = repository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (request.username() != null) {
            existingUser.setUsername(request.username());
        }
        if (request.email() != null && !request.email().equals(existingUser.getEmail())) {
            if (repository.existsByEmail(request.email())) {
                return ResponseWrapper.error("E-Mail bereits vergeben");
            }
            existingUser.setEmail(request.email());
        }
        if (request.password() != null) {
            existingUser.setPassword(request.password());
        }
        if (request.rank() != null) {
            existingUser.setRank(request.rank());
        }

        repository.save(existingUser);

        return ResponseWrapper.ok(Map.of("message", "User updated", "userId", userId));
    }

    @PostMapping("/create")
    public ResponseWrapper<Map<String, Object>> createUser(@RequestBody ForumUser user) {
        user.setUserId(restUtils.generateUserId());
        if (user.getEmail() == null || user.getEmail().equals("")) {
            json = new HashMap<>();
            json.put("message", GlobalExceptionMsg.USER_NO_CREATION_MISSING_CREDENTIALS.getExceptionMsg());
            return ResponseWrapper.badRequest(json, "Email is required");
        }

        if (user.getPassword() == null || user.getPassword().equals("")) {
            json = new HashMap<>();
            json.put("message", GlobalExceptionMsg.USER_NO_CREATION_MISSING_CREDENTIALS.getExceptionMsg());
            return ResponseWrapper.badRequest(json, "Password is required");
        }

        if (user.getUsername() == null || user.getUsername().equals("")) {
            json = new HashMap<>();
            json.put("message", GlobalExceptionMsg.USER_NO_CREATION_MISSING_CREDENTIALS.getExceptionMsg());

            return ResponseWrapper.badRequest(json, "Username is required");
        }

        if (repository.existsByEmail(user.getEmail())) {
            json = new HashMap<>();
            json.put("message", GlobalExceptionMsg.USER_NO_CREATION_ALREADY_EXISTS.getExceptionMsg());
            json.put("email", user.getEmail());
            return ResponseWrapper.error(json, "User with that email already exists");
        }


        if (repository.existsByUsername(user.getUsername())) {
            json = new HashMap<>();
            json.put("message", GlobalExceptionMsg.USER_NO_CREATION_ALREADY_EXISTS.getExceptionMsg());
            json.put("username", user.getUsername());
            return ResponseWrapper.error(json, "User with that Name already exists");
        }

        if (repository.existsById(user.getUserId())) {
            return ResponseWrapper.badRequest("User ID already exists.");
        }
        forumUserDataService.createUser(user);

        json = new HashMap<>();
        json.put("message", GlobalExceptionMsg.USER_CREATED.getExceptionMsg());
        json.put("userId", user.getUserId());

        return ResponseWrapper.ok(json, "User with ID (" + user.getUserId() + ") created successfully");
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
