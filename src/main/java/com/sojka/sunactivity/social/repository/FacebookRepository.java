package com.sojka.sunactivity.social.repository;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.sojka.sunactivity.social.feed.post.FacebookPost;
import com.sojka.sunactivity.social.feed.post.SocialMediaPost;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FacebookRepository implements SocialMediaRepository {

    private final Firestore firestore;
    private static final String FACEBOOK_POST_COLLECTION = "FacebookPost";

    @Override
    public Optional<SocialMediaPost> savePost(SocialMediaPost post) {
        try {
            var writeResult = firestore.collection(FACEBOOK_POST_COLLECTION)
                    .document(post.getId())
                    .set(post)
                    .get();
            log.info(post.getId() + " successfully persisted with time: " + writeResult.getUpdateTime());
            return getPost(post.getId());
        } catch (ExecutionException | InterruptedException e) {
            log.error(e.toString());
            return Optional.empty();
        }
    }

    @Override
    public Optional<SocialMediaPost> getPost(String id) {
        try {
            DocumentSnapshot doc = firestore.collection(FACEBOOK_POST_COLLECTION).document(id).get().get();
            return Optional.ofNullable(doc.toObject(FacebookPost.class));
        } catch (ExecutionException | InterruptedException e) {
            return Optional.empty();
        }
    }

}
