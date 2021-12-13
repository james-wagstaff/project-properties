package com.identifix.projectproperties.gui;

import static com.identifix.projectproperties.gui.utils.predicates.listNotEmpty;
import static com.identifix.projectproperties.gui.utils.predicates.listNotNull;
import static com.identifix.projectproperties.gui.utils.predicates.stringNotEmpty;
import static com.identifix.projectproperties.gui.utils.predicates.stringNotNull;

import com.identifix.projectproperties.gui.events.ListEventPublisher;
import com.identifix.projectproperties.kubernetes.models.Namespace;
import com.identifix.projectproperties.kubernetes.services.IKubernetesService;
import java.util.Arrays;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataLoader {

    IKubernetesService kubeservice;
    ListEventPublisher publisher;

    @Autowired
    public DataLoader(IKubernetesService kubeservice, ListEventPublisher publisher
    ) {
        this.kubeservice = kubeservice;
        this.publisher = publisher;
    }

    private void loadConfigs(ObservableList<String> configs, String selectedConfig) {
        if (listNotNull.and(listNotEmpty).test(configs)) {
            publisher.publishCustomEvent(Identifier.CONFIGS, configs, selectedConfig);
        } else {
            List<String> allConfigs = Arrays.asList(getConfigs());
            publisher.publishCustomEvent(Identifier.CONFIGS, allConfigs, allConfigs.get(0));
        }
    }

    void loadData(@NotNull CacheData data) {
        loadConfigs(data.getConfigs(), data.getSelectedConfig());
        loadNamespaces(data.getNamespaces(), data.getSelectedNamespace(), data.getSelectedConfig());
        loadServices(data.getServices(), data.getSelectedService());
    }

    private void loadNamespaces(ObservableList<String> namespaces, String selectedNamespace, String config) {
        String finalConfig = stringNotNull.and(stringNotEmpty).test(config) ? config : getConfigs()[0];
        new Thread(() -> {
            ObservableList<String> data = listNotNull.and(listNotEmpty).test(namespaces) ?
                namespaces :
                FXCollections.observableArrayList(getNamespaces(finalConfig));
            publisher.publishCustomEvent(Identifier.NAMESPACES, data, selectedNamespace);
        }).start();

    }

    private void loadServices(ObservableList<String> services, String selectedService) {
        new Thread(() -> {
            if (listNotNull.and(listNotEmpty).test(services)) {
                publisher.publishCustomEvent(Identifier.SERVICES, services, selectedService);
            }
        }).start();
    }

    private String[] getConfigs() {
        String config = System.getenv("KUBECONFIG");
        return config.split(";");
    }

    private String[] getNamespaces(String config) {
        return kubeservice.getNamespaces(config).stream()
            .map(namespace -> namespace.getName()).toList().toArray(new String[0]);
    }

    public String getApplicationPropertiesForService(String namespace, String service, String config) {
        return kubeservice.getServiceApplicationProperties(
            namespace, service, config);
    }

    public void requestServices(String namespace, String config) {
        publisher.publishCustomEvent(
            Identifier.SERVICES,
            kubeservice.getServices(namespace, config)
                .stream().map(com.identifix.projectproperties.kubernetes.models.Service::getName).toList(),
            null);
    }

    public void requestNamespaces(String config) {
        publisher.publishCustomEvent(
            Identifier.NAMESPACES,
            kubeservice.getNamespaces(config) // Add config later
                .stream().map(Namespace::getName).toList(),
            null);
    }
}
