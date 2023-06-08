package com.sojka.sunactivity.donki;

import com.sojka.sunactivity.donki.dto.EarthGbCme;
import com.sojka.sunactivity.donki.http.DonkiHttpClient;
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
        DonkiHttpClient http = new DonkiHttpClient(server.url("").toString());
        donkiService = new DonkiService(http);
    }

    @Test
    void getYesterdayEarthGbCmes_singleRichCme_mappedEarthGbCme() {
        server.enqueue(new MockResponse()
                .setBody("[" + MockCme.getRichCmeString() + "]")
                .setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE));

        Set<EarthGbCme> yesterdayEarthGbCmes = donkiService.getYesterdayEarthGbCmes();

        assertThat(yesterdayEarthGbCmes)
                .singleElement()
                .has(new Condition<>(s -> s.getId().equals("2023-04-18T23:48:00-CME-001"), "correct ID"));
    }
}