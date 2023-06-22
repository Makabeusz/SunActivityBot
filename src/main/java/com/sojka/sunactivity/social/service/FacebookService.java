package com.sojka.sunactivity.social.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sojka.sunactivity.donki.domain.EarthGbCme;
import com.sojka.sunactivity.social.feed.PostCreator;
import com.sojka.sunactivity.social.feed.post.FacebookPost;
import com.sojka.sunactivity.social.feed.post.SocialMediaPost;
import com.sojka.sunactivity.social.http.FacebookHttpClient;
import com.sojka.sunactivity.social.repository.FacebookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FacebookService implements SocialMediaService {

    private final FacebookHttpClient http;
    private final FacebookRepository repository;
    private final ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Override
    public LinkedList<SocialMediaPost> preparePosts(Collection<EarthGbCme> cmes) {
        return cmes.stream()
                .sorted()
                .map(PostCreator::createFacebookPost)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public Optional<SocialMediaPost> postToFeed(SocialMediaPost post) {
        FacebookPost toSave = checkIfFacebookPost(post);
        try {
            String id = mapper.readValue(http.postToFeed(toSave), PostId.class).getCorrectId();
            toSave.setId(id);
            var saved = repository.savePost(toSave);
            log.info("Posted on Facebook with ID = " + id);
            return saved;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Set<SocialMediaPost> postToFeed(Collection<SocialMediaPost> posts) {
        if (posts == null || posts.size() < 1) throw new IllegalArgumentException();
        var listed = new LinkedList<>(posts);
        Optional<SocialMediaPost> first = postToFeed(listed.removeFirst());
        var savedPosts = new LinkedHashSet<>(List.of(first.orElseThrow()));
        log.info("Posted on Facebook with ID = " + first.orElseThrow().getId());

        Calendar plusOneHour = new GregorianCalendar();
        for (SocialMediaPost post : listed) {
            FacebookPost toSave = checkIfFacebookPost(post);
            plusOneHour.add(Calendar.HOUR_OF_DAY, 1);
            try {
                String id = mapper.readValue(http.schedulePost(toSave, plusOneHour.getTime().getTime()),
                        PostId.class).getCorrectId();
                toSave.setId(id);
                savedPosts.add(repository.savePost(toSave).orElseThrow());
                log.info("Posted on Facebook with ID = " + id);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

        }
        return savedPosts;
    }

    private static FacebookPost checkIfFacebookPost(SocialMediaPost post) {
        if (post instanceof FacebookPost facebookPost) {
            return facebookPost;
        } else {
            throw new IllegalArgumentException(post.getClass().toString());
        }
    }

    /**
     * @param id     post ID or image ID if posted with image
     * @param postId post ID or null if posted without image
     */
    private record PostId(String id, String postId) {

        String getCorrectId() {
            return postId == null ? id : postId;
        }
    }
}
