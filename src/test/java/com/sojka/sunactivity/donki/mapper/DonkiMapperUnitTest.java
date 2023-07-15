package com.sojka.sunactivity.donki.mapper;

import com.google.cloud.Timestamp;
import com.sojka.sunactivity.donki.MockCme;
import com.sojka.sunactivity.donki.domain.Cme;
import com.sojka.sunactivity.donki.domain.mapped.CmeWithSimulation;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DonkiMapperUnitTest {

    private static final Cme richCme = MockCme.getRichCme();
    private static final CmeWithSimulation mapped = DonkiMapper.mapCmeWithSimulation(richCme);

    @Test
    void mapEarthGbCme_richResponse_mostRecentAnalyze() {
        assertThat(mapped.getTime().getAnalysisTime()).isEqualTo(Timestamp.parseTimestamp("2023-04-21T21:29Z"));
    }

    @Test
    void mapEarthGbCme_richResponse_mostRecentSimulation() {
        assertThat(mapped.getTime().getSimulationTime()).isEqualTo(Timestamp.parseTimestamp("2023-04-21T20:31Z"));
    }
}