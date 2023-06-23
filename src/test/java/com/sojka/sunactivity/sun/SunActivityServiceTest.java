package com.sojka.sunactivity.sun;

import com.sojka.sunactivity.donki.DonkiService;
import com.sojka.sunactivity.social.feed.post.SocialMediaPost;
import com.sojka.sunactivity.social.service.MockedSocialMediaService;
import com.sojka.sunactivity.social.service.SocialMediaService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static com.sojka.sunactivity.donki.domain.MockEarthGbCme.firstMinimal;
import static com.sojka.sunactivity.donki.domain.MockEarthGbCme.secondMinimal;
import static com.sojka.sunactivity.donki.domain.MockEarthGbCme.thirdMinimal;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SunActivityServiceTest {

    private static final DonkiService donki = mock(DonkiService.class);
    private static final SocialMediaService mockService = new MockedSocialMediaService();

    private static SunService sunService;

    @BeforeAll
    static void beforeAll() {
        sunService = new SunService(donki, List.of(mockService));
    }

    @Test
    void getAndPostCmeData_threeCmesAndMockedService_allThreeAssignedNewId() {
        when(donki.getAndPersistYesterdayEarthGbCmes()).thenReturn(Set.of(firstMinimal(),
                secondMinimal(), thirdMinimal()));

        List<SocialMediaPost> postedOrScheduled = sunService.getAndPostCmeData().stream()
                .flatMap(Collection::stream)
                .toList();

        assertThat(postedOrScheduled)
                .hasSize(3)
                .allMatch(post -> post.getId().equals("new ID"));
    }


}