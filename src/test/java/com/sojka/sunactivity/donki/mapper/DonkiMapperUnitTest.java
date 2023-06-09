package com.sojka.sunactivity.donki.mapper;

import com.sojka.sunactivity.donki.MockCme;
import com.sojka.sunactivity.donki.domain.Cme;
import com.sojka.sunactivity.donki.domain.EarthGbCme;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class DonkiMapperUnitTest {

    private static final Cme richCme = MockCme.getRichCme();
    private static final EarthGbCme mapped = DonkiMapper.mapEarthGbCme(richCme);

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