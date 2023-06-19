package com.sojka.sunactivity.sun;

import com.sojka.sunactivity.donki.DonkiService;
import com.sojka.sunactivity.donki.domain.EarthGbCme;
import com.sojka.sunactivity.social.feed.post.SocialMediaPost;
import com.sojka.sunactivity.social.service.SocialMediaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class SunActivityService {

    private final DonkiService donki;
    private final List<SocialMediaService> services;

    public Set<Set<SocialMediaPost>> getAndPostCmeData() {
        log.info("Started main job");
        Set<EarthGbCme> cmes = donki.getAndPersistYesterdayEarthGbCmes();
        if (cmes.isEmpty()) return Collections.emptySet();
        log.info("Posting {} events to {} social media platforms", cmes.size(), services.size());
        return post(cmes);
    }

    public Optional<SocialMediaPost> post(SocialMediaPost post) {
        return chooseAService(post).postToFeed(post);
    }

    public Set<Set<SocialMediaPost>> post(Collection<EarthGbCme> cmes) {
        Set<Set<SocialMediaPost>> postedOrScheduled = new LinkedHashSet<>();
        for (LinkedList<SocialMediaPost> oneServicePosts : preparePosts(cmes)) {
            var posted = chooseAService(oneServicePosts.peek()).postToFeed(oneServicePosts);
            postedOrScheduled.add(posted);
        }
        return postedOrScheduled;
    }

    private Set<LinkedList<SocialMediaPost>> preparePosts(Collection<EarthGbCme> cmes) {
        Set<LinkedList<SocialMediaPost>> posts = new LinkedHashSet<>();
        for (SocialMediaService service : services) {
            posts.add(service.preparePosts(cmes));
        }
        return posts;
    }

    private SocialMediaService chooseAService(SocialMediaPost post) {
        String postName = getNameWithoutLastSequence(post, "Post");
        for (SocialMediaService service : services) {
            String serviceName = getNameWithoutLastSequence(service, "Service");
            if (postName.equalsIgnoreCase(serviceName)) return service;
        }
        throw new IllegalArgumentException("No implementation of " + post.getClass().getSimpleName());
    }

    private static String getNameWithoutLastSequence(Object object, String sequence) {
        String simpleName = object.getClass().getSimpleName();
        return simpleName.substring(0, simpleName.lastIndexOf(sequence));
    }

}
