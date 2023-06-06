package com.sojka.sunactivity.donki.dto;

import lombok.Data;

import java.net.URI;
import java.util.HashSet;

@Data
public class WSAEnlil {

    private String modelCompletionTime;
    private Float au;
    private String estimatedShockArrivalTime;
    private Float estimatedDuration;
    private Float rmin_re;
    private Integer kp_18;
    private Integer kp_90;
    private Integer kp_135;
    private Integer kp_180;
    private Boolean isEarthGB;
    private URI link;
    private HashSet<Impact> impactList;
    private HashSet<String> cmeIDs;

    @Data
    public static final class Impact {

        private Boolean isGlancingBlow;
        private String location;
        private String arrivalTime;
    }
}
