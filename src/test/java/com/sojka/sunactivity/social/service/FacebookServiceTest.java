package com.sojka.sunactivity.social.service;

import com.sojka.sunactivity.donki.domain.MockEarthGbCme;
import com.sojka.sunactivity.social.http.FacebookHttpClient;
import com.sojka.sunactivity.social.repository.FacebookRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class FacebookServiceTest {

    private final FacebookHttpClient http = mock(FacebookHttpClient.class);
    private final FacebookRepository repository = mock(FacebookRepository.class);
    private final FacebookService service = new FacebookService(http, repository);

    @Test
    void preparePosts_() {
       assertThat(service.preparePosts(List.of(MockEarthGbCme.rich())))
               .singleElement()
               .isEqualTo("""
                       O-type coronal mass ejection alert

                       NASA sun observatories detected coronal mass ejection started at 2023-04-18T23:48Z \
                       in active region 13283.
                       According to the simulations it will deliver glancing blow to the Earth at 2023-04-23T19:25Z \
                       reaching the speed of 1087 km/s. The CME will be affecting earth up to 2023-04-24T22:31Z.

                       Ejected sun particles will reach Mars at 2023-05-20T06:00Z delivering glancing blow \
                       to the planet!
                       Other NASA instruments hit by sun particles:
                       - STEREO A at 2023-04-23T18:24Z
                       - OSIRIS-REx at 2023-04-24T22:44Z

                       Faint and narrow CME seen to the W in SOHO LASCO C2/C3. Data gap in STEREO-A COR2.

                       Analyze:
                       Bulk measurement following a brighter leading edge seen within the core structure of \
                       the halo CME in later frames mostly to the south and east.
                       Latitude: -14.0
                       Longitude: 14.0
                       Half-angle: 45.0
                       KP index 90°: 6
                       KP index 135°: 7
                       KP index 180°: 8
                       The analyze is not most accurate.""");
    }

    @Test
    void postToFeed() {
    }
}