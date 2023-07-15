package com.sojka.sunactivity.donki.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteBatch;
import com.sojka.sunactivity.donki.domain.mapped.CmeWithSimulation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CmeWithSimulationRepository {

    private final Firestore firestore;
    private static final String CME_COLLECTION = "CmeWithSimulation";

    public Optional<CmeWithSimulation> saveCme(CmeWithSimulation cme) {
        try {
            firestore.collection(CME_COLLECTION)
                    .document(cme.getId())
                    .set(cme)
                    .get();
            return Optional.of(cme);
        } catch (ExecutionException | InterruptedException e) {
            log.error(e.toString());
            return Optional.empty();
        }
    }

    public List<CmeWithSimulation> saveCme(List<CmeWithSimulation> cmes) {
        try {
            WriteBatch batch = firestore.batch();
            for (CmeWithSimulation cme : cmes) {
                var docRef = firestore.collection(CME_COLLECTION)
                        .document(cme.getId());
                batch.set(docRef, cme);
            }
            batch.commit().get();
            return cmes;
        } catch (ExecutionException | InterruptedException e) {
            log.error(e.toString());
            return Collections.emptyList();
        }
    }

    public Optional<CmeWithSimulation> getCme(String id) {
        try {
            DocumentSnapshot doc = firestore
                    .collection(CME_COLLECTION)
                    .document(id)
                    .get().get();
            return Optional.ofNullable(doc.toObject(CmeWithSimulation.class));
        } catch (ExecutionException | InterruptedException e) {
            log.error(e.toString());
            return Optional.empty();
        }
    }

    public List<CmeWithSimulation> getCmes(LocalDate from, LocalDate to) {
        try {
            ApiFuture<QuerySnapshot> future = firestore.collection(CME_COLLECTION)
                    .whereGreaterThanOrEqualTo("time.startTime", toGoogleTimestamp(from))
                    .whereLessThanOrEqualTo("time.startTime", toGoogleTimestamp(to))
                    .get();
            return future.get().getDocuments().stream()
                    .map(d -> d.toObject(CmeWithSimulation.class))
                    .toList();
        } catch (ExecutionException | InterruptedException e) {
            log.error(e.toString());
            return Collections.emptyList();
        }
    }

    private static Timestamp toGoogleTimestamp(LocalDate date) {
        return Timestamp.parseTimestamp(date.toString() + "T00:00Z");
    }

}
