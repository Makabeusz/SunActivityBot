package com.sojka.sunactivity.donki.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Instrument {

    @JsonProperty(value = "displayName")
    private String name;
}