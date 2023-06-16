package com.sojka.sunactivity.donki.domain;

import java.util.List;

import static com.sojka.sunactivity.donki.domain.MockEarthGbCme.MockImpacts.marsWithGb;
import static com.sojka.sunactivity.donki.domain.MockEarthGbCme.MockImpacts.orisisRex;
import static com.sojka.sunactivity.donki.domain.MockEarthGbCme.MockImpacts.stereoA;

public final class MockEarthGbCme {

    public static EarthGbCme firstMinimal() {
        return EarthGbCme.builder()
                .analyze(EarthGbCme.Analyze.builder()
                        .score(EarthGbCme.Analyze.Score.O)
                        .speed(1758F)
                        .isMostAccurate(true)
                        .build())
                .time(EarthGbCme.Time.builder()
                        .startTime("2023-04-20T06:12Z")
                        .arrivalTime("2023-04-25T01:28Z")
                        .build())
                .kpIndex(EarthGbCme.KpIndexes.builder().build())
                .build();
    }

    public static EarthGbCme secondMinimal() {
        return EarthGbCme.builder()
                .analyze(EarthGbCme.Analyze.builder()
                        .score(EarthGbCme.Analyze.Score.R)
                        .speed(2543.0F)
                        .isMostAccurate(false)
                        .build())
                .time(EarthGbCme.Time.builder()
                        .startTime("2023-04-20T06:13Z")
                        .arrivalTime("2023-04-25T01:36Z")
                        .build())
                .kpIndex(EarthGbCme.KpIndexes.builder().build())
                .build();
    }


    public static EarthGbCme thirdMinimal() {
        return EarthGbCme.builder()
                .analyze(EarthGbCme.Analyze.builder()
                        .score(EarthGbCme.Analyze.Score.S)
                        .speed(445.8F)
                        .isMostAccurate(true)
                        .build())
                .time(EarthGbCme.Time.builder()
                        .startTime("2023-04-20T07:09Z")
                        .arrivalTime("2023-04-25T02:12Z")
                        .build())
                .kpIndex(EarthGbCme.KpIndexes.builder().build())
                .build();
    }

    public static EarthGbCme rich() {
        return EarthGbCme.builder()
                .id("2023-04-18T23:48:00-CME-001")
                .catalog("M2M_CATALOG")
                .time(EarthGbCme.Time.builder()
                        .startTime("2023-04-18T23:48Z")
                        .arrivalTime("2023-04-23T19:25Z")
                        .simulationTime("2023-04-21T20:31Z")
                        .analysisTime("2023-04-21T21:29Z")
                        .duration(27.1F)
                        .build())
                .sourceLocation("S20E90")
                .activeRegion(13283)
                .cmeUrl("https://webtools.ccmc.gsfc.nasa.gov/DONKI/view/CME/24696/-1")
                .simulationUrl("https://webtools.ccmc.gsfc.nasa.gov/DONKI/view/CMEAnalysis/24722/-1")
                .note("Faint and narrow CME seen to the W in SOHO LASCO C2/C3. Data gap in STEREO-A COR2.")
                .instruments(List.of("SOHO: LASCO/C2", "SOHO: LASCO/C3"))
                .analyze(EarthGbCme.Analyze.builder()
                        .score(EarthGbCme.Analyze.Score.O)
                        .latitude(-14.0F)
                        .longitude(14.0F)
                        .halfAngle(45.0F)
                        .speed(1087.0F)
                        .isMostAccurate(false)
                        .note("Bulk measurement following a brighter leading edge seen within the core structure "
                              + "of the halo CME in later frames mostly to the south and east.")
                        .levelOfData(0)
                        .build())
                .kpIndex(EarthGbCme.KpIndexes.builder()
                        .kp90(6)
                        .kp135(7)
                        .kp180(8)
                        .build())
                .impacts(List.of(orisisRex(), stereoA(), marsWithGb()))
                .build();
    }

    public static class MockImpacts {

        public static EarthGbCme.Impact mars() {
            return EarthGbCme.Impact.builder()
                    .arrivalTime("2023-05-04T18:25Z")
                    .isGlancingBlow(false)
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

        public static EarthGbCme.Impact orisisRex() {
            return EarthGbCme.Impact.builder()
                    .arrivalTime("2023-04-24T22:44Z")
                    .isGlancingBlow(false)
                    .location("OSIRIS-REx")
                    .build();
        }

        public static EarthGbCme.Impact stereoA() {
            return EarthGbCme.Impact.builder()
                    .arrivalTime("2023-04-23T18:24Z")
                    .isGlancingBlow(false)
                    .location("STEREO A")
                    .build();
        }

        public static EarthGbCme.Impact marsWithGb() {
            return EarthGbCme.Impact.builder()
                    .arrivalTime("2023-05-20T06:00Z")
                    .isGlancingBlow(true)
                    .location("Mars")
                    .build();
        }
    }
}