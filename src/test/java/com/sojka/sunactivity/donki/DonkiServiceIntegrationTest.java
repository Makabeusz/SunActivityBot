package com.sojka.sunactivity.donki;

import com.sojka.sunactivity.donki.domain.EarthGbCme;
import com.sojka.sunactivity.donki.http.DonkiHttpClient;
import com.sojka.sunactivity.donki.repository.EarthGbCmeRepository;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class DonkiServiceIntegrationTest {

    private static MockWebServer server;
    private DonkiService donkiService;

    @BeforeAll
    static void setUp() throws IOException {
        server = new MockWebServer();
        server.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        server.shutdown();
    }

    @BeforeEach
    void initialize() {
        String serverUrl = server.url("").toString();
        DonkiHttpClient http = new DonkiHttpClient(serverUrl, serverUrl);
        donkiService = new DonkiService(http, mock(EarthGbCmeRepository.class));
    }

    @Test
    void getYesterdayEarthGbCmes_singleRichCme_mappedEarthGbCme() {
        server.enqueue(new MockResponse()
                .setBody("[" + MockCme.getRichCmeString() + "]")
                .setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE));

        Set<EarthGbCme> yesterdayEarthGbCmes = donkiService.getAndPersistYesterdayEarthGbCmes();

        assertThat(yesterdayEarthGbCmes)
                .singleElement()
                .has(new Condition<>(s -> s.getId().equals("2023-04-18T23:48:00-CME-001"), "correct ID"));
    }
}