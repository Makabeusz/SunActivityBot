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
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.toString());
            throw new RuntimeException(e);
        }
    }

}
