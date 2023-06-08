package com.sojka.sunactivity.donki.mapper;

import com.sojka.sunactivity.donki.domain.Cme;
import com.sojka.sunactivity.donki.dto.EarthGbCme;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.time.ZonedDateTime;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

class CmeMapperUnitTest {

    private static EarthGbCme mapped;

    @BeforeAll
    static void beforeAll() {
        try (InputStream input = CmeMapperUnitTest.class.getClassLoader()
                .getResourceAsStream("cme-richresponse.json")) {
            Objects.requireNonNull(input);
            String sampleCmeResponse = new String(input.readAllBytes());
            ObjectMapper mapper = new ObjectMapper();
            Cme cme = mapper.readValue(sampleCmeResponse, Cme.class);
            mapped = CmeMapper.mapEarthGbCme(cme);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void mapEarthGbCme_richResponse_mostRecentAnalyze() {
        assertThat(ZonedDateTime.parse(mapped.getTime().getAnalysisTime()))
                .isEqualTo("2023-04-21T21:29Z");
    }

    @Test
    void mapEarthGbCme_richResponse_mostRecentSimulation() {
        assertThat(ZonedDateTime.parse(mapped.getTime().getSimulationTime()))
                .isEqualTo("2023-04-21T20:31Z");
    }
}