package com.identifix.projectproperties;

import com.identifix.projectproperties.gui.KubernetesApplication;
import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.identifix")
public class ProjectPropertiesApplication {

    public static void main(String[] args) {
        Application.launch(KubernetesApplication.class, args);
    }

}
