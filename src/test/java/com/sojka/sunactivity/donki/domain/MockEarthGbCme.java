package com.sojka.sunactivity.donki.domain;

import java.util.List;

public final class MockEarthGbCme {

    //    private static final EarthGbCme.Impact mars = EarthGbCme.Impact.builder()
//            .arrivalTime("2023-05-04T18:25Z")
//            .isGlancingBlow(true)
//            .location("Mars")
//            .build();
//    private static final EarthGbCme.Impact lascoc2 = EarthGbCme.Impact.builder()
//            .arrivalTime("2023-05-04T19:26Z")
//            .isGlancingBlow(false)
//            .location("SOHO: LASCO/C2")
//            .build();
//    public static final EarthGbCme e = EarthGbCme.builder()
//            .impacts(List.of(lascoc2, mars))
//            .build();
    public static EarthGbCme minimal() {
        return EarthGbCme.builder()
                .analyze(EarthGbCme.Analyze.builder()
                        .score(EarthGbCme.Analyze.Score.S)
                        .speed(350.2F)
                        .isMostAccurate(true)
                        .build())
                .time(EarthGbCme.Time.builder()
                        .startTime("2023-06-13T17:43Z")
                        .arrivalTime("2023-06-14T00:25Z")
                        .build())
                .kpIndex(EarthGbCme.KpIndexes.builder().build())
                .build();
    }
}