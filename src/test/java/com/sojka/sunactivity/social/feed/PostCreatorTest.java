package com.sojka.sunactivity.social.feed;

import com.sojka.sunactivity.donki.domain.EarthGbCme.Analyze.Score;
import com.sojka.sunactivity.donki.domain.MockEarthGbCme;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PostCreatorTest {

    private static final String POST_BASE = """
            %s-type coronal mass ejection %s

            NASA sun observatories detected coronal mass ejection started at %s.
            According to the simulations it will deliver glancing blow to the Earth at %s reaching the speed of %d km/s.
            The analyze is %s accurate!""";

    @Test
    void createFacebookPost_minimumDataCme_correctPost() {
        assertThat(PostCreator.createFacebookPost(MockEarthGbCme.minimal()).toString())
                .isEqualTo(String.format(POST_BASE, Score.S, "information",
                        "2023-06-13T17:43Z", "2023-06-14T00:25Z", 350, "most"));
    }
}