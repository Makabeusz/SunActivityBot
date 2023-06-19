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

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FacebookService implements SocialMediaService {

    private final FacebookHttpClient http;
    private final FacebookRepository repository;

    @Override
    public LinkedHashSet<SocialMediaPost> preparePosts(Collection<EarthGbCme> cmes) {
        return cmes.stream()
                .sorted(Comparator.comparing(cme -> ZonedDateTime.parse(
                                cme.getTime().getStartTime()),
                        Comparator.naturalOrder()))
                .map(PostCreator::createFacebookPost)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public Optional<SocialMediaPost> postToFeed(SocialMediaPost post) {
        if (post instanceof FacebookPost toSave) {
            ObjectMapper mapper = new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            try {
                PostId idResponse = mapper.readValue(http.postToFeed(toSave).getBody(), PostId.class);
                toSave.setId(idResponse.getCorrectId());
                return repository.savePost(toSave);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new IllegalArgumentException(post.getClass().toString());
        }
    }

    /**
     * @param id post ID or image ID if posted with image
     * @param postId post ID or null if posted without image
     */
    private record PostId(String id, String postId) {

        String getCorrectId() {
            return postId == null ? id : postId;
        }
    }
}
