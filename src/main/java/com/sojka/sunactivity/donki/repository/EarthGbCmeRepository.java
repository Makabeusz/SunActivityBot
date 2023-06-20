package com.sojka.sunactivity.donki.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.SetOptions;
import com.sojka.sunactivity.donki.domain.EarthGbCme;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Slf4j
@Repository
@RequiredArgsConstructor
public class EarthGbCmeRepository {

    private final Firestore firestore;
    private static final String EARTH_GB_CME_COLLECTION = "EarthGbCme";
    private static final DateFormat date = new SimpleDateFormat("yyyy-MM-dd");

    public Optional<EarthGbCme> saveEarthGbCme(EarthGbCme cme) {
        try {
            firestore.collection(EARTH_GB_CME_COLLECTION)
                    .document(cme.getId())
                    .set(cme, SetOptions.merge())
                    .get();
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

    public List<EarthGbCme> getCmes(Date from, Date to) {
        try {
            ApiFuture<QuerySnapshot> future = firestore.collection(EARTH_GB_CME_COLLECTION)
                    .whereGreaterThanOrEqualTo("time.startTime", date.format(from))
                    .whereLessThanOrEqualTo("time.startTime", date.format(to))
                    .get();
            return future.get().getDocuments().stream()
                    .map(d -> d.toObject(EarthGbCme.class))
                    .toList();
        } catch (ExecutionException | InterruptedException e) {
            log.error(e.toString());
            return Collections.emptyList();
        }
    }



}
