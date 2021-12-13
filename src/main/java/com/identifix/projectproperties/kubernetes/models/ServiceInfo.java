package com.identifix.projectproperties.kubernetes.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ServiceInfo {

    @JsonProperty("Name")
    String name;
    @JsonProperty("Namespace")
    String namespace;
    @JsonProperty("CreationTimestamp")
    String creationTimestamp;
    @JsonProperty("Labels")
    String labels;
    @JsonProperty("Annotations")
    String annotations;
    @JsonProperty("Selector")
    String selector;
    @JsonProperty("Replicas")
    String replicas;
    @JsonProperty("StrategyType")
    String strategyType;
    @JsonProperty("MinReadySeconds")
    String minReadySeconds;
    @JsonProperty("RollingUpdateStrategy")
    String rollingUpdateStrategy;
    @JsonProperty("Pod Template")
    PodTemplate podTemplate;

    @JsonProperty("Conditions")
    String conditions;
    @JsonProperty("OldReplicaSets")
    String oldReplicaSets;
    @JsonProperty("NewReplicaSet")
    String newReplicaSet;
    @JsonProperty("Events")
    String events;

}


