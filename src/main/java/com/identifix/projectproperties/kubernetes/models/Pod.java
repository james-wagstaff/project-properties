package com.identifix.projectproperties.kubernetes.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Pod {

    @JsonProperty("NAME")
    String name;
    @JsonProperty("READY")
    String ready;
    @JsonProperty("STATUS")
    String status;
    @JsonProperty("RESTARTS")
    String restarts;
    @JsonProperty("AGE")
    String age;

}
