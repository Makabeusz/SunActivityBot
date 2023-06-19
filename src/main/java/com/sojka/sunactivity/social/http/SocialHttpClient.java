package com.sojka.sunactivity.social.http;

import com.sojka.sunactivity.social.feed.post.SocialMediaPost;
import org.springframework.http.ResponseEntity;

public interface SocialHttpClient {

    ResponseEntity<String> postToFeed(SocialMediaPost post);
    ResponseEntity<String> schedulePost(SocialMediaPost post, long timestamp);
    SocialHealth ping();
}
