package com.identifix.projectproperties.kubernetes.impl.services;

import static com.identifix.projectproperties.kubernetes.models.IngressInfo.createFromIngressOutput;

import com.identifix.projectproperties.kubernetes.impl.runners.CommandLineRunner;
import com.identifix.projectproperties.kubernetes.models.IngressInfo;
import com.identifix.projectproperties.kubernetes.models.Namespace;
import com.identifix.projectproperties.kubernetes.models.Pod;
import com.identifix.projectproperties.kubernetes.models.Service;
import com.identifix.projectproperties.kubernetes.models.ServiceConfig;
import com.identifix.projectproperties.kubernetes.models.ServiceInfo;
import com.identifix.projectproperties.kubernetes.parsers.ICsvParser;
import com.identifix.projectproperties.kubernetes.parsers.IYamlParser;
import com.identifix.projectproperties.kubernetes.services.IKubernetesService;
import com.identifix.projectproperties.kubernetes.models.Service;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

@org.springframework.stereotype.Service
@AllArgsConstructor
public class KubernetesService implements IKubernetesService {

    ICsvParser csvParser;
    IYamlParser yamlParser;
    CommandLineRunner runner;

    @Override
    @SneakyThrows
    public List<Namespace> getNamespaces(String config) {
        String command = "kubectl --kubeconfig=%s get namespaces".formatted(config);
        String resultSet = getResultsForCommand(command);
        return csvParser.read(Namespace.class, resultSet, true);
    }

    @Override
    public void updateConfigFile(String absolutePath) throws FileNotFoundException {

    }

    @SneakyThrows
    @Override
    public List<Service> getServices(String namespace, String config) {
        String command = "kubectl --kubeconfig=%s get services -n %s".formatted(config, namespace);
        String resultSet = getResultsForCommand(command);
        return csvParser.read(Service.class, resultSet, true);
    }

    @SneakyThrows
    @Override
    public List<Pod> getPods(String namespace) {
        String command = "kubectl get pods -n %s".formatted(namespace);
        String resultSet = getResultsForCommand(command);
        return csvParser.read(Pod.class, resultSet, true);
    }

    @Override
    public IngressInfo getIngressInfo(String namespace, String service, String config) {
        String command = "kubectl describe ingress -n %s %s".formatted(namespace, service);
        String resultSet = getResultsForCommand(command, false);
        return createFromIngressOutput(resultSet);
    }

    @Override
    public String getUrlForService(String namespace, String service, String config) {
        return getIngressInfo(namespace, service, config).getHost();
    }

    @Override
    @SneakyThrows
    public ServiceInfo getServiceConfiguration(String namespace, String service, String config) {
        String command = "kubectl --kubeconfig=%s describe deployment -n %s %s".formatted(config, namespace, service);
        String resultSet = getResultsForCommand(command, false);
        resultSet =  Pattern.compile("Annotations.*\\n").matcher(resultSet).replaceAll("");
        ServiceInfo info = yamlParser.read(ServiceInfo.class, resultSet);
        return info;
    }

    @Override
    public String getServiceApplicationProperties(String namespace, String service, String config) {
        Map<String, ServiceConfig> containers = getServiceConfiguration(namespace, service, config).getPodTemplate().getContainers();
        Map<String, String> environment = containers.get(containers.keySet().toArray()[0]).getEnvironment();
        String properties = parseAppProperties(environment);
        return properties;
    }

    private String parseAppProperties(Map<String, String> environment) {
        StringBuilder sb = new StringBuilder();
        environment.keySet().forEach(key  -> {
            sb.append(key.toLowerCase().replaceAll("_", "."))
                .append("=")
                .append(environment.get(key))
                .append("\n");
        });
        return sb.toString();
    }

    @SneakyThrows
    String getResultsForCommand(String command) {
     return getResultsForCommand(command, true);
    }

    @SneakyThrows
    String getResultsForCommand(String command, Boolean csv) {
        StringBuilder resultSet = new StringBuilder();
        runner.runCommand(command, line -> {
            resultSet.append(csv ? formatServerResponseAsCsv(line) : line).append("\n");
        }).get(20, TimeUnit.SECONDS);
        return resultSet.toString();
    }

    String formatServerResponseAsCsv(String input) {
        return Pattern.compile("\\s{2,}").matcher(input).replaceAll("|");
    }
}
