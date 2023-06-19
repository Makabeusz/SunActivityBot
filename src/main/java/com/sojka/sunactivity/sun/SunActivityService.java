package com.sojka.sunactivity.sun;

import com.sojka.sunactivity.donki.DonkiService;
import com.sojka.sunactivity.donki.domain.EarthGbCme;
import com.sojka.sunactivity.social.feed.post.SocialMediaPost;
import com.sojka.sunactivity.social.service.FacebookService;
import com.sojka.sunactivity.social.service.SocialMediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SunActivityService {

    private final DonkiService donki;
    private final List<SocialMediaService> services;

    public Set<EarthGbCme> performDailyCheck() {
        return donki.getAndPersistYesterdayEarthGbCmes();
    }

    public Set<Set<SocialMediaPost>> preparePosts(Collection<EarthGbCme> cmes) {
        Set<Set<SocialMediaPost>> posts = new LinkedHashSet<>();
        for (SocialMediaService service : services) {
            posts.add(service.preparePosts(cmes));
        }
        return posts;
    }

    public List<SocialMediaPost> postFirst(Collection<SocialMediaPost> posts) {
        var rest = new LinkedList<>(posts);
        SocialMediaPost first = rest.removeFirst();
        chooseAService(first).postToFeed(first);
        return rest;
    }

    private SocialMediaService chooseAService(SocialMediaPost post) {
        if (post instanceof FacebookService) {
            return services.stream()
                    .filter(s -> s instanceof FacebookService)
                    .findFirst()
                    .orElseThrow();
        }
        throw new IllegalArgumentException("No implementation of " + post.getClass().getSimpleName());
    }

}
