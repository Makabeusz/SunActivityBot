package com.sojka.sunactivity.social.service;

import com.sojka.sunactivity.donki.domain.MockEarthGbCme;
import com.sojka.sunactivity.social.feed.post.FacebookPost;
import com.sojka.sunactivity.social.feed.post.MockFacebookPost;
import com.sojka.sunactivity.social.feed.post.SocialMediaPost;
import com.sojka.sunactivity.social.http.FacebookHttpClient;
import com.sojka.sunactivity.social.repository.FacebookRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FacebookServiceTest {

    private final FacebookHttpClient http = mock(FacebookHttpClient.class);
    private final FacebookRepository repository = mock(FacebookRepository.class);
    private final FacebookService service = new FacebookService(http, repository);

    @Test
    void preparePosts_richEarthGbCme_correctPost() {
        assertThat(service.preparePosts(List.of(MockEarthGbCme.rich())))
                .singleElement()
                .isEqualTo(MockFacebookPost.rich());
    }

    @Test
    void postToFeed_postWithoutImage_idAssigned() {
        FacebookPost post = MockFacebookPost.firstMinimal();
        when(http.postToFeed(post)).thenReturn("{\"id\":\"Post ID\"}");
        FacebookPost postWithId = MockFacebookPost.firstMinimal();
        postWithId.setId("Post ID");
        when(repository.savePost(post)).thenReturn(Optional.of(postWithId));

        assertThat(service.postToFeed(post)).contains(postWithId);
    }

    @Test
    void postToFeed_postWithImage_postIdAssigned() {
        SocialMediaPost post = MockFacebookPost.firstMinimal();
        when(http.postToFeed(post)).thenReturn("{\"id\":\"Photo ID\",\"post_id\":\"Post ID\"}");
        SocialMediaPost postWithId = MockFacebookPost.firstMinimal();
        postWithId.setId("Post ID");
        when(repository.savePost(post)).thenReturn(Optional.of(postWithId));

        assertThat(service.postToFeed(post)).contains(postWithId);
    }

    @Test
    void postToFeed_otherSocialMediaPost_throwIllegalArgumentException() {
        SocialMediaPost otherPost = new SocialMediaPost() {
            public String getContent() {
                return null;
            }
        };
        assertThatThrownBy(() -> service.postToFeed(otherPost))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("class com.sojka.sunactivity.social.service.FacebookServiceTest$1");
    }

    @Test
    void preparePosts_threeEarthGbCmes_sortedByStartTimeAsc() {
        SocialMediaPost firstPost = MockFacebookPost.firstMinimal();
        SocialMediaPost secondPost = MockFacebookPost.secondMinimal();
        SocialMediaPost thirdPost = MockFacebookPost.thirdMinimal();

        List<SocialMediaPost> posts = service.preparePosts(List.of(MockEarthGbCme.thirdMinimal(),
                MockEarthGbCme.firstMinimal(), MockEarthGbCme.secondMinimal()));

        assertThat(posts)
                .hasSize(3)
                .containsExactly(firstPost, secondPost, thirdPost);
    }
}