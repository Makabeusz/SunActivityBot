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

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
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

    @Valid
    public Set<EarthGbCme> getEarthGbCmes(LocalDate from, LocalDate to) {
        var cmes = http.getCMEs(from, to).getBody();
        Objects.requireNonNull(cmes);
        return cmes.stream()
                .filter(Cme::willDeliverEarthGlancingBlow)
                .map(DonkiMapper::mapEarthGbCme)
                .map(this::obtainAnimations)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    public Set<EarthGbCme> getAndPersistYesterdayEarthGbCmes() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        Set<EarthGbCme> cmes = getEarthGbCmes(yesterday, yesterday);
        if (cmes.isEmpty()) {
            log.info("No coronal mass ejections that will possibly deliver glancing blow to Earth occurred");
            return Collections.emptySet();
        }
        cmes.forEach(earthGbCmeRepository::saveEarthGbCme);
        return cmes;
    }

    public List<EarthGbCme> getSavedEarthGbCme(LocalDate from, LocalDate to) {
        return earthGbCmeRepository.getCmes(from, to);
    }

    private EarthGbCme obtainAnimations(EarthGbCme cme) {
        String html = http.getViewContent(cme.getSimulationUrl()).getBody();
        List<String> animationUrls = Objects.requireNonNull(html).lines()
                .filter(line -> line.contains("Inner Planets Link = <a href=\""))
                .filter(line -> line.contains("anim.tim-den.gif") || line.contains("anim.tim-vel.gif"))
                .map(line -> line
                        .replaceAll("^.*Inner Planets Link = <a href=\"", "")
                        .replaceAll("\">.*$", ""))
                .toList();
        for (String animation : animationUrls) {
            if (animation.endsWith("-den.gif")) {
                cme.setAnimationDensity(animation);
            } else if (animation.endsWith("-vel.gif")) {
                cme.setAnimationVelocity(animation);
            } else {
                throw new RuntimeException("Unrecognised URL mapped: " + animation);
            }
        }
        return cme;
    }

}
