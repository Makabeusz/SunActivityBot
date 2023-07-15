package com.sojka.sunactivity.donki.mapper;

import com.sojka.sunactivity.donki.domain.mapped.CmeWithSimulation;
import com.sojka.sunactivity.donki.domain.mapped.CmeWithSimulation.Analyze;
import com.sojka.sunactivity.donki.domain.mapped.CmeWithSimulation.Impact;
import com.sojka.sunactivity.donki.domain.mapped.CmeWithSimulation.KpIndexes;
import com.sojka.sunactivity.donki.domain.mapped.CmeWithSimulation.Time;
import com.sojka.sunactivity.donki.dto.CmeWithSimulationDto;
import com.sojka.sunactivity.donki.dto.CmeWithSimulationDto.AnalyzeDto;
import com.sojka.sunactivity.donki.dto.CmeWithSimulationDto.ImpactDto;
import com.sojka.sunactivity.donki.dto.CmeWithSimulationDto.KpIndexesDto;
import com.sojka.sunactivity.donki.dto.CmeWithSimulationDto.TimeDto;

import java.time.ZonedDateTime;

public final class DonkiDtoMapper {

    private DonkiDtoMapper() {
        throw new AssertionError();
    }

    public static CmeWithSimulationDto toEarthGbCmeDto(CmeWithSimulation cme) {
        return CmeWithSimulationDto.builder()
                .id(cme.getId())
                .catalog(cme.getCatalog())
                .time(mapTime(cme.getTime()))
                .sourceLocation(cme.getSourceLocation())
                .activeRegion(cme.getActiveRegion())
                .cmeUrl(cme.getCmeUrl())
                .simulationUrl(cme.getSimulationUrl())
                .note(cme.getNote())
                .instruments(cme.getInstruments())
                .kpIndex(mapKpIndexes(cme.getKpIndex()))
                .linkedEvents(cme.getLinkedEvents())
                .impacts(cme.getImpacts().stream()
                        .map(DonkiDtoMapper::mapImpact)
                        .toList())
                .analyze(mapAnalyze(cme.getAnalyze()))
                .animationDensity(cme.getAnimationDensity())
                .animationVelocity(cme.getAnimationVelocity())
                .earthGb(cme.isEarthGb())
                .build();

    }
    private static KpIndexesDto mapKpIndexes(KpIndexes kp) {
        return KpIndexesDto.builder()
                .kp18(kp.getKp18())
                .kp90(kp.getKp90())
                .kp135(kp.getKp135())
                .kp180(kp.getKp180())
                .build();
    }

    private static TimeDto mapTime(Time time) {
        return CmeWithSimulationDto.TimeDto.builder()
                .startTime(ZonedDateTime.parse(time.getStartTime().toString()))
                .arrivalTime(ZonedDateTime.parse(time.getArrivalTime().toString()))
                .duration(time.getDuration())
                .simulationTime(ZonedDateTime.parse(time.getSimulationTime().toString()))
                .analysisTime(ZonedDateTime.parse(time.getAnalysisTime().toString()))
                .build();
    }

    private static ImpactDto mapImpact(Impact impact) {
        return ImpactDto.builder()
                .isGlancingBlow(impact.getIsGlancingBlow())
                .location(impact.getLocation())
                .arrivalTime(impact.getArrivalTime())
                .build();
    }

    private static AnalyzeDto mapAnalyze(Analyze analyze) {
        return AnalyzeDto.builder()
                .latitude(analyze.getLatitude())
                .longitude(analyze.getLongitude())
                .halfAngle(analyze.getHalfAngle())
                .speed(analyze.getSpeed())
                .score(AnalyzeDto.ScoreDto.valueOf(analyze.getScore().toString()))
                .isMostAccurate(analyze.getIsMostAccurate())
                .note(analyze.getNote())
                .levelOfData(analyze.getLevelOfData())
                .url(analyze.getUrl())
                .build();
    }
}
