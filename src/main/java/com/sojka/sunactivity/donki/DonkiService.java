package com.sojka.sunactivity.donki;

import com.sojka.sunactivity.donki.domain.mapped.CmeWithSimulation;
import com.sojka.sunactivity.donki.http.DonkiHttpClient;
import com.sojka.sunactivity.donki.http.animation.AnimationQueue;
import com.sojka.sunactivity.donki.http.animation.AnimationSupplier;
import com.sojka.sunactivity.donki.mapper.DonkiMapper;
import com.sojka.sunactivity.donki.repository.CmeWithSimulationRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
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
    private final CmeWithSimulationRepository cmeWithSimulationRepository;
    private final int maxNumberOfAnimationThreads;
    private final int animationThreadsTimeout;

    public DonkiService(DonkiHttpClient http,
                        CmeWithSimulationRepository cmeWithSimulationRepository,
                        @Value("${nasa.donki.http.animation_threads}") int maxThreadsNumber,
                        @Value("${nasa.donki.http.read_timeout}") int timeout) {
        this.http = http;
        this.cmeWithSimulationRepository = cmeWithSimulationRepository;
        this.maxNumberOfAnimationThreads = maxThreadsNumber;
        this.animationThreadsTimeout = (int) (timeout * 0.9);
    }

    /**
     * Fetch and save all CMEs from DONKI in given time scope, but return only ones that will possibly
     * deliver glancing blow to Earth.
     *
     * @return Sorted Set of {@link CmeWithSimulation} that will deliver glancing blast to Earth
     */
    @Valid
    public Set<CmeWithSimulation> fetchEarthGbCmes(LocalDate from, LocalDate to) {
        var domainCmes = http.getCMEs(from, to).getBody();
        Objects.requireNonNull(domainCmes);
        var mapped = domainCmes.stream()
                .map(DonkiMapper::mapCmeWithSimulation)
                .collect(Collectors.toSet());

        var cmes = fetchAnimations(mapped);
        cmeWithSimulationRepository.saveCme(cmes);
        List<CmeWithSimulation> earthGbCmes = cmes.stream()
                .filter(CmeWithSimulation::isEarthGb)
                .toList();

        if (earthGbCmes.isEmpty()) { // TODO: test for cmes without Earth GB, must be empty here
            log.info("No coronal mass ejections that will possibly deliver glancing blow to Earth occurred");
            return Collections.emptySet();
        } else {
            return new LinkedHashSet<>(earthGbCmes);
        }
    }

    public Set<CmeWithSimulation> getAndPersistYesterdayEarthGbCmes() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        return fetchEarthGbCmes(yesterday, yesterday);
    }

    public List<CmeWithSimulation> getSavedEarthGbCme(LocalDate from, LocalDate to) {
        return cmeWithSimulationRepository.getCmes(from, to);
    }

    private boolean supplyAnimations(AnimationQueue queue,
                                     ConcurrentLinkedQueue<CmeWithSimulation> results) {
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

    private List<CmeWithSimulation> fetchAnimations(Collection<CmeWithSimulation> cmes) {
        AnimationQueue queue = new AnimationQueue(cmes);
        ConcurrentLinkedQueue<CmeWithSimulation> withAnimations = new ConcurrentLinkedQueue<>();

        boolean executedWithoutInterruption = supplyAnimations(queue, withAnimations);
        if (!executedWithoutInterruption) {
            log.error(String.format("Returning CMEs without animations due to exceeded timeout of %d seconds with"
                                    + " %d working threads. ",
                    maxNumberOfAnimationThreads, animationThreadsTimeout));
        }
        return new ArrayList<>(withAnimations);
    }

}
