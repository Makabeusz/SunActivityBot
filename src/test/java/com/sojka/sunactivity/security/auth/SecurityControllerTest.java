package com.sojka.sunactivity.security.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SecurityController.class)
@Import(AuthTestConfig.class)
@WithAnonymousUser
class SecurityControllerTest {

    private static final String REGISTER_URI = "/auth/register";
    private static final String LOGIN_URI = "/auth";

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private AuthenticationService authenticationService;

    @Test
    void register_anonymousUser_okWithToken() throws Exception {
        RegisterRequest request = new RegisterRequest(
                "first", "last", "user@email.com", "pass"
        );
        AuthenticationResponse response = new AuthenticationResponse("dummy-token");
        when(authenticationService.register(request)).thenReturn(response);

        mvc.perform(post(REGISTER_URI)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(response)));
    }

    @Test
    void authenticate_anonymousUser_okWithToken() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest("user@email.com", "passU");
        AuthenticationResponse response = new AuthenticationResponse("dummy-token");
        when(authenticationService.authenticate(request)).thenReturn(response);

        mvc.perform(post(LOGIN_URI)
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(response)));
    }

}