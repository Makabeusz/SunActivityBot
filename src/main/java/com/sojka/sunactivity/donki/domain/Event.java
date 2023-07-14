package com.sojka.sunactivity.donki.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Event {

    @JsonProperty(value = "activityID")
    private String id;
}
