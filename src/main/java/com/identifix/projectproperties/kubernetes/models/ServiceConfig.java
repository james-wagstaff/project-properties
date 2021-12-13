package com.identifix.projectproperties.kubernetes.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceConfig {

    @JsonProperty("Image")
    String image;
    @JsonProperty("Port")
    String port;
    @JsonProperty("Host Port")
    String hostPort;
    @JsonProperty("Limits")
    Object limits;
    @JsonProperty("Liveness")
    String liveness;
    @JsonProperty("Environment")
    Map<String, String> environment;

}
