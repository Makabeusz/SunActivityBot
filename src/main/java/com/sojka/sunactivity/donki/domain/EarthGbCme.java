package com.sojka.sunactivity.donki.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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

    @NotBlank
    private String id;
    private String catalog;
    @NotNull
    private Time time;
    private String sourceLocation;
    private Integer activeRegion;
    @NotBlank
    private String cmeUrl;
    @NotBlank
    private String simulationUrl;
    @NotBlank
    private String note;
    private List<String> instruments;
    private KpIndexes kpIndex;
    private List<String> linkedEvents;
    private List<Impact> impacts;
    @NotNull
    private Analyze analyze;
    private String animationDensity;
    private String animationVelocity;

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

        @NotBlank
        private String startTime;
        @NotBlank
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
        @Positive
        private Float speed;
        @NotNull
        private Score score;
        private Boolean isMostAccurate;
        @NotBlank
        private String note;
        private Integer levelOfData;
        private String url;

        /**
         * <pre>SCORE CME typification system:
         *   S-type: CMEs with speeds less than 500 km/s
         *   C-type: Common 500-999 km/s
         *   O-type: Occasional 1000-1999 km/s
         *   R-type: Rare 2000-2999 km/s
         *   ER-type: Extremely Rare >3000 km/s</pre>
         */
        public enum Score {
            S("S"),
            C("C"),
            O("O"),
            R("R"),
            ER("ER");

            private final String type;

            Score(String type) {
                this.type = type;
            }

            @Override
            public String toString() {
                return this.type;
            }
        }
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
