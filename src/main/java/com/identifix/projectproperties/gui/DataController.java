package com.identifix.projectproperties.gui;

import static com.identifix.projectproperties.gui.utils.predicates.stringNotEmpty;
import static com.identifix.projectproperties.gui.utils.predicates.stringNotNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.identifix.projectproperties.gui.events.ListDataEvent;
import com.identifix.projectproperties.gui.events.ListEventPublisher;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class DataController implements Initializable, ApplicationListener<ListDataEvent> {

    private final Path CACHE_LOCATION = Path.of("cache.json");
    @FXML
    ChoiceBox<String> configs;
    @FXML
    ChoiceBox<String> namespaces;
    @FXML
    ChoiceBox<String> services;
    @FXML
    TextArea properties;
    @FXML
    ImageView loading;

    ListEventPublisher publisher;
    DataLoader dataLoader;
    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public DataController(ListEventPublisher listEventPublisher, DataLoader dataLoader) {
        this.publisher = listEventPublisher;
        this.dataLoader = dataLoader;
    }

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image image = new Image(getClass().getResourceAsStream("/loading.gif"));
        loading.setImage(image);
//        loading.setVisible(false);
        instantiateData();
    }

    private void instantiateData() {
        CacheData data = null;
        try {
            data = loadFromCache();
        } catch (IOException e) {
            data = new CacheData();
        } finally {
            dataLoader.loadData(data);
            addConfigEventListener();
            addNamespaceEventListener();
            addServiceEventListener();

        }
    }

    private CacheData loadFromCache() throws IOException {
        String savedData = Files.readString(CACHE_LOCATION);
        return mapper.readValue(savedData, CacheData.class);
    }

    private void addConfigEventListener() {
        this.configs.setOnAction(event -> {
            Thread thread = new Thread(() -> {
                Platform.runLater(() -> loading.setVisible(true));
                clearItems(Identifier.PROPERTIES, Identifier.NAMESPACES, Identifier.SERVICES);
                dataLoader.requestNamespaces(configs.getValue());
            });
            thread.setDaemon(true);
            thread.start();
        });
    }

    private void addNamespaceEventListener() {
        this.namespaces.setOnAction(event -> {
            Thread thread = new Thread(() -> {
                Platform.runLater(() -> loading.setVisible(true));
                clearItems(Identifier.PROPERTIES, Identifier.SERVICES);
                dataLoader.requestServices(namespaces.getValue(), configs.getValue());
            });
            thread.setDaemon(true);
            thread.start();
        });
    }

    private void addServiceEventListener() {
        this.services.setOnAction(event -> {
            Thread thread = new Thread(() -> {
                Platform.runLater(() -> loading.setVisible(true));
                clearItems(Identifier.PROPERTIES);
                String properties = dataLoader.getApplicationPropertiesForService(
                    this.namespaces.getValue(), this.services.getValue(), this.configs.getValue()
                );
                publisher.publishCustomEvent(Identifier.PROPERTIES, null, properties);
            });
            thread.setDaemon(true);
            thread.start();
        });
    }

    void clearItems(Identifier... boxes) {
        if (Objects.nonNull(boxes)) {
            Arrays.stream(boxes).forEach(box ->
                publisher.publishCustomEvent(box, null, ""));
        }
    }

    @SneakyThrows
    public void saveState() {
        CacheData data = new CacheData(
            configs.getItems(),
            configs.getValue(),
            namespaces.getItems(),
            namespaces.getValue(),
            services.getItems(),
            services.getValue());
        String value = mapper.writeValueAsString(data);
        Files.write(CACHE_LOCATION, value.getBytes());
    }

    @Override
    public void onApplicationEvent(ListDataEvent event) {
        switch (event.getIdentifier()) {
            case CONFIGS -> {
                loading.setVisible(false);
                configs.setItems(FXCollections.observableList(event.getList()));
                configs.getSelectionModel().select(event.getSelectedValue());
            }
            case NAMESPACES -> {
                loading.setVisible(false);
                Platform.runLater(
                    () -> namespaces.setItems(FXCollections.observableList(event.getList())));
                if (stringNotNull.and(stringNotEmpty).test(event.getSelectedValue())) {
                    namespaces.getSelectionModel().select(event.getSelectedValue());
                }
            }
            case SERVICES -> {
                loading.setVisible(false);
                Platform.runLater(
                    () -> services.setItems(FXCollections.observableList(event.getList())));
            }
            case PROPERTIES -> {
                loading.setVisible(false);
                properties.setText(event.getSelectedValue());
            }
        }
    }

}
