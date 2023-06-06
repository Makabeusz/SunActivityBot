package com.sojka.sunactivity.donki.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URL;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WsaEnlil {

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
    private URL link;
    private Set<Impact> impactList;
    private Set<String> cmeIDs;

    @Data
    public static final class Impact {

        private Boolean isGlancingBlow;
        private String location;
        private String arrivalTime;
    }
}
