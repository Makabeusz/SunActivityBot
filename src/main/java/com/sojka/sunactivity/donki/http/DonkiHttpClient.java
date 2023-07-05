package com.sojka.sunactivity.donki.http;

import com.sojka.sunactivity.donki.domain.Cme;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class DonkiHttpClient {

    private static final String API_KEY = System.getenv("SUN_DONKI_KEY");

    private final String cmeUrl;
    private final RestTemplate rest;

    public DonkiHttpClient(@Value("${nasa.donki.api}") String apiUrl,
                           RestTemplate restTemplate) {
        this.cmeUrl = apiUrl + "/CME";
        this.rest = restTemplate;
    }

    public ResponseEntity<Set<Cme>> getCMEs(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> params = new HashMap<>();
        params.put("api_key", API_KEY);
        params.put("startDate", startDate);
        params.put("endDate", endDate);

        var uri = UriComponentsBuilder.fromHttpUrl(cmeUrl)
                .queryParam("api_key", "{api_key}")
                .queryParam("startDate", "{startDate}")
                .queryParam("endDate", "{endDate}")
                .build(params);
        try {
            log.info("Looking for CMEs in DONKI between {} and {}", startDate, endDate);
            Cme[] cmes = rest.getForObject(uri, Cme[].class);
            Objects.requireNonNull(cmes);
            return ResponseEntity.ok(Arrays.stream(cmes).collect(Collectors.toSet()));
        } catch (RestClientException e) {
            log.error(e.getMessage());
            return ResponseEntity.ok(Collections.emptySet());
        }
    }

    public ResponseEntity<String> getViewContent(String url) {
        try {
            log.info("Called DONKI view content: {}", url);
            return rest.getForEntity(formatDonkiUri(url), String.class);
        } catch (RestClientException e) {
            log.error(e.getMessage());
            return ResponseEntity.of(
                            ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(400), e.getMessage()))
                    .build();
        }
    }

    /**
     * Format given URI to just resource path. Also, DONKI api return objects with all the URIs ended with "/-1"
     * which result in 302 code for further api calls. Removing "-" sign solve the problem.
     *
     * @param uri original path with "/-1" ending
     * @return fixed path
     */
    private String formatDonkiUri(String uri) {
        return uri
                .replaceAll("/-1$", "/1");
    }

}
