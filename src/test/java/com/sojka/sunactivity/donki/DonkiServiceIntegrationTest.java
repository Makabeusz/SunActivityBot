package com.sojka.sunactivity.donki;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.Timestamp;
import com.sojka.sunactivity.donki.domain.Cme;
import com.sojka.sunactivity.donki.domain.EarthGbCme;
import com.sojka.sunactivity.donki.http.DonkiHttpClient;
import com.sojka.sunactivity.donki.repository.EarthGbCmeRepository;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class DonkiServiceIntegrationTest {

    private static final String DONKI_KEY = System.getenv("SUN_DONKI_KEY");

    private static MockWebServer server;
    private final RestTemplate mockedRest = mock(RestTemplate.class);
    private DonkiService donkiService;
    @Autowired
    private ObjectMapper mapper;

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
        DonkiHttpClient http = new DonkiHttpClient(serverUrl, mockedRest);
        donkiService = new DonkiService(http, mock(EarthGbCmeRepository.class), 5, 3);
    }

    @Test
    void getYesterdayEarthGbCmes_singleRichCme_mappedEarthGbCme() {
        server.enqueue(new MockResponse()
                .setBody("[" + MockCme.getRichCmeString() + "]")
                .setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE));
        server.enqueue(new MockResponse()
                .setBody(MockCme.getHtmlWithAnimations())
                .setHeader("Content-Type", "text/html;charset=ISO-8859-1"));
        EarthGbCme.Time correctTimes = EarthGbCme.Time.builder()
                .startTime(Timestamp.parseTimestamp("2023-04-18T23:48Z"))
                .arrivalTime(Timestamp.parseTimestamp("2023-04-23T19:25Z"))
                .duration(27.1F)
                .simulationTime(Timestamp.parseTimestamp("2023-04-21T20:31Z"))
                .analysisTime(Timestamp.parseTimestamp("2023-04-21T21:29Z"))
                .build();
        URI getCmesFullUri = donkiCmeUri();
        when(mockedRest.getForObject(getCmesFullUri, Cme[].class)).thenReturn(new Cme[]{MockCme.getRichCme()});
        when(mockedRest.getForEntity("https://webtools.ccmc.gsfc.nasa.gov/DONKI/view/WSA-ENLIL/24725/1",
                String.class)).thenReturn(ResponseEntity.ok(MockCme.getHtmlWithAnimations()));

        Set<EarthGbCme> yesterdayEarthGbCmes = donkiService.getAndPersistYesterdayEarthGbCmes();

        assertThat(yesterdayEarthGbCmes)
                .singleElement()
                .hasFieldOrPropertyWithValue("id", "2023-04-18T23:48:00-CME-001")
                .hasFieldOrPropertyWithValue("time", correctTimes);
    }

    @Test
    void getAndPersistYesterdayEarthGbCmes_nineCmes_allMappedWithAnimations() throws JsonProcessingException {
        server.enqueue(new MockResponse()
                .setBody(mapper.writeValueAsString(MockCme.getNineCmes()))
                .setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE));
        for (int i = 0; i < 9; i++) {
            server.enqueue(new MockResponse()
                    .setBody(MockCme.getHtmlWithAnimations())
                    .setHeader("Content-Type", "text/html;charset=ISO-8859-1"));
        }
        URI getCmesFullUri = donkiCmeUri();
        when(mockedRest.getForObject(getCmesFullUri, Cme[].class))
                .thenReturn(MockCme.getNineCmes().toArray(Cme[]::new));
        when(mockedRest.getForEntity("https://webtools.ccmc.gsfc.nasa.gov/DONKI/view/WSA-ENLIL/24725/1",
                String.class)).thenReturn(ResponseEntity.ok(MockCme.getHtmlWithAnimations()));

        Set<EarthGbCme> yesterdayEarthGbCmes = donkiService.getAndPersistYesterdayEarthGbCmes();

        assertThat(yesterdayEarthGbCmes)
                .hasSize(9)
                .allMatch(cme -> cme.getAnimationDensity()
                                .equals("http://iswa.gsfc.nasa.gov/downloads/20230601_024400_2.0_anim.tim-den.gif"),
                        "dummy density GIF")
                .allMatch(cme -> cme.getAnimationVelocity()
                                .equals("http://iswa.gsfc.nasa.gov/downloads/20230601_024400_2.0_anim.tim-vel.gif"),
                        "dummy velocity GIF");
    }

    private static URI donkiCmeUri() {
        LocalDate yesterday = LocalDate.now().minus(1, ChronoUnit.DAYS);
        return URI.create("http://" + server.getHostName() + ":" + server.getPort() + "/CME?api_key="
                          + DONKI_KEY + "&startDate=" + yesterday + "&endDate=" + yesterday);
    }
}