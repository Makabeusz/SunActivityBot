package com.sojka.sunactivity.donki.mapper;

import com.sojka.sunactivity.donki.domain.EarthGbCme;
import com.sojka.sunactivity.donki.domain.EarthGbCme.Analyze;
import com.sojka.sunactivity.donki.domain.EarthGbCme.Impact;
import com.sojka.sunactivity.donki.domain.EarthGbCme.KpIndexes;
import com.sojka.sunactivity.donki.domain.EarthGbCme.Time;
import com.sojka.sunactivity.donki.dto.EarthGbCmeDto;
import com.sojka.sunactivity.donki.dto.EarthGbCmeDto.AnalyzeDto;
import com.sojka.sunactivity.donki.dto.EarthGbCmeDto.ImpactDto;
import com.sojka.sunactivity.donki.dto.EarthGbCmeDto.KpIndexesDto;
import com.sojka.sunactivity.donki.dto.EarthGbCmeDto.TimeDto;

public final class DonkiDtoMapper {

    private DonkiDtoMapper() {
        throw new AssertionError();
    }

    public static EarthGbCmeDto toEarthGbCmeDto(EarthGbCme cme) {
        return EarthGbCmeDto.builder()
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
        return EarthGbCmeDto.TimeDto.builder()
                .startTime(time.getStartTime())
                .arrivalTime(time.getArrivalTime())
                .duration(time.getDuration())
                .simulationTime(time.getSimulationTime())
                .analysisTime(time.getAnalysisTime())
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
