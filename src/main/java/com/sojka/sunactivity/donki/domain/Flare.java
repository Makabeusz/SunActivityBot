package com.sojka.sunactivity.donki.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Flare {

    @JsonProperty(value = "flrID", required = true)
    private String id;
    private List<Instrument> instruments;
    private String beginTime;
    private String peakTime;
    private String endTime;
    @JsonProperty(value = "classType")
    private String type;
    @JsonProperty(value = "sourceLocation")
    private String location;
    @JsonProperty(value = "activeRegionNum")
    private Integer activeRegion;
    private List<Event> linkedEvents;
    @JsonProperty(value = "link")
    private String url;

}
