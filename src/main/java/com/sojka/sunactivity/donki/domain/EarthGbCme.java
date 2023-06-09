package com.sojka.sunactivity.donki.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Coronal Mass Ejection that make glance blow on earth with embedded inside,
 * the most fresh WSA-ENLIL simulation.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EarthGbCme implements Comparable<EarthGbCme> {

    private String id;
    private String catalog;
    private Time time;
    private String sourceLocation;
    private Integer activeRegion;
    private String cmeUrl;
    private String simulationUrl;
    private String note;
    private List<String> instruments;
    private KpIndexes kpIndex;
    private List<String> linkedEvents;
    private List<Impact> impacts;
    private Analyze analyze;

    @Override
    public int compareTo(EarthGbCme o) {
        return ZonedDateTime.parse(this.time.getStartTime())
                .compareTo(ZonedDateTime.parse(o.time.getStartTime()));
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Time {

        private String startTime;
        private String arrivalTime;
        private Float duration;
        private String simulationTime;
        private String analysisTime;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Analyze {

        private Float latitude;
        private Float longitude;
        private Float halfAngle;
        private Float speed;
        private String type;
        private Boolean isMostAccurate;
        private String note;
        private Integer levelOfData;
        private String url;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class KpIndexes {

        private Integer kp18;
        private Integer kp90;
        private Integer kp135;
        private Integer kp180;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Impact {

        private Boolean isGlancingBlow;
        private String location;
        private String arrivalTime;
    }
}
