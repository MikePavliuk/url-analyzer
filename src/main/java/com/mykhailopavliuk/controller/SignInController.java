package com.mykhailopavliuk.controller;

import com.github.plushaze.traynotification.animations.Animations;
import com.github.plushaze.traynotification.notification.Notifications;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.mykhailopavliuk.configuration.application.AdminProperties;
import com.mykhailopavliuk.controller.admin.overview.AdminOverviewController;
import com.mykhailopavliuk.controller.user.overview.UserOverviewController;
import com.mykhailopavliuk.controller.user.overview.MediumUserOverviewController;
import com.mykhailopavliuk.exception.DatabaseOperationException;
import com.mykhailopavliuk.exception.EntityNotFoundException;
import com.mykhailopavliuk.model.Settings;
import com.mykhailopavliuk.model.User;
import com.mykhailopavliuk.service.SettingsService;
import com.mykhailopavliuk.service.UserService;
import com.mykhailopavliuk.util.TrayNotificationHandler;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

@Component
@FxmlView("/view/sign-in.fxml")
public class SignInController implements Initializable {

    private final UserService userService;

    private final FxWeaver fxWeaver;

    private final AdminProperties adminProperties;

    @FXML
    private JFXTextField inputEmail;

    @FXML
    private JFXPasswordField inputPassword;

    @FXML
    private Label singInFailedLabel;

    @Autowired
    public SignInController(UserService userService, FxWeaver fxWeaver, AdminProperties adminProperties) {
        this.userService = userService;
        this.fxWeaver = fxWeaver;
        this.adminProperties = adminProperties;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        singInFailedLabel.setVisible(false);
    }

    @FXML
    void handleSignInButton(ActionEvent event) {
        if (inputEmail.getText().isEmpty() || inputPassword.getText().isEmpty()) return;

        String email = inputEmail.getText();
        String password = inputPassword.getText();

        if (email.equals(adminProperties.getEmail()) && password.equals(adminProperties.getPassword())) {
            TrayNotificationHandler.notify(
                    "Welcome, Administrator",
                    "Now you can do everything you want with user accounts",
                    Notifications.SUCCESS,
                    Animations.POPUP,
                    Paint.valueOf("#4883db"),
                    Duration.seconds(5)
            );

            Stage stageTheEventSourceNodeBelongs = (Stage) ((Node)event.getSource()).getScene().getWindow();
            stageTheEventSourceNodeBelongs.setScene(new Scene(fxWeaver.loadView(AdminOverviewController.class)));
            stageTheEventSourceNodeBelongs.centerOnScreen();
            return;
        }

        try {
            User user = userService.readByEmail(email);

            if (Arrays.equals(user.getPassword(), password.toCharArray())) {
                TrayNotificationHandler.notify(
                        "Welcome, " + user.getEmail(),
                        "Now you can analyze urls you want",
                        Notifications.SUCCESS,
                        Animations.POPUP,
                        Paint.valueOf("#4883db"),
                        Duration.seconds(5)
                );
                Stage stageTheEventSourceNodeBelongs = (Stage) ((Node)event.getSource()).getScene().getWindow();
                UserOverviewController userOverviewController = fxWeaver.loadController(MediumUserOverviewController.class);
                userOverviewController.setUser(user);
                stageTheEventSourceNodeBelongs.setScene(new Scene(fxWeaver.loadView(MediumUserOverviewController.class)));
                stageTheEventSourceNodeBelongs.centerOnScreen();
            } else {
                throw new EntityNotFoundException();
            }

        } catch (EntityNotFoundException exception) {
            singInFailedLabel.setVisible(true);
            Timeline timeline = new Timeline();
            timeline.getKeyFrames().add(
                    new KeyFrame(Duration.seconds(3), (actionEvent) -> singInFailedLabel.setVisible(false))
            );
            timeline.play();
            inputPassword.setText("");

        } catch (DatabaseOperationException exception) {
            TrayNotificationHandler.notify(
                    "Error",
                    exception.getMessage(),
                    Notifications.ERROR,
                    Animations.POPUP,
                    Paint.valueOf("#fc5b5b"),
                    Duration.seconds(3)
            );
        }
    }

    @FXML
    void handleGoToSignUpButton(ActionEvent event) {
        Stage stageTheEventSourceNodeBelongs = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stageTheEventSourceNodeBelongs.setScene(new Scene(fxWeaver.loadView(SignUpController.class)));
        stageTheEventSourceNodeBelongs.centerOnScreen();
    }
}
