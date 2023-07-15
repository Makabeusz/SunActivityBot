package com.sojka.sunactivity.sun;

import com.sojka.sunactivity.donki.DonkiService;
import com.sojka.sunactivity.donki.domain.mapped.CmeWithSimulation;
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
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class SunService {

    private final DonkiService donki;
    private final List<SocialMediaService> services;

    public Set<Set<SocialMediaPost>> getAndPostCmeData() {
        log.info("Started main job");
        Set<CmeWithSimulation> cmes = donki.getAndPersistYesterdayEarthGbCmes();
        if (cmes.isEmpty()) return Collections.emptySet();
        log.info("Posting {} events to {} social media platforms", cmes.size(), services.size());
        return postToAllSocialMediaPlatforms(cmes);
    }

    public Set<Set<SocialMediaPost>> postToAllSocialMediaPlatforms(Collection<CmeWithSimulation> cmes) {
        Set<Set<SocialMediaPost>> postedOrScheduled = new LinkedHashSet<>();
        for (SocialMediaService service : services) {
            LinkedList<SocialMediaPost> posts = service.preparePosts(cmes);
            postedOrScheduled.add(service.postToFeed(posts));
        }
        return postedOrScheduled;
    }

}
