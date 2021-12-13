package com.identifix.projectproperties.gui;

import com.identifix.projectproperties.ProjectPropertiesApplication;
import java.io.File;
import java.io.InputStream;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

public class KubernetesApplication extends Application {

    private ConfigurableApplicationContext applicationContext;

    @Autowired
    DataController controller;

    @Override
    public void init() {
        ApplicationContextInitializer<GenericApplicationContext> initializer =
            ac -> {
                ac.registerBean(Application.class, () -> KubernetesApplication.this);
                ac.registerBean(Parameters.class, () -> getParameters());
                ac.registerBean(HostServices.class, () -> getHostServices());
            };

        this.applicationContext = new SpringApplicationBuilder()
            .sources(ProjectPropertiesApplication.class)
            .initializers(initializer)
            .run(getParameters().getRaw().toArray(new String[0]));
    }

    @Override
    public void stop() {
        controller.saveState();
        applicationContext.stop();
        Platform.exit();
    }

    @SneakyThrows
    @Override
    public void start(Stage stage) {
        applicationContext.publishEvent(new StageReadyEvent(stage));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main-stage.fxml"));
        loader.setControllerFactory(clazz -> applicationContext.getBean(clazz));
        VBox vbox = loader.load();
        Scene scene = new Scene(vbox);
        stage.setScene(scene);
        stage.show();
    }



    static class StageReadyEvent extends ApplicationEvent {

        public StageReadyEvent(Stage stage) {
            super(stage);
        }

        public Stage getStage() {
            return (Stage) getSource();
        }
    }
}
