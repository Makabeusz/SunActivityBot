package com.sojka.sunactivity.donki.dto;

import com.sojka.sunactivity.donki.domain.mapped.CmeWithSimulation;
import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * @see CmeWithSimulation
 */
@Data
@Builder
public class CmeWithSimulationDto {

    private String id;
    private String catalog;
    private TimeDto time;
    private String sourceLocation;
    private Integer activeRegion;
    private String cmeUrl;
    private String simulationUrl;
    private String note;
    private List<String> instruments;
    private KpIndexesDto kpIndex;
    private List<String> linkedEvents;
    private List<ImpactDto> impacts;
    private AnalyzeDto analyze;
    private String animationDensity;
    private String animationVelocity;
    private boolean earthGb;

    @Data
    @Builder
    public static class TimeDto {

        private ZonedDateTime startTime;
        private ZonedDateTime arrivalTime;
        private Float duration;
        private ZonedDateTime simulationTime;
        private ZonedDateTime analysisTime;
    }

    @Data
    @Builder
    public static class AnalyzeDto {

        private Float latitude;
        private Float longitude;
        private Float halfAngle;
        private Float speed;
        private ScoreDto score;
        private Boolean isMostAccurate;
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
        public enum ScoreDto {
            S("S"),
            C("C"),
            O("O"),
            R("R"),
            ER("ER");

            private final String type;

            ScoreDto(String type) {
                this.type = type;
            }

            @Override
            public String toString() {
                return this.type;
            }
        }
    }

    @Data
    @Builder
    public static class KpIndexesDto {

        private Integer kp18;
        private Integer kp90;
        private Integer kp135;
        private Integer kp180;
    }

    @Data
    @Builder
    public static class ImpactDto {

        private Boolean isGlancingBlow;
        private String location;
        private String arrivalTime;
    }
}
