package com.sojka.sunactivity.social.repository;

import com.sojka.sunactivity.social.feed.post.SocialMediaPost;

import java.util.Optional;

public interface SocialMediaRepository {
    Optional<SocialMediaPost> savePost(SocialMediaPost post);

    Optional<SocialMediaPost> getPost(String id);
}
