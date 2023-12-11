package com.kotkina.quotesServiceApi.web.controllers;

import com.kotkina.quotesServiceApi.entities.User;
import com.kotkina.quotesServiceApi.services.UserService;
import com.kotkina.quotesServiceApi.utils.mappers.UserMapper;
import com.kotkina.quotesServiceApi.web.models.requests.UserRegisterRequest;
import com.kotkina.quotesServiceApi.web.models.responses.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User Manager")
public class UserController {

    private final UserService userService;

    private final UserMapper userMapper;

    @Operation(summary = "A new user")
    @PostMapping("/new")
    public ResponseEntity<UserResponse> addUser(@ParameterObject @Valid UserRegisterRequest request) {
        User savedUser = userService.save(userMapper.requestToUser(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.userToResponse(savedUser));
    }

    @Operation(summary = "The user with id")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable long id) {
        return ResponseEntity.ok(userMapper.userToResponse(userService.getById(id)));
    }
}
