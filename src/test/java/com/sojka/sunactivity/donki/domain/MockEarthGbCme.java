package com.sojka.sunactivity.donki.domain;

public final class MockEarthGbCme {

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

    public static EarthGbCme.Impact mars() {
        return EarthGbCme.Impact.builder()
                .arrivalTime("2023-05-04T18:25Z")
                .isGlancingBlow(true)
                .location("Mars")
                .build();
    }

    public static EarthGbCme.Impact lascoC2() {
        return EarthGbCme.Impact.builder()
                .arrivalTime("2023-05-04T19:26Z")
                .isGlancingBlow(false)
                .location("SOHO: LASCO/C2")
                .build();
    }

    public static EarthGbCme.Impact lucy() {
        return EarthGbCme.Impact.builder()
                .arrivalTime("2023-05-05T00:26Z")
                .isGlancingBlow(true)
                .location("Lucy")
                .build();
    }
}