package com.sojka.sunactivity.social.http;

import com.sojka.sunactivity.social.feed.post.SocialMediaPost;

public interface SocialHttpClient {

    String postToFeed(SocialMediaPost post);
    String schedulePost(SocialMediaPost post, long timestamp);
    SocialHealth ping();
}
