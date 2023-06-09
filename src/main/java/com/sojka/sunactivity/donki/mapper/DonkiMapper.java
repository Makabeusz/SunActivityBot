package com.sojka.sunactivity.donki.mapper;

import com.sojka.sunactivity.donki.domain.Cme;
import com.sojka.sunactivity.donki.domain.WsaEnlil;
import com.sojka.sunactivity.donki.domain.EarthGbCme;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public final class DonkiMapper {

    private DonkiMapper() {
        throw new AssertionError();
    }

    public static EarthGbCme mapEarthGbCme(Cme cme) {
        if (!cme.willDeliverEarthGlancingBlow()) {
            throw new DonkiMapperException("Given CME will not deliver glancing blow to Earth");
        }
        WsaEnlil simulation = getMostRecentSimulation(cme);
        Cme.CmeAnalyze analyze = getMostRecentCmeAnalyze(cme);

        return EarthGbCme.builder()
                .id(cme.getActivityID())
                .catalog(cme.getCatalog())
                .time(EarthGbCme.Time.builder()
                        .startTime(cme.getStartTime())
                        .arrivalTime(simulation.getEstimatedShockArrivalTime())
                        .duration(simulation.getEstimatedDuration())
                        .simulationTime(simulation.getModelCompletionTime())
                        .analysisTime(analyze.getTime21_5())
                        .build())
                .sourceLocation(cme.getSourceLocation())
                .activeRegion(cme.getActiveRegionNum())
                .cmeUrl(cme.getLink())
                .simulationUrl(simulation.getLink())
                .note(cme.getNote())
                .instruments(cme.getInstruments().stream()
                        .map(Cme.Instrument::getDisplayName)
                        .toList())
                .kpIndex(EarthGbCme.KpIndexes.builder()
                        .kp18(simulation.getKp_18())
                        .kp90(simulation.getKp_90())
                        .kp135(simulation.getKp_135())
                        .kp180(simulation.getKp_180())
                        .build())
                .linkedEvents(mapLinkedEvents(cme))
                .impacts(mapImpacts(simulation))
                .analyze(mapAnalyze(analyze))
                .build();
    }

    private static List<EarthGbCme.Impact> mapImpacts(WsaEnlil simulation) {
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
                .map(Cme.Event::getActivityID)
                .toList();
    }

    private static Cme.CmeAnalyze getMostRecentCmeAnalyze(Cme cme) {
        return cme.getCmeAnalyses().stream()
                .max(Comparator.comparing(a -> ZonedDateTime.parse(a.getTime21_5())))
                .orElseThrow(() -> new DonkiMapperException("Missing CME analyze"));
    }

    private static WsaEnlil getMostRecentSimulation(Cme cme) {
        return cme.getCmeAnalyses().stream()
                .map(Cme.CmeAnalyze::getEnlilList)
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .max(Comparator.comparing(sim -> ZonedDateTime.parse(sim.getModelCompletionTime())))
                .orElseThrow(() -> new DonkiMapperException("Missing WSA-ENLIL simulation"));
    }

    private static EarthGbCme.Impact mapImpact(WsaEnlil.Impact impact) {
        return EarthGbCme.Impact.builder()
                .arrivalTime(impact.getArrivalTime())
                .location(impact.getLocation())
                .isGlancingBlow(impact.getIsGlancingBlow())
                .build();
    }

    private static EarthGbCme.Analyze mapAnalyze(Cme.CmeAnalyze analyze) {
        return EarthGbCme.Analyze.builder()
                .latitude(analyze.getLatitude())
                .longitude(analyze.getLongitude())
                .halfAngle(analyze.getHalfAngle())
                .speed(analyze.getSpeed())
                .type(analyze.getType())
                .isMostAccurate(analyze.getIsMostAccurate())
                .note(analyze.getNote())
                .levelOfData(analyze.getLevelOfData())
                .url(analyze.getLink())
                .build();
    }
}
