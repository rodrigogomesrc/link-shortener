package space.rodrigorocha.user_service.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import space.rodrigorocha.user_service.model.dto.RoleAssignmentRequestRecord;
import space.rodrigorocha.user_service.model.dto.UserCreationRequestRecord;
import space.rodrigorocha.user_service.service.UserService;

import java.util.Collections;
import java.util.Map;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> createUser(@Valid @RequestBody UserCreationRequestRecord request) {
        String userId = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(Collections.singletonMap("userId", userId));
    }

    @PatchMapping("/{userId}/deactivate")
    public ResponseEntity<Void> deactivateUser(@PathVariable String userId) {
        userService.deactivateUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{userId}/roles")
    public ResponseEntity<Void> assignRole(
            @PathVariable String userId,
            @Valid @RequestBody RoleAssignmentRequestRecord request) {

        userService.assignRoleToUser(userId, request.roleName());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}/roles/{roleName}")
    public ResponseEntity<Void> removeRole(
            @PathVariable String userId,
            @PathVariable String roleName) {

        userService.removeRoleFromUser(userId, roleName);
        return ResponseEntity.noContent().build();
    }

}
