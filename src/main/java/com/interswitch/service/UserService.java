package com.interswitch.service;


import com.interswitch.dto.request.CreateUserRequest;
import com.interswitch.dto.request.LoginRequest;
import com.interswitch.dto.response.CreateUserResponse;
import com.interswitch.dto.response.LoginResponse;
import com.interswitch.exceptions.UserAccountDisabledException;
import com.interswitch.exceptions.UserAlreadyExistsException;
import com.interswitch.exceptions.UserNotFoundException;

public interface UserService {

    CreateUserResponse createUser(CreateUserRequest request) throws UserAlreadyExistsException, UserNotFoundException;
    LoginResponse authenticateUser(LoginRequest request) throws UserNotFoundException, UserAccountDisabledException;
}
