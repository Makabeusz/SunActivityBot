package com.sojka.sunactivity.donki.http;

import com.sojka.sunactivity.donki.domain.Cme;
import com.sojka.sunactivity.donki.domain.EarthGbCme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@Component
public class DonkiHttpClient {

    private static final String API_KEY = "DEMO_KEY";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private final WebClient apiClient;
    private final WebClient viewClient;

    public DonkiHttpClient(@Value("${nasa.donki.api}") String apiUri) {
        apiClient = WebClient.builder()
                .baseUrl(apiUri)
                .build();
        viewClient = WebClient.builder().build();
    }

    public ResponseEntity<Set<Cme>> getCMEs(Date startDate, Date endDate) {
        var response = apiClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/CME")
                        .queryParam("startDate", DATE_FORMAT.format(startDate))
                        .queryParam("endDate", DATE_FORMAT.format(endDate))
                        .queryParam("api_key", API_KEY)
                        .build())
                .retrieve();
        try {
            return response.toEntity(new ParameterizedTypeReference<Set<Cme>>() {
                    })
                    .block();
        } catch (WebClientResponseException e) {
            return ResponseEntity.ok(Collections.emptySet());
        }

    }

    public ResponseEntity<String> getHtmlWithAnimations(EarthGbCme cme) {
        return viewClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(cme.getSimulationUrl())
                        .build())
                .retrieve()
                .toEntity(String.class)
                .block();
    }

}
