package com.sojka.sunactivity.donki;

import com.sojka.sunactivity.donki.domain.Cme;
import com.sojka.sunactivity.donki.domain.EarthGbCme;
import com.sojka.sunactivity.donki.http.DonkiHttpClient;
import com.sojka.sunactivity.donki.mapper.DonkiMapper;
import com.sojka.sunactivity.donki.repository.EarthGbCmeRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class DonkiService {

    private final DonkiHttpClient http;
    private final EarthGbCmeRepository earthGbCmeRepository;

    public Set<EarthGbCme> getEarthGbCmes(Date from, Date to) {
        var cmes = http.getCMEs(from, to).getBody();
        Objects.requireNonNull(cmes);
        return cmes.stream()
                .filter(Cme::willDeliverEarthGlancingBlow)
                .map(DonkiMapper::mapEarthGbCme)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    public Set<EarthGbCme> getAndPersistYesterdayEarthGbCmes() {
        Date yesterday = new Date(Instant.now().minus(1, ChronoUnit.DAYS).toEpochMilli());
        @Valid Set<EarthGbCme> cmes = Objects.requireNonNull(http
                        .getCMEs(yesterday, yesterday)
                        .getBody()).stream()
                .filter(Cme::willDeliverEarthGlancingBlow)
                .map(DonkiMapper::mapEarthGbCme)
                .collect(Collectors.toSet());
        if (cmes.isEmpty()) {
            log.info("No coronal mass ejections that will possibly deliver glancing blow to Earth occur yesterday");
            return Collections.emptySet();
        }
        cmes.forEach(earthGbCmeRepository::saveEarthGbCme);
        return cmes;
    }

}
