package com.identifix.projectproperties.kubernetes.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import lombok.Data;

@Data
public class PodTemplate {

    @JsonProperty("Labels")
    Object labels;
    @JsonProperty("Service Account")
    String serviceAccount;
    @JsonProperty("Containers")
    Map<String, ServiceConfig> containers;
    @JsonProperty("Volumes")
    String volumes;

}
