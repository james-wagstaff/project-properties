package com.identifix.projectproperties.kubernetes.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Service {

    @JsonProperty("NAME")
    String name;
    @JsonProperty("TYPE")
    Object type;
    @JsonProperty("CLUSTER-IP")
    Object clusterIp;
    @JsonProperty("EXTERNAL-IP")
    Object externalIp;
    @JsonProperty("PORT(S)")
    Object port;
    @JsonProperty("AGE")
    Object age;

}
