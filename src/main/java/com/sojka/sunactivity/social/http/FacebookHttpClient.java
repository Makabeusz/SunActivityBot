package com.sojka.sunactivity.social.http;

import com.sojka.sunactivity.social.feed.post.SocialMediaPost;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class FacebookHttpClient implements SocialHttpClient {

    private final RestTemplate rest;
    private final String apiUrl;
    private final String feedUrl;
    private static final String ACCESS_TOKEN = System.getenv("SUN_FACEBOOK_TOKEN");

    public FacebookHttpClient(@Value("${social.facebook.api}") String apiUrl,
                              @Value("${social.facebook.page_id}") String pageId,
                              RestTemplate restTemplate) {
        rest = restTemplate;
        this.apiUrl = apiUrl;
        this.feedUrl = apiUrl + "/" + pageId + "/feed";
    }

    @Override
    public String postToFeed(SocialMediaPost post) {
        HttpEntity<?> request = createRequest(post);
        try {
            return rest.postForObject(feedUrl, request, String.class);
        } catch (RestClientException e) {
            log.error(e.getMessage());
            return "";
        }
    }

    @Override
    public String schedulePost(SocialMediaPost post, long timestamp) {
        Instant now = Instant.now();
        long tenMinutesAhead = now.plus(10, ChronoUnit.MINUTES).getEpochSecond();
        if (timestamp < tenMinutesAhead) {
            LocalTime actual = LocalTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneOffset.UTC);
            throw new IllegalArgumentException("Must schedule the post at least 10 minutes after posting time. "
                                               + "Posting time: " + LocalTime.ofInstant(now, ZoneOffset.UTC)
                                               + " Post time: " + actual);
        }
        Map<String, Object> params = Map.of(
                "published", false,
                "scheduled_publish_time", timestamp
        );
        HttpEntity<?> request = createRequest(post, params);
        return rest.postForObject(feedUrl, request, String.class);
    }

    @Override
    public SocialHealth ping() {
        Map<String, Object> params = Map.of("access_token", ACCESS_TOKEN);

        var uri = UriComponentsBuilder.fromHttpUrl(apiUrl + "/me")
                .queryParam("access_token", "{access_token}")
                .build(params);

        try {
            ResponseEntity<String> response = rest.getForEntity(uri, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                return new SocialHealth(true, response.getBody());
            } else {
                return new SocialHealth(false, response.getBody());
            }
        } catch (RestClientException e) {
            log.error(e.toString());
            return new SocialHealth(false, e.toString());
        }
    }

    private static HttpEntity<?> createRequest(SocialMediaPost post, Map<String, Object> bodyParams) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("message", post.getContent());
        body.put("access_token", ACCESS_TOKEN);
        body.putAll(bodyParams);

        return new HttpEntity<>(body, headers);
    }

    private static HttpEntity<?> createRequest(SocialMediaPost post) {
        return createRequest(post, Collections.emptyMap());
    }

}
