package com.sojka.sunactivity.social.service;

import com.sojka.sunactivity.donki.domain.EarthGbCme;
import com.sojka.sunactivity.social.feed.post.SocialMediaPost;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Set;

public interface SocialMediaService {

    LinkedList<SocialMediaPost> preparePosts(Collection<EarthGbCme> cmes);

    Optional<SocialMediaPost> postToFeed(SocialMediaPost post);

    Set<SocialMediaPost> postToFeed(Collection<SocialMediaPost> posts);
}
