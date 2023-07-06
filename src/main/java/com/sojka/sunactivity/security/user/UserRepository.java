package com.sojka.sunactivity.security.user;

import com.google.cloud.firestore.Firestore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserRepository {

    private static final String USERS_COLLECTION = "Users";

    private final Firestore firestore;

    public Optional<User> findByEmail(String email) {
        try {
            var doc = firestore.collection(USERS_COLLECTION).document(email).get().get();
            return Optional.ofNullable(doc.toObject(User.class));
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public User save(User user) {
        try {
            firestore.collection(USERS_COLLECTION)
                    .document(user.getEmail())
                    .create(user)
                    .get();
            log.info("New user saved: " + user.getEmail());
            return findByEmail(user.getEmail()).orElseThrow();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
