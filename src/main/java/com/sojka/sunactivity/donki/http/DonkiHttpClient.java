package com.sojka.sunactivity.donki.http;

import com.sojka.sunactivity.donki.dto.CME;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DonkiHttpClient {

    private static final String DONKI_URL = "https://api.nasa.gov/DONKI";
    private static final String API_KEY = "DEMO_KEY";

    private static final WebClient client = WebClient.builder()
            .baseUrl(DONKI_URL)
            .build();
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public static ResponseEntity<Flux<CME>> getCMEs(Date startDate, Date endDate) {
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/CME")
                        .queryParam("startDate", DATE_FORMAT.format(startDate))
                        .queryParam("endDate", DATE_FORMAT.format(endDate))
                        .queryParam("api_key", API_KEY)
                        .build())
                .retrieve()
                .toEntityFlux(CME.class)
                .block();
    }


}
