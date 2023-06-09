package com.sojka.sunactivity.security.auth;

import com.sojka.sunactivity.security.jwt.JwtService;
import com.sojka.sunactivity.security.user.Role;
import com.sojka.sunactivity.security.user.User;
import com.sojka.sunactivity.security.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        User saved = repository.save(user);
        if (!saved.equals(user)) {
            throw new AuthenticationServiceException("User not saved correctly: " + request.getEmail());
        }
        log.info("New user registered: " + user.getEmail());
        var token = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(token)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword())
        );
        User user = repository.findByEmail(request.getEmail()).orElseThrow(
                () -> new AuthenticationServiceException(
                        String.format("Authenticated correctly, but did not find the email at the same time: %s",
                                request.getEmail()))
        );
        String token = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(token)
                .build();
    }
}
