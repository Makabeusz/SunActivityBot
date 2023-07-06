package com.sojka.sunactivity.security;

import com.sojka.sunactivity.security.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.sojka.sunactivity.security.user.Role.ADMIN;
import static com.sojka.sunactivity.security.user.Role.USER;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SunSecurityConfig {

    private final JwtAuthenticationFilter authFilter;
    private final AuthenticationProvider authProvider;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // TODO: think through all SunApp access, roles and everything
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(r -> r
                        .requestMatchers("/auth/**")
                        .permitAll())
                .authorizeHttpRequests(r -> r
                        .requestMatchers("/sun/**")
                        .hasRole(ADMIN.name()))
                .authorizeHttpRequests(r -> r
                        .requestMatchers(HttpMethod.GET)
                        .hasRole(USER.name()))
                .authorizeHttpRequests(r -> r
                        .anyRequest()
                        .hasRole(ADMIN.name()))
                .sessionManagement(s -> s
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authProvider)
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
