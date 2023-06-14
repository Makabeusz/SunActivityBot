package com.sojka.sunactivity.social.feed;

import com.sojka.sunactivity.donki.domain.EarthGbCme;
import com.sojka.sunactivity.donki.domain.EarthGbCme.Analyze.Score;
import com.sojka.sunactivity.donki.domain.MockEarthGbCme;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PostCreatorUnitTest {

    private static final String TITLE_SUBTITLE_BASE = """
            %s-type coronal mass ejection %s

            NASA sun observatories detected coronal mass ejection started at %s.
            According to the simulations it will deliver glancing blow to the Earth at %s reaching the speed of %d km/s.
            The analyze is %s accurate!""";
    private static final String TITLE_SUBTITLE_WITH_MINIMUM_CME_DATA = String.format(TITLE_SUBTITLE_BASE,
            Score.S, "information", "2023-06-13T17:43Z", "2023-06-14T00:25Z", 350, "most");

    @Test
    void createFacebookPost_minimalCme_correctPost() {
        assertThat(PostCreator.createFacebookPost(MockEarthGbCme.minimal()).toString())
                .isEqualTo(TITLE_SUBTITLE_WITH_MINIMUM_CME_DATA);
    }

    @Test
    void createImpactsHeading_minimalCmeWithMarsOnly_onlyMars() {
        EarthGbCme minimalWithMarsOnly = MockEarthGbCme.minimal();
        minimalWithMarsOnly.setImpacts(List.of(MockEarthGbCme.mars()));

        assertThat(PostCreator.createImpactsHeading(minimalWithMarsOnly).trim())
                .isEqualTo("""
                        Ejected sun particles will reach Mars at 2023-05-04T18:25Z \
                        delivering glancing blow to the planet!""");
    }

    @Test
    void createImpactsHeading_minimalCmeWithMarsAndOtherSatellites_marsFirstAndOther() {
        EarthGbCme minimalWithVariousImpacts = MockEarthGbCme.minimal();
        minimalWithVariousImpacts.setImpacts(List.of(MockEarthGbCme.lucy(), MockEarthGbCme.mars(), MockEarthGbCme.lascoC2()));

        assertThat(PostCreator.createImpactsHeading(minimalWithVariousImpacts).trim())
                .isEqualTo("""
                        Ejected sun particles will reach Mars at 2023-05-04T18:25Z delivering glancing blow to the planet!
                        Other NASA instruments hit by sun particles:
                        - SOHO: LASCO/C2 at 2023-05-04T19:26Z
                        - Lucy at 2023-05-05T00:26Z delivering glancing blow to the instrument!""");
    }

    @Test
    void createImpactsHeading_minimalCmeWithoutMarsAndWithOtherSatellites_marsFirstAndOther() {
        EarthGbCme minimalWithoutMarsOnly = MockEarthGbCme.minimal();
        minimalWithoutMarsOnly.setImpacts(List.of(MockEarthGbCme.lucy(), MockEarthGbCme.lascoC2()));

        assertThat(PostCreator.createImpactsHeading(minimalWithoutMarsOnly).trim())
                .isEqualTo("""
                        Ejected sun particles will reach NASA instruments:
                        - SOHO: LASCO/C2 at 2023-05-04T19:26Z
                        - Lucy at 2023-05-05T00:26Z delivering glancing blow to the instrument!""");
    }

    @Test
    void createAnalyzeHeading_minimalCmeWithAllTheAnalyze_correctHeading() {
        EarthGbCme minimalWithAllTheAnalyze = MockEarthGbCme.minimal();
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
        EarthGbCme minimalWithOneKpAndLatitude = MockEarthGbCme.minimal();
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
        EarthGbCme minimalCmeWithoutKp = MockEarthGbCme.minimal();
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