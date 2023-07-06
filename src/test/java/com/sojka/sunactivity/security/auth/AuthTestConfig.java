package com.sojka.sunactivity.security.auth;

import com.sojka.sunactivity.security.SunSecurityConfig;
import com.sojka.sunactivity.security.auth.exception.SecurityControllerExceptionHandler;
import com.sojka.sunactivity.security.jwt.JwtService;
import com.sojka.sunactivity.security.user.UserRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;

import static org.mockito.Mockito.mock;

@TestConfiguration
@Import({SunSecurityConfig.class, AuthenticationConfig.class,
        SecurityControllerExceptionHandler.class})
public class AuthTestConfig {

    @Bean
    UserDetailsService userDetailsService() {
        return mock(UserDetailsService.class);
    }

    @Bean
    JwtService jwtService() {
        return mock(JwtService.class);
    }

    @Bean
    AuthenticationService authenticationService() {
        return mock(AuthenticationService.class);
    }

    @Bean
    UserRepository userRepository() {
        return mock(UserRepository.class);
    }

}
