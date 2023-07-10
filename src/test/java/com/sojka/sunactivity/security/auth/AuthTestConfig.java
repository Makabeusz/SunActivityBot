package com.sojka.sunactivity.security.auth;

import com.google.api.gax.httpjson.HttpJsonStatusCode;
import com.google.api.gax.rpc.AlreadyExistsException;
import com.google.api.gax.rpc.StatusCode;
import com.sojka.sunactivity.security.SunSecurityConfig;
import com.sojka.sunactivity.security.auth.exception.SecurityControllerExceptionHandler;
import com.sojka.sunactivity.security.jwt.JwtService;
import com.sojka.sunactivity.security.user.Role;
import com.sojka.sunactivity.security.user.User;
import com.sojka.sunactivity.security.user.UserRepository;
import com.sojka.sunactivity.security.user.UserService;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@TestConfiguration
@Import({SunSecurityConfig.class, AuthenticationConfig.class,
        SecurityControllerExceptionHandler.class})
public class AuthTestConfig {

    private static final Map<String, User> dummyUsers = new HashMap<>(Map.of(
            "user@email.com", User.builder()
                    .email("user@email.com")
                    .password("$2a$12$BW5as1iQ1hHV4l2jkAoiB.yUht/D4jNqYMEb5oGLO6b0ms/Q6m1Ve") // passwordU
                    .firstname("firstU")
                    .lastname("lastU")
                    .role(Role.USER)
                    .build(),
            "admin@email.com", User.builder()
                    .email("admin@email.com")
                    .password("$2a$12$ToZg3cWayBS8.jS5caVMBuiCeJc.Vjm6N8oiqK90ui7s6T.ZeQEu2") //passwordA
                    .firstname("firstA")
                    .lastname("secondA")
                    .role(Role.ADMIN)
                    .build()
    ));

    @Bean
    AuthenticationService authenticationService(PasswordEncoder passwordEncoder,
                                                JwtService jwtService,
                                                AuthenticationManager authenticationManager) {
        return new AuthenticationService(
                userRepositoryDummy(), passwordEncoder, jwtService, authenticationManager
        );
    }

    @Bean
    UserRepository userRepositoryDummy() {
        return new UserRepository() {
            @Override
            public Optional<User> findByEmail(String email) {
                return Optional.ofNullable(dummyUsers.get(email));
            }

            @Override
            public User save(User user) {
                String email = user.getEmail();
                if (dummyUsers.containsKey(email)) {
                    throw new RuntimeException(new ExecutionException(
                            new AlreadyExistsException(new StatusRuntimeException(
                                    Status.ALREADY_EXISTS.withDescription("/users/firestore/location/".concat(email))
                            ),
                                    HttpJsonStatusCode.of(StatusCode.Code.ALREADY_EXISTS), false))
                    );
                }
                dummyUsers.put(email, user);
                return user;
            }
        };
    }

    @Bean
    JwtService jwtService(@Value("${sun.security.secret}") String dummySecret) {
        return new JwtService(dummySecret);
    }

    @Bean
    UserDetailsService userDetailsService(UserRepository repo) {
        return new UserService(repo);
    }

}
