package com.sojka.sunactivity.donki;

import com.sojka.sunactivity.donki.domain.Cme;
import com.sojka.sunactivity.donki.dto.EarthGbCme;
import com.sojka.sunactivity.donki.http.DonkiHttpClient;
import com.sojka.sunactivity.donki.mapper.CmeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DonkiService {

    private final DonkiHttpClient http;

    public Set<EarthGbCme> getYesterdayEarthGbCmes() {
        Date yesterday = new Date(Instant.now().minus(1, ChronoUnit.DAYS).toEpochMilli());
        var yesterdayCmes = http.getCMEs(yesterday, yesterday).getBody();
        Objects.requireNonNull(yesterdayCmes);
        return yesterdayCmes.stream()
                .filter(DonkiService::willDeliverEarthGlancingBlow)
                .map(CmeMapper::mapEarthGbCme)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    private static boolean willDeliverEarthGlancingBlow(Cme cme) {
        if (cme.getCmeAnalyses() == null) {
            return false;
        }
        return cme.getCmeAnalyses().stream()
                .map(Cme.CmeAnalyze::getEnlilList)
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .max(Comparator.comparing(sim -> ZonedDateTime.parse(sim.getModelCompletionTime())))
                .orElseThrow()
                .getIsEarthGB();
    }


}
