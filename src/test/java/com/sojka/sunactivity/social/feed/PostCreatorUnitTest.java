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
    void createFacebookPost_minimumCme_correctPost() {
        assertThat(PostCreator.createFacebookPost(MockEarthGbCme.minimal()).toString())
                .isEqualTo(TITLE_SUBTITLE_WITH_MINIMUM_CME_DATA);
    }

    @Test
    void createImpactsHeading_minimumCmeWithMarsOnly_onlyMars() {
        EarthGbCme minimalWithMarsOnly = MockEarthGbCme.minimal();
        minimalWithMarsOnly.setImpacts(List.of(MockEarthGbCme.mars()));

        assertThat(PostCreator.createImpactsHeading(minimalWithMarsOnly).trim())
                .isEqualTo("""
                        Ejected sun particles will reach Mars at 2023-05-04T18:25Z \
                        delivering glancing blow to the planet!""");
    }

    @Test
    void createImpactsHeading_minimumCmeWithMarsAndOtherSatellites_marsFirstAndOther() {
        EarthGbCme minimalWithMarsOnly = MockEarthGbCme.minimal();
        minimalWithMarsOnly.setImpacts(List.of(MockEarthGbCme.lucy(), MockEarthGbCme.mars(), MockEarthGbCme.lascoC2()));

        assertThat(PostCreator.createImpactsHeading(minimalWithMarsOnly).trim())
                .isEqualTo("""
                        Ejected sun particles will reach Mars at 2023-05-04T18:25Z delivering glancing blow to the planet!
                        Other NASA instruments hit by sun particles:
                        - SOHO: LASCO/C2 at 2023-05-04T19:26Z
                        - Lucy at 2023-05-05T00:26Z delivering glancing blow to the instrument!""");
    }

    @Test
    void createImpactsHeading_minimumCmeWithoutMarsAndWithOtherSatellites_marsFirstAndOther() {
        EarthGbCme minimalWithMarsOnly = MockEarthGbCme.minimal();
        minimalWithMarsOnly.setImpacts(List.of(MockEarthGbCme.lucy(), MockEarthGbCme.lascoC2()));

        assertThat(PostCreator.createImpactsHeading(minimalWithMarsOnly).trim())
                .isEqualTo("""
                        Ejected sun particles will reach NASA instruments:
                        - SOHO: LASCO/C2 at 2023-05-04T19:26Z
                        - Lucy at 2023-05-05T00:26Z delivering glancing blow to the instrument!""");
    }

}