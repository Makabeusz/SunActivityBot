package com.sojka.sunactivity.social.http;

import com.sojka.sunactivity.social.feed.post.SocialMediaPost;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
@Slf4j
public class FacebookHttpClient implements SocialHttpClient {

    private final WebClient client;
    private final String PAGE_ID;
    private static final String ACCESS_TOKEN = System.getenv("SUN_FACEBOOK_TOKEN");

    public FacebookHttpClient(@Value("${social.facebook.api}") String apiUrl,
                              @Value("${social.facebook.page_id}") String pageId) {
        client = WebClient.builder()
                .baseUrl(apiUrl)
                .build();
        PAGE_ID = pageId;
    }

    @Override
    public ResponseEntity<String> postToFeed(SocialMediaPost post) {
        return schedulePost(post, 0L);
    }

    @Override
    public ResponseEntity<String> schedulePost(SocialMediaPost post, long timestamp) {
        final boolean published = timestamp == 0L;
        return client.post().uri(uriBuilder -> uriBuilder
                        .path("/" + PAGE_ID + "/feed")
                        .queryParam("access_token", ACCESS_TOKEN)
                        .queryParam("url", post.getImage())
                        .queryParam("published", published)
                        .queryParam("scheduled_publish_time", timestamp)
                        .build())
                .bodyValue(new Message(post.toString()))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(String.class)
                .doOnError(e -> {
                    log.error(e.toString());
                    log.debug("Try to ping facebook: " + ping());
                })
                .block();
    }

    @Override
    public SocialHealth ping() {
        try {
            var response = client.get().uri(uriBuilder -> uriBuilder
                            .path("/me")
                            .queryParam("access_token", ACCESS_TOKEN)
                            .build())
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .toEntity(String.class)
                    .block();
            if (response == null) {
                return new SocialHealth(false, "null response");
            }
            return new SocialHealth(true, response.getBody());
        } catch (WebClientResponseException e) {
            return new SocialHealth(false, e.toString());
        }
    }

    private record Message(String message) {
    }
}
