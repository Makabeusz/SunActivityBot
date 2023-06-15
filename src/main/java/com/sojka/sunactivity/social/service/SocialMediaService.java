package com.sojka.sunactivity.social.service;

import com.sojka.sunactivity.donki.domain.EarthGbCme;
import com.sojka.sunactivity.social.feed.post.SocialMediaPost;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Optional;

public interface SocialMediaService {

    LinkedHashSet<SocialMediaPost> preparePosts(Collection<EarthGbCme> cmes);

    Optional<SocialMediaPost> postToFeed(SocialMediaPost post);
}
