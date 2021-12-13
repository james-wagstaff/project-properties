package com.identifix.projectproperties.gui;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
class CacheData {

    String[] configs = new String[0];
    String selectedConfig;
    String[] namespaces = new String[0];
    String selectedNamespace;
    String[] services = new String[0];
    String selectedService;

    public CacheData(ObservableList<String> configs, String selectedConfig,
        ObservableList<String> namespaces,
        String selectedNamespace, ObservableList<String> services, String selectedService) {
        this.configs = configs.toArray(new String[0]);
        this.selectedConfig = selectedConfig;
        this.namespaces = namespaces.toArray(new String[0]);
        this.selectedNamespace = selectedNamespace;
        this.services = services.toArray(new String[0]);
        this.selectedService = selectedService;
    }

    public ObservableList<String> getConfigs() {
        return FXCollections.observableArrayList(configs);
    }

    public ObservableList<String> getNamespaces() {
        return FXCollections.observableArrayList(namespaces);
    }

    public ObservableList<String> getServices() {
        return FXCollections.observableArrayList(services);
    }

}

