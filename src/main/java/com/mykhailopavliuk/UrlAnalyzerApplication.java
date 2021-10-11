package com.mykhailopavliuk;
import com.mykhailopavliuk.controller.SignInController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class UrlAnalyzerApplication extends Application {

    private ConfigurableApplicationContext context;

    @Override
    public void init() {
        String[] args = getParameters().getRaw().toArray(new String[0]);

        context = new SpringApplicationBuilder()
                .sources(SpringApplication.class)
                .run(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FxWeaver fxWeaver = context.getBean(FxWeaver.class);
        Parent root = fxWeaver.loadView(SignInController.class);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.centerOnScreen();
        //context.publishEvent(new StageReadyEvent(primaryStage));
    }

    @Override
    public void stop() throws Exception {
        this.context.close();
        Platform.exit();
    }
}
