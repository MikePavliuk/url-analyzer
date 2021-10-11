package com.mykhailopavliuk.controller;

import com.github.plushaze.traynotification.animations.Animations;
import com.github.plushaze.traynotification.notification.Notifications;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.mykhailopavliuk.exception.DatabaseOperationException;
import com.mykhailopavliuk.exception.EntityNotFoundException;
import com.mykhailopavliuk.model.User;
import com.mykhailopavliuk.service.UserService;
import com.mykhailopavliuk.util.TrayHandler;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

@Component
@FxmlView("/view/sign-in.fxml")
public class SignInController implements Initializable {

    private final UserService userService;

    private final FxWeaver fxWeaver;

    @FXML
    private JFXTextField inputEmail;

    @FXML
    private JFXPasswordField inputPassword;

    @FXML
    private Label singInFailedLabel;

    @Autowired
    public SignInController(UserService userService, FxWeaver fxWeaver) {
        this.userService = userService;
        this.fxWeaver = fxWeaver;
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

        try {
            User user = userService.readByEmail(email);

            if (Arrays.equals(user.getPassword(), password.toCharArray())) {
                TrayHandler.notify(
                        "Welcome, " + user.getEmail(),
                        "Now you can analyze urls you want",
                        Notifications.SUCCESS,
                        Animations.POPUP,
                        Paint.valueOf("#4883db"),
                        Duration.seconds(5)
                );
                Stage stageTheEventSourceNodeBelongs = (Stage) ((Node)event.getSource()).getScene().getWindow();
                DashboardMainController dashboardMainController = fxWeaver.loadController(DashboardMainController.class);
                dashboardMainController.setUser(user);
                stageTheEventSourceNodeBelongs.setScene(new Scene(fxWeaver.loadView(DashboardMainController.class)));
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
            TrayHandler.notify(
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
