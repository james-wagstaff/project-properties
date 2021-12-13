package com.identifix.projectproperties.kubernetes.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Namespace {

    @JsonProperty("NAME")
    String name;
    @JsonProperty("STATUS")
    String status;
    @JsonProperty("AGE")
    String age;

}
