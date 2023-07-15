package com.sojka.sunactivity.donki.mapper;

import com.google.cloud.Timestamp;
import com.sojka.sunactivity.donki.domain.Cme;
import com.sojka.sunactivity.donki.domain.Cme.CmeAnalyze;
import com.sojka.sunactivity.donki.domain.mapped.CmeWithSimulation;
import com.sojka.sunactivity.donki.domain.mapped.CmeWithSimulation.Analyze;
import com.sojka.sunactivity.donki.domain.mapped.CmeWithSimulation.Analyze.Score;
import com.sojka.sunactivity.donki.domain.Event;
import com.sojka.sunactivity.donki.domain.Instrument;
import com.sojka.sunactivity.donki.domain.WsaEnlil;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public final class DonkiMapper {

    private DonkiMapper() {
        throw new AssertionError();
    }

    public static CmeWithSimulation mapCmeWithSimulation(Cme cme) {
        if (!cme.willDeliverEarthGlancingBlow()) {
            throw new DonkiMapperException("Given CME will not deliver glancing blow to Earth");
        }
        WsaEnlil simulation = getMostRecentSimulation(cme);
        CmeAnalyze analyze = getMostRecentCmeAnalyze(cme);

        return CmeWithSimulation.builder()
                .id(cme.getActivityID())
                .catalog(cme.getCatalog())
                .time(CmeWithSimulation.Time.builder()
                        .startTime(parseTimestamp(cme.getStartTime()))
                        .arrivalTime(parseTimestamp(simulation.getEstimatedShockArrivalTime()))
                        .duration(simulation.getEstimatedDuration())
                        .simulationTime(parseTimestamp(simulation.getModelCompletionTime()))
                        .analysisTime(parseTimestamp(analyze.getTime21_5()))
                        .build())
                .sourceLocation(cme.getSourceLocation())
                .activeRegion(cme.getActiveRegionNum())
                .cmeUrl(cme.getLink())
                .simulationUrl(simulation.getLink())
                .note(cme.getNote())
                .instruments(mapInstruments(cme.getInstruments()))
                .kpIndex(CmeWithSimulation.KpIndexes.builder()
                        .kp18(simulation.getKp_18())
                        .kp90(simulation.getKp_90())
                        .kp135(simulation.getKp_135())
                        .kp180(simulation.getKp_180())
                        .build())
                .linkedEvents(mapLinkedEvents(cme))
                .impacts(mapImpacts(simulation))
                .analyze(mapAnalyze(analyze))
                .earthGb(simulation.getIsEarthGB())
                .build();
    }

    private static List<String> mapInstruments(List<Instrument> instruments) {
        if (instruments == null) return Collections.emptyList();
        return instruments.stream()
                .map(Instrument::getName)
                .toList();
    }

    private static Timestamp parseTimestamp(String time) {
        if (time == null) return null;
        return Timestamp.parseTimestamp(time);
    }

    private static List<CmeWithSimulation.Impact> mapImpacts(WsaEnlil simulation) {
        if (simulation.getImpactList() == null) {
            return null;
        }
        return simulation.getImpactList().stream()
                .map(DonkiMapper::mapImpact)
                .toList();
    }

    private static List<String> mapLinkedEvents(Cme cme) {
        if (cme.getLinkedEvents() == null) {
            return null;
        }
        return cme.getLinkedEvents().stream()
                .map(Event::getId)
                .toList();
    }

    private static CmeAnalyze getMostRecentCmeAnalyze(Cme cme) {
        return cme.getCmeAnalyses().stream()
                .max(Comparator.comparing(a -> ZonedDateTime.parse(a.getTime21_5())))
                .orElseThrow(() -> new DonkiMapperException("Missing CME analyze"));
    }

    private static WsaEnlil getMostRecentSimulation(Cme cme) {
        return cme.getCmeAnalyses().stream()
                .map(CmeAnalyze::getEnlilList)
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .max(Comparator.comparing(sim -> ZonedDateTime.parse(sim.getModelCompletionTime())))
                .orElseThrow(() -> new DonkiMapperException("Missing WSA-ENLIL simulation"));
    }

    private static CmeWithSimulation.Impact mapImpact(WsaEnlil.Impact impact) {
        return CmeWithSimulation.Impact.builder()
                .arrivalTime(impact.getArrivalTime())
                .location(impact.getLocation())
                .isGlancingBlow(impact.getIsGlancingBlow())
                .build();
    }

    private static Analyze mapAnalyze(CmeAnalyze analyze) {
        return Analyze.builder()
                .latitude(analyze.getLatitude())
                .longitude(analyze.getLongitude())
                .halfAngle(analyze.getHalfAngle())
                .speed(analyze.getSpeed())
                .score(Score.valueOf(analyze.getType()))
                .isMostAccurate(analyze.getIsMostAccurate())
                .note(analyze.getNote())
                .levelOfData(analyze.getLevelOfData())
                .url(analyze.getLink())
                .build();
    }
}
