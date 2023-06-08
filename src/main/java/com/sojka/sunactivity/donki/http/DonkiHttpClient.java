package com.sojka.sunactivity.donki.http;

import com.sojka.sunactivity.donki.domain.Cme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

@Component
public class DonkiHttpClient {

    private static final String API_KEY = "DEMO_KEY";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private final WebClient client;

    public DonkiHttpClient(@Value("${nasa.donki.api}") String uri) {
        client = WebClient.builder()
                .baseUrl(uri)
                .build();
    }

    public ResponseEntity<Set<Cme>> getCMEs(Date startDate, Date endDate) {
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/CME")
                        .queryParam("startDate", DATE_FORMAT.format(startDate))
                        .queryParam("endDate", DATE_FORMAT.format(endDate))
                        .queryParam("api_key", API_KEY)
                        .build())
                .retrieve()
                .toEntity(new ParameterizedTypeReference<Set<Cme>>() {
                })
                .block();
    }

}
