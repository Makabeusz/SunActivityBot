package com.sojka.sunactivity.donki.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URL;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cme {

    private String activityID;
    private String catalog;
    private String startTime;
    private String sourceLocation;
    private Integer activeRegionNum;
    private URL link;
    private String note;
    private Set<Instrument> instruments;
    private Set<CmeAnalyze> cmeAnalyses;
    private Set<Event> linkedEvents;

    public boolean willDeliverEarthGlancingBlow() {
        if (this.cmeAnalyses == null) {
            return false;
        }
        return cmeAnalyses.stream()
                .map(Cme.CmeAnalyze::getEnlilList)
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .max(Comparator.comparing(sim -> ZonedDateTime.parse(sim.getModelCompletionTime())))
                .orElse(WsaEnlil.builder().isEarthGB(false).build())
                .getIsEarthGB();
    }

    @Data
    public static class Instrument {

        private String displayName;
    }

    @Data
    public static class CmeAnalyze {

        private String time21_5;
        private Float latitude;
        private Float longitude;
        private Float halfAngle;
        private Float speed;
        private String type;
        private Boolean isMostAccurate;
        private String note;
        private Integer levelOfData;
        private URL link;
        private Set<WsaEnlil> enlilList;
    }

    @Data
    public static class Event {

        private String activityID;
    }
}
