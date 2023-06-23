package com.sojka.sunactivity.social.service;

import com.sojka.sunactivity.donki.domain.EarthGbCme;
import com.sojka.sunactivity.social.feed.post.SocialMediaPost;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class MockedSocialMediaService implements SocialMediaService {

    @Override
    public LinkedList<SocialMediaPost> preparePosts(Collection<EarthGbCme> cmes) {
        return cmes.stream()
                .map(cme -> new MockedSocialMediaPost(cme.getAnimationDensity(),
                        cme.getAnalyze().getScore() + "- score notification",
                        "Speed " + cme.getAnalyze().getSpeed()))
                .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public Optional<SocialMediaPost> postToFeed(SocialMediaPost post) {
        MockedSocialMediaPost withId = (MockedSocialMediaPost) post;
        withId.setId("new ID");
        return Optional.of(withId);
    }

    @Override
    public Set<SocialMediaPost> postToFeed(Collection<SocialMediaPost> posts) {
        return posts.stream()
                .map(this::postToFeed)
                .map(Optional::orElseThrow)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }


    public static class MockedSocialMediaPost extends SocialMediaPost {

        private final String title;
        private final String body;

        public MockedSocialMediaPost(String image, String title, String body) {
            super(image);
            this.title = title;
            this.body = body;
        }

        @Override
        public String getContent() {
            return title + "\n\n" + body;
        }

        @Override
        public String toString() {
            return "MockedSocialMediaPost{" +
                   "title='" + title.substring(0, 10) + '\'' +
                   ", body='" + body.substring(0, 10) + '\'' +
                   '}' + " (truncated data)";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;
            MockedSocialMediaPost that = (MockedSocialMediaPost) o;
            return Objects.equals(title, that.title) && Objects.equals(body, that.body)
                    && super.equals(o);
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), title, body);
        }
    }

}
