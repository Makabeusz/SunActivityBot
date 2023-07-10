package com.sojka.sunactivity.security.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sojka.sunactivity.security.auth.exception.ExceptionResponse;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SecurityController.class)
@Import(AuthTestConfig.class)
@ActiveProfiles("security")
@WithAnonymousUser
class SecurityControllerTest {

    private static final String REGISTER_URI = "/auth/register";
    private static final String AUTH_URI = "/auth";

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private AuthenticationService authenticationService;

    @Test
    void register_newUser_createdWithTokenResponse() throws Exception {
        RegisterRequest newUserRequest = RegisterRequest.builder()
                .email("newUser@email.com")
                .password("newPassword")
                .firstname("name")
                .lastname("surname")
                .build();

        mvc.perform(post(REGISTER_URI)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newUserRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().string(Matchers.startsWith("{\"token\":\"eyJhbGciOiJIUzI1NiJ9.")));
    }

    @Test
    void register_existingUser_conflictWithEmailAlreadyExistsResponse() throws Exception {
        RegisterRequest request = RegisterRequest.builder()
                .email("user@email.com")
                .password("password")
                .firstname("name")
                .lastname("surname")
                .build();
        ExceptionResponse response = new ExceptionResponse("User user@email.com already exists");

        mvc.perform(post(REGISTER_URI)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(content().json(mapper.writeValueAsString(response)));
    }

    @Test
    void authenticate_validEmailAndPassword_okWithTokenResponse() throws Exception {
        AuthenticationRequest request = AuthenticationRequest.builder()
                .email("user@email.com")
                .password("passwordU")
                .build();

        mvc.perform(post(AUTH_URI)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.startsWith("{\"token\":\"eyJhbGciOiJIUzI1NiJ9")));
    }

    @Test
    void authenticate_invalidEmail_forbiddenWithoutContent() throws Exception {
        AuthenticationRequest request = AuthenticationRequest.builder()
                .email("invalid@email.com")
                .password("passwordU")
                .build();

        mvc.perform(post(AUTH_URI)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(content().string(""));
        // TODO: RFC says 401 https://datatracker.ietf.org/doc/html/rfc9110#name-401-unauthorized
        //       Spring default return 403
    }

}