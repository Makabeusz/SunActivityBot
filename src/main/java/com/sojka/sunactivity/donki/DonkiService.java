package com.sojka.sunactivity.donki;

import com.sojka.sunactivity.donki.domain.Cme;
import com.sojka.sunactivity.donki.dto.EarthGbCme;
import com.sojka.sunactivity.donki.http.DonkiHttpClient;
import com.sojka.sunactivity.donki.mapper.CmeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DonkiService {

    private final DonkiHttpClient http;

    public Set<EarthGbCme> getEarthGbCmes(Date from, Date to) {
        var cmes = http.getCMEs(from, to).getBody();
        Objects.requireNonNull(cmes);
        return cmes.stream()
                .filter(Cme::willDeliverEarthGlancingBlow)
                .map(CmeMapper::mapEarthGbCme)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    public Set<EarthGbCme> getYesterdayEarthGbCmes() {
        Date yesterday = new Date(Instant.now().minus(1, ChronoUnit.DAYS).toEpochMilli());
        return getEarthGbCmes(yesterday, yesterday);
    }

}
