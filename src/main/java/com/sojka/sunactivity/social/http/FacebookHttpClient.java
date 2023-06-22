package com.sojka.sunactivity.social.http;

import com.sojka.sunactivity.social.feed.post.SocialMediaPost;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

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
    public String postToFeed(SocialMediaPost post) {
        return postToFeedRequest(Map.of("message", post.toString()))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    @Override
    public String schedulePost(SocialMediaPost post, long timestamp) {
        long tenMinutesAhead = Instant.now().plus(10, ChronoUnit.MINUTES).getEpochSecond();
        if (timestamp < tenMinutesAhead) {
            throw new IllegalArgumentException("timestamp = " + timestamp);
        }
        Map<String, Object> fields = Map.of(
                "message", post.toString(),
                "published", false,
                "scheduled_publish_time", timestamp
        );
        return postToFeedRequest(fields)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    private RequestHeadersSpec<? extends RequestHeadersSpec<?>> postToFeedRequest(
            Map<String, Object> bodyParams) {
        Map<String, Object> body = new HashMap<>(
                Map.of("access_token", ACCESS_TOKEN)
        );
        body.putAll(bodyParams);
        return client.post().uri("/" + PAGE_ID + "/feed").bodyValue(body);
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


}
