package com.sojka.sunactivity.donki;

import com.sojka.sunactivity.donki.domain.Cme;
import com.sojka.sunactivity.donki.domain.EarthGbCme;
import com.sojka.sunactivity.donki.domain.animation.AnimationQueue;
import com.sojka.sunactivity.donki.domain.animation.AnimationSupplier;
import com.sojka.sunactivity.donki.http.DonkiHttpClient;
import com.sojka.sunactivity.donki.mapper.DonkiMapper;
import com.sojka.sunactivity.donki.repository.EarthGbCmeRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DonkiService {

    private final DonkiHttpClient http;
    private final EarthGbCmeRepository earthGbCmeRepository;
    private final int maxNumberOfAnimationThreads;
    private final int animationThreadsTimeout;

    public DonkiService(DonkiHttpClient http,
                        EarthGbCmeRepository earthGbCmeRepository,
                        @Value("${nasa.donki.http.animation_threads}") int maxThreadsNumber,
                        @Value("${nasa.donki.http.read_timeout}") int timeout) {
        this.http = http;
        this.earthGbCmeRepository = earthGbCmeRepository;
        this.maxNumberOfAnimationThreads = maxThreadsNumber;
        this.animationThreadsTimeout = (int) (timeout * 0.9);
    }

    @Valid
    public Set<EarthGbCme> getEarthGbCmes(LocalDate from, LocalDate to) {
        var cmes = http.getCMEs(from, to).getBody();
        Objects.requireNonNull(cmes);
        Set<EarthGbCme> earthGbCmes = cmes.stream()
                .filter(Cme::willDeliverEarthGlancingBlow)
                .map(DonkiMapper::mapEarthGbCme)
                .collect(Collectors.toSet());

        if (earthGbCmes.isEmpty()) {
            log.info("No coronal mass ejections that will possibly deliver glancing blow to Earth occurred");
            return Collections.emptySet();
        }

        AnimationQueue queue = new AnimationQueue(earthGbCmes);
        ConcurrentLinkedQueue<EarthGbCme> withAnimations = new ConcurrentLinkedQueue<>();

        boolean executedWithoutInterruption = supplyAnimations(queue, withAnimations);
        if (executedWithoutInterruption) {
            return new LinkedHashSet<>(withAnimations);
        } else {
            log.error(String.format("Animations could not be loaded in timeout of %d seconds with %d threads",
                    maxNumberOfAnimationThreads, animationThreadsTimeout));
            return new LinkedHashSet<>(earthGbCmes);
        }
    }

    private boolean supplyAnimations(AnimationQueue queue,
                                     ConcurrentLinkedQueue<EarthGbCme> results) {
        ExecutorService executor = Executors.newCachedThreadPool();
        int threadsNumber = Math.min(maxNumberOfAnimationThreads, queue.initialSize());

        for (int i = 0; i < threadsNumber; i++) {
            executor.execute(new AnimationSupplier(queue, http, results));
        }
        executor.shutdown();
        try {
            return executor.awaitTermination(animationThreadsTimeout, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public Set<EarthGbCme> getAndPersistYesterdayEarthGbCmes() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        Set<EarthGbCme> cmes = getEarthGbCmes(yesterday, yesterday);
        cmes.forEach(earthGbCmeRepository::saveEarthGbCme);
        return cmes;
    }

    public List<EarthGbCme> getSavedEarthGbCme(LocalDate from, LocalDate to) {
        return earthGbCmeRepository.getCmes(from, to);
    }

}
