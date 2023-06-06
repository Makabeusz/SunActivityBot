package com.sojka.sunactivity.donki.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CME {

    private String activityID;
    private String catalog;
    private String startTime;
    private String sourceLocation;
    private String activeRegionNum;
    private String link;
    private String note;
    private HashSet<Instrument> instruments;
    private HashSet<CmeAnalyze> cmeAnalyses;
    private HashSet<Event> linkedEvents;

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
        private String link;
        private HashSet<WSAEnlil> enlilList;

    }

    @Data
    public static class Event {

        private String activityID;
    }
}
