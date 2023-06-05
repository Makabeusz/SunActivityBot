package com.sojka.sunactivity.cme.http;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;
import org.springframework.web.reactive.function.client.WebClient.UriSpec;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;

public class CmeHttpClient {

    private static final WebClient client = WebClient.builder()
            .baseUrl("https://kauai.ccmc.gsfc.nasa.gov/DONKI/WS/get/IPS?startDate=2016-01-01&endDate=2016-01-30")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();

    public static void main(String[] args) {
        String subscribe = client.get()
                .uri("/DONKI/WS/get/IPS")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        System.out.println(subscribe);

//        System.out.println(response);
    }


}
