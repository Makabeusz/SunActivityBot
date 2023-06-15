package com.sojka.sunactivity.donki.repository;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.SetOptions;
import com.sojka.sunactivity.donki.domain.EarthGbCme;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Slf4j
@Repository
@RequiredArgsConstructor
public class EarthGbCmeRepository {

    private final Firestore firestore;
    private static final String EARTH_GB_CME_COLLECTION = "EarthGbCme";

    public Optional<EarthGbCme> saveEarthGbCme(EarthGbCme cme) {
        try {
            var writeResult = firestore.collection(EARTH_GB_CME_COLLECTION)
                    .document(cme.getId())
                    .set(cme, SetOptions.merge())
                    .get();
            log.info(cme.getId() + " successfully persisted with time: " + writeResult.getUpdateTime());
            return getCme(cme.getId());
        } catch (ExecutionException | InterruptedException e) {
            log.error(e.toString());
            return Optional.empty();
        }
    }

    public Optional<EarthGbCme> getCme(String id) {
        try {
            DocumentSnapshot doc = firestore.collection(EARTH_GB_CME_COLLECTION).document(id).get().get();
            return Optional.ofNullable(doc.toObject(EarthGbCme.class));
        } catch (ExecutionException | InterruptedException e) {
            return Optional.empty();
        }
    }


}
