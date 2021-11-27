package com.mykhailopavliuk;

import com.dustinredmond.fxtrayicon.FXTrayIcon;
import com.mykhailopavliuk.controller.SignInController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import javax.swing.*;
import java.util.Objects;

public class UrlAnalyzerApplication extends Application {

    private ConfigurableApplicationContext context;

    @Override
    public void init() {
        String[] args = getParameters().getRaw().toArray(new String[0]);

        context = new SpringApplicationBuilder()
                .sources(SpringApplication.class)
                .headless(false)
                .run(args);
    }

    @Override
    public void start(Stage primaryStage) {
        FxWeaver fxWeaver = context.getBean(FxWeaver.class);
        Parent root = fxWeaver.loadView(SignInController.class);

        primaryStage.getIcons().add(new Image("/assets/url-analyzer-icon-tray-blue-200.png"));
        try {
            java.awt.Image image = new ImageIcon(getClass().getResource("/assets/url-analyzer-icon-tray-blue-200.png"), "URL Analyzer").getImage();
            com.apple.eawt.Application.getApplication().setDockIconImage(image);
        } catch (Exception e) {
            // Won't work on Windows or Linux.
        }

        Platform.setImplicitExit(false);
        primaryStage.setOnCloseRequest(e -> {
            e.consume();
            primaryStage.toBack();
        });

        FXTrayIcon icon = new FXTrayIcon.Builder(primaryStage, Objects.requireNonNull(getClass().getResource("/assets/url-analyzer-icon-tray-white-150.png")))
                .menuItem(
                        "Show", e -> primaryStage.toFront()
                )
                .menuItem(
                        "Exit", e -> {
                            this.context.close();
                            Platform.exit();
                            System.exit(0);
                        }
                )
                .show()
                .build();


        Scene scene = new Scene(root);
        primaryStage.setTitle("URL Analyzer");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.centerOnScreen();
        primaryStage.setResizable(false);
    }

    @Override
    public void stop() {
        this.context.close();
        Platform.exit();
    }
}
