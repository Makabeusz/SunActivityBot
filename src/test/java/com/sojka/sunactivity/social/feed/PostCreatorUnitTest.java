package com.sojka.sunactivity.social.feed;

import com.sojka.sunactivity.donki.domain.EarthGbCme;
import com.sojka.sunactivity.donki.domain.MockEarthGbCme;
import com.sojka.sunactivity.donki.domain.MockEarthGbCme.MockImpacts;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PostCreatorUnitTest {

    @Test
    void createImpactsHeading_minimalCmeWithMarsOnly_onlyMars() {
        EarthGbCme minimalWithMarsOnly = MockEarthGbCme.firstMinimal();
        minimalWithMarsOnly.setImpacts(List.of(MockImpacts.mars()));

        assertThat(PostCreator.createImpactsHeading(minimalWithMarsOnly).trim())
                .isEqualTo("Ejected sun particles will reach Mars at 2023-05-04T18:25Z.");
    }

    @Test
    void createImpactsHeading_minimalCmeWithMarsAndOtherSatellites_marsFirstAndOther() {
        EarthGbCme minimalWithVariousImpacts = MockEarthGbCme.firstMinimal();
        minimalWithVariousImpacts.setImpacts(List.of(MockImpacts.lucy(), MockImpacts.mars(), MockImpacts.lascoC2()));

        assertThat(PostCreator.createImpactsHeading(minimalWithVariousImpacts).trim())
                .isEqualTo("""
                        Ejected sun particles will reach Mars at 2023-05-04T18:25Z.
                        Other NASA instruments hit by sun particles:
                        - SOHO: LASCO/C2 at 2023-05-04T19:26Z
                        - Lucy at 2023-05-05T00:26Z delivering glancing blow to the instrument!""");
    }

    @Test
    void createImpactsHeading_minimalCmeWithoutMarsAndWithOtherSatellites_marsFirstAndOther() {
        EarthGbCme minimalWithoutMarsOnly = MockEarthGbCme.firstMinimal();
        minimalWithoutMarsOnly.setImpacts(List.of(MockImpacts.lucy(), MockImpacts.lascoC2()));

        assertThat(PostCreator.createImpactsHeading(minimalWithoutMarsOnly).trim())
                .isEqualTo("""
                        Ejected sun particles will reach NASA instruments:
                        - SOHO: LASCO/C2 at 2023-05-04T19:26Z
                        - Lucy at 2023-05-05T00:26Z delivering glancing blow to the instrument!""");
    }

    @Test
    void createAnalyzeHeading_minimalCmeWithAllTheAnalyze_correctHeading() {
        EarthGbCme minimalWithAllTheAnalyze = MockEarthGbCme.firstMinimal();
        minimalWithAllTheAnalyze.setAnalyze(EarthGbCme.Analyze.builder()
                .score(EarthGbCme.Analyze.Score.S)
                .speed(350.2F)
                .isMostAccurate(true)
                .latitude(10.0F)
                .longitude(-14.7F)
                .halfAngle(45.0F)
                .build());
        minimalWithAllTheAnalyze.setKpIndex(EarthGbCme.KpIndexes.builder()
                .kp18(3)
                .kp90(4)
                .kp135(5)
                .kp180(6)
                .build());

        assertThat(PostCreator.createAnalyzeHeading(minimalWithAllTheAnalyze).trim())
                .isEqualTo("""
                        Analyze:
                        Latitude: 10.0
                        Longitude: -14.7
                        Half-angle: 45.0
                        KP index 18°: 3
                        KP index 90°: 4
                        KP index 135°: 5
                        KP index 180°: 6""");
    }

    @Test
    void createAnalyzeHeading_minimalCmeWithOneKpAndLatitude_correctHeading() {
        EarthGbCme minimalWithOneKpAndLatitude = MockEarthGbCme.firstMinimal();
        minimalWithOneKpAndLatitude.setAnalyze(EarthGbCme.Analyze.builder()
                .score(EarthGbCme.Analyze.Score.S)
                .speed(350.2F)
                .isMostAccurate(true)
                .latitude(10.0F)
                .build());
        minimalWithOneKpAndLatitude.setKpIndex(EarthGbCme.KpIndexes.builder()
                .kp180(6)
                .build());

        assertThat(PostCreator.createAnalyzeHeading(minimalWithOneKpAndLatitude).trim())
                .isEqualTo("""
                        Analyze:
                        Latitude: 10.0
                        KP index 180°: 6""");
    }

    @Test
    void createAnalyzeHeading_minimalCmeWithoutKp_correctHeading() {
        EarthGbCme minimalCmeWithoutKp = MockEarthGbCme.firstMinimal();
        minimalCmeWithoutKp.setAnalyze(EarthGbCme.Analyze.builder()
                .score(EarthGbCme.Analyze.Score.S)
                .speed(350.2F)
                .isMostAccurate(true)
                .latitude(10.0F)
                .longitude(-14.7F)
                .halfAngle(45.0F)
                .build());

        assertThat(PostCreator.createAnalyzeHeading(minimalCmeWithoutKp).trim())
                .isEqualTo("""
                        Analyze:
                        Latitude: 10.0
                        Longitude: -14.7
                        Half-angle: 45.0""");
    }
}