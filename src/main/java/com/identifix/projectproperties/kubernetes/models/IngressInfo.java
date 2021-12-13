package com.identifix.projectproperties.kubernetes.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Data;

@Data
public class IngressInfo {

    @JsonProperty("Name")
    String name;
    @JsonProperty("Namespace")
    String namespace;
    @JsonProperty("Address")
    String[] address;
    @JsonProperty("Host")
    String host;
    @JsonProperty("Annotations")
    String annotations;
    @JsonProperty("Events")
    String events;

    public IngressInfo(Map<String, String> map) {
        this.name = map.get("name");
        this.namespace = map.get("namespace");
        this.address = map.get("address").split(",");
        this.host = map.get("host");
        this.annotations = map.get("annotations");
        this.events = map.get("events");
    }

    public static IngressInfo createFromIngressOutput(String rawInfo) {
        Matcher matcher = Pattern.compile("[A-Z][ a-zA-Z]+:.*").matcher(rawInfo);
        List<String> results = new ArrayList<>();
        matcher.results().forEach(it -> results.add(it.group()));
        Map<String, String> map = new HashMap<>();
        results.stream().forEach(it -> {
            String key = it.substring(0, it.indexOf(':')).toLowerCase();
            String value = it.substring(it.indexOf(':') + 1).trim();
            map.put(key, value);
        });
        map.put("host", parseIngressHost(rawInfo));
        return new IngressInfo(map);
    }

    private static String parseIngressHost(String ingressInfo) {
        Matcher matcher = Pattern.compile("[\\w\\.\\-]*farm").matcher(ingressInfo);
        return matcher.results().findFirst().get().group();
    }

}
