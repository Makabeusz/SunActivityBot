package com.sojka.sunactivity.security.user;

import com.google.cloud.firestore.Firestore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Repository
@RequiredArgsConstructor
public class UserRepositoryFirestoreImpl implements UserRepository {

    private static final String USERS_COLLECTION = "Users";

    private final Firestore firestore;

    @Override
    public Optional<User> findByEmail(String email) {
        try {
            var doc = firestore.collection(USERS_COLLECTION).document(email).get().get();
            return Optional.ofNullable(doc.toObject(User.class));
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User save(User user) {
        try {
            firestore.collection(USERS_COLLECTION)
                    .document(user.getEmail())
                    .create(user)
                    .get();
            return findByEmail(user.getEmail()).orElseThrow();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
