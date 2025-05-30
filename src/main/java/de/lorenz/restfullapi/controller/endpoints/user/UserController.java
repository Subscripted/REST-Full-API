package de.lorenz.restfullapi.controller.endpoints.user;

import de.lorenz.restfullapi.dto.post.ForumUserRequestCreate;
import de.lorenz.restfullapi.dto.put.ForumUserRequestUpdate;
import de.lorenz.restfullapi.dto.put.ForumUserUpdateResponse;
import de.lorenz.restfullapi.dto.wrapper.ResponseWrapper;
import de.lorenz.restfullapi.global.exception.GlobalExceptionMsg;
import de.lorenz.restfullapi.model.ForumUser;
import de.lorenz.restfullapi.repository.ForumUserRepository;
import de.lorenz.restfullapi.service.ForumUserDataService;
import de.lorenz.restfullapi.utils.RestUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
//todo Else-Blöcke einbauen
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


        Map<String, Object> changedFields = new HashMap<>();
        if (request.username() != null ) {
            existingUser.setUsername(request.username());
            changedFields.put("username", request.username());
        }

        if (request.email() != null && !request.email().equals(existingUser.getEmail())) {
            if (repository.existsByEmail(request.email())) {
                return ResponseWrapper.error("E-Mail already in use");
            }
            existingUser.setEmail(request.email());
            changedFields.put("email", existingUser.getEmail());
        }
        if (request.password() != null) {
            existingUser.setPassword(request.password());
            changedFields.put("password", request.password());
        }
        if (request.rank() != null) {
            existingUser.setRank(request.rank());
            changedFields.put("rank", existingUser.getRank());
        }

        forumUserDataService.saveForumUser(existingUser);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "User Updated Successfully");
        response.put("changedFields", new ForumUserUpdateResponse(userId, changedFields));
        return ResponseWrapper.ok(response);
    }

    @PostMapping("/create")
    public ResponseWrapper<Map<String, Object>> createUser(@RequestBody ForumUserRequestCreate user) {
        json = new HashMap<>();

        if (user.email() == null || user.email().isEmpty()) {
            json.put("message", GlobalExceptionMsg.USER_NO_CREATION_MISSING_CREDENTIALS.getExceptionMsg());
            return ResponseWrapper.badRequest(json, "Email is required");
        }

        if (user.password() == null || user.password().isEmpty()) {
            json.put("message", GlobalExceptionMsg.USER_NO_CREATION_MISSING_CREDENTIALS.getExceptionMsg());
            return ResponseWrapper.badRequest(json, "Password is required");
        }

        if (user.username() == null || user.username().isEmpty()) {
            json.put("message", GlobalExceptionMsg.USER_NO_CREATION_MISSING_CREDENTIALS.getExceptionMsg());
            return ResponseWrapper.badRequest(json, "Username is required");
        }

        if (repository.existsByEmail(user.email())) {
            json.put("message", GlobalExceptionMsg.USER_NO_CREATION_ALREADY_EXISTS.getExceptionMsg());
            json.put("email", user.email());
            return ResponseWrapper.error(json, "User with that email already exists");
        }

        if (repository.existsByUsername(user.username())) {
            json.put("message", GlobalExceptionMsg.USER_NO_CREATION_ALREADY_EXISTS.getExceptionMsg());
            json.put("username", user.username());
            return ResponseWrapper.error(json, "User with that Name already exists");
        }

        ForumUser newUser = new ForumUser();
        newUser.setUsername(user.username());
        newUser.setUserId(restUtils.generateUserId());
        newUser.setEmail(user.email());
        newUser.setPassword(user.password());
        newUser.setRank(user.rank());

        forumUserDataService.createUser(newUser);

        json.put("message", GlobalExceptionMsg.USER_CREATED.getExceptionMsg());
        json.put("userId", newUser.getUserId());

        return ResponseWrapper.ok(json, "User with ID (" + newUser.getUserId() + ") created successfully");
    }
}

