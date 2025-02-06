package com.interswitch.service.impl;

import com.interswitch.dto.request.CreateUserRequest;
import com.interswitch.dto.request.LoginRequest;
import com.interswitch.dto.response.CreateUserResponse;
import com.interswitch.dto.response.LoginResponse;
import com.interswitch.entity.User;
import com.interswitch.enums.Role;
import com.interswitch.exceptions.UserAlreadyExistsException;
import com.interswitch.exceptions.UserNotFoundException;
import com.interswitch.repository.UserRepository;
import com.interswitch.security.JwtService;
import com.interswitch.security.JwtToken;
import com.interswitch.security.JwtTokenRepository;
import com.interswitch.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final JwtTokenRepository jwtTokenRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public CreateUserResponse createUser(CreateUserRequest request) throws UserAlreadyExistsException {

        Optional<User> user = userRepository.findByEmail(request.getEmail());

        if(user.isPresent()){
            throw new UserAlreadyExistsException("User with email "+request.getEmail() + " already exists.");
        }

        User newUser = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .role(Role.valueOf("USER"))
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(newUser);
        return CreateUserResponse.builder()
                .message("User created successfully")
                .build();
    }

    @Override
    public LoginResponse authenticateUser(LoginRequest request) throws UserNotFoundException {
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow(()->
                new UserNotFoundException("User with email: " +request.getEmail() +" not found"));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        revokeToken(user);
        var jwtToken = jwtService.generateToken(user);
        saveToken(user,jwtToken);

        return LoginResponse.builder()
                .userId(user.getId().toString())
                .token(jwtToken)
                .expiredAt(jwtService.extractExpiration(jwtToken))
                .build();
    }

    private void saveToken(User user, String jwtToken) {
        JwtToken token = JwtToken.builder()
                .token(jwtToken)
                .expired(false)
                .revoked(false)
                .user(user)
                .build();
        jwtTokenRepository.save(token);
    }

    private void revokeToken(User user){
        var validTokenByUser= jwtTokenRepository.findTokenByUserAndExpiredIsFalseAndRevokedIsFalse(user);

        if(validTokenByUser.isEmpty()) return;

        validTokenByUser.forEach(token->{
            token.setRevoked(true);
            token.setExpired(true);
        });

        jwtTokenRepository.saveAll(validTokenByUser);
    }

}
