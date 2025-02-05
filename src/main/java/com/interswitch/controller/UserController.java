package com.interswitch.controller;

import com.interswitch.dto.request.CreateUserRequest;
import com.interswitch.dto.request.LoginRequest;
import com.interswitch.dto.response.CreateUserResponse;
import com.interswitch.dto.response.LoginResponse;
import com.interswitch.exceptions.UserAccountDisabledException;
import com.interswitch.exceptions.UserAlreadyExistsException;
import com.interswitch.exceptions.UserNotFoundException;
import com.interswitch.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "users")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @ApiOperation(value = "User authentication", notes = "Authenticate a user and return a token")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully authenticated"),
            @ApiResponse(code = 401, message = "Unauthorized - invalid credentials"),
            @ApiResponse(code = 404, message = "User not found")
    })
    @PostMapping("/auth")
    public ResponseEntity<LoginResponse> authenticateUser(@RequestBody @Valid LoginRequest request) throws UserNotFoundException, UserAccountDisabledException {
        return ResponseEntity.ok(userService.authenticateUser(request));
    }

    @ApiOperation(value = "Create a new user", notes = "Create a new user account")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "User created successfully"),
            @ApiResponse(code = 400, message = "Bad request - validation failed"),
            @ApiResponse(code = 409, message = "Conflict - user already exists")
    })
    @PostMapping("/users")
    public ResponseEntity<CreateUserResponse> createUser(@RequestBody @Valid @NonNull CreateUserRequest request) throws UserAlreadyExistsException, UserNotFoundException {
        CreateUserResponse response = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
