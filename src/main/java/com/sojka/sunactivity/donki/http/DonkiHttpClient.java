package com.sojka.sunactivity.donki.http;

import com.sojka.sunactivity.donki.domain.Cme;
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

    private final String viewUrl;
    private final WebClient apiClient;
    private final WebClient viewClient;

    public DonkiHttpClient(@Value("${nasa.donki.api}") String apiUrl,
                           @Value("${nasa.donki.view}") String viewUrl) { // https://webtools.ccmc.gsfc.nasa.gov/DONKI/view
        apiClient = WebClient.builder()
                .baseUrl(apiUrl)
                .build();
        this.viewUrl = viewUrl;
        viewClient = WebClient.builder()
                .baseUrl(viewUrl)
                .build();
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

    public ResponseEntity<String> getViewContent(String url) {
        return viewClient.get()
                .uri(formatDonkiUri(url))
                .retrieve()
                .toEntity(String.class)
                .blockOptional()
                .orElseThrow(() -> new RuntimeException("Cannot access CME simulations HTML content"));
    }

    /**
     * Format given URI to just resource path. Also DONKI api return objects with all the URIs ended with "/-1"
     * which result in 302 code for further api calls. Removing "-" sign solve the problem.
     *
     * @param uri original path with "/-1" ending
     * @return fixed path
     */
    private String formatDonkiUri(String uri) {
        return uri
                .substring(viewUrl.length())
                .replaceAll("/-1$", "/1");
    }

}
