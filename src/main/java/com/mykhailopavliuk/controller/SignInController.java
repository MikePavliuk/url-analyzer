package com.mykhailopavliuk.controller;

import com.github.plushaze.traynotification.animations.Animations;
import com.github.plushaze.traynotification.notification.Notifications;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.mykhailopavliuk.configuration.application.AdminProperties;
import com.mykhailopavliuk.controller.admin.overview.AdminOverviewController;
import com.mykhailopavliuk.controller.admin.overview.LargeAdminOverviewController;
import com.mykhailopavliuk.controller.admin.overview.MediumAdminOverviewController;
import com.mykhailopavliuk.controller.admin.overview.SmallAdminOverviewController;
import com.mykhailopavliuk.controller.user.overview.MediumUserOverviewController;
import com.mykhailopavliuk.controller.user.overview.SmallUserOverviewController;
import com.mykhailopavliuk.exception.DatabaseOperationException;
import com.mykhailopavliuk.exception.EntityNotFoundException;
import com.mykhailopavliuk.model.Settings;
import com.mykhailopavliuk.model.User;
import com.mykhailopavliuk.service.SettingsService;
import com.mykhailopavliuk.service.UserService;
import com.mykhailopavliuk.util.TrayNotificationHandler;
import com.mykhailopavliuk.util.urlHandler.UrlHandler;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
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

    private final AdminProperties adminProperties;

    private final SettingsService settingsService;

    private User signedInUser;
    private Settings.DisplayMode currentDisplayMode;

    @FXML
    private JFXTextField inputEmail;
    @FXML
    private JFXPasswordField inputPassword;
    @FXML
    private Label singInFailedLabel;
    @FXML
    private Pane paneWithLogo;
    @FXML
    private Pane formPane;
    @FXML
    private Label signInLabel;
    @FXML
    private JFXButton signInButton;

    public User getSignedInUser() {
        return signedInUser;
    }

    @Autowired
    public SignInController(UserService userService, FxWeaver fxWeaver, AdminProperties adminProperties, SettingsService settingsService) {
        this.userService = userService;
        this.fxWeaver = fxWeaver;
        this.adminProperties = adminProperties;
        this.settingsService = settingsService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        singInFailedLabel.setVisible(false);
        currentDisplayMode = settingsService.read().getDisplayMode();
        initializeStyles();
    }

    public void initializeStyles() {
        paneWithLogo.setStyle("-fx-background-color: " + currentDisplayMode.getPrimaryColor());
        formPane.setStyle("-fx-background-color: " + currentDisplayMode.getBackgroundColor());
        inputEmail.setFocusColor(Paint.valueOf(currentDisplayMode.getFontColorOnFormItems()));
        inputEmail.setStyle("-fx-text-fill: " + currentDisplayMode.getFontColorOnBackground());
        inputPassword.setFocusColor(Paint.valueOf(currentDisplayMode.getFontColorOnFormItems()));
        inputPassword.setStyle("-fx-text-fill: " + currentDisplayMode.getFontColorOnBackground());
        signInLabel.setTextFill(Paint.valueOf(currentDisplayMode.getFontColorOnBackground()));
        signInButton.setStyle("-fx-background-color: " + currentDisplayMode.getPrimaryColor());
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
                    Paint.valueOf(currentDisplayMode.getPrimaryColor()),
                    Duration.seconds(5)
            );

            Stage stageTheEventSourceNodeBelongs = (Stage) ((Node)event.getSource()).getScene().getWindow();


            switch (settingsService.read().getScreenResolution()) {
                case SMALL:
                    stageTheEventSourceNodeBelongs.setScene(new Scene(fxWeaver.loadView(SmallAdminOverviewController.class)));
                    break;

                case MEDIUM:
                    stageTheEventSourceNodeBelongs.setScene(new Scene(fxWeaver.loadView(MediumAdminOverviewController.class)));
                    break;

                case LARGE:
                    stageTheEventSourceNodeBelongs.setScene(new Scene(fxWeaver.loadView(LargeAdminOverviewController.class)));
                    break;

                default:
                    stageTheEventSourceNodeBelongs.setScene(new Scene(fxWeaver.loadView(MediumAdminOverviewController.class)));
                    break;
            }

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
                        Paint.valueOf(currentDisplayMode.getPrimaryColor()),
                        Duration.seconds(5)
                );
                signedInUser = user;
                initializeUrlAnalysisOutputFile(signedInUser);
                Stage stageTheEventSourceNodeBelongs = (Stage) ((Node)event.getSource()).getScene().getWindow();
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

    private void initializeUrlAnalysisOutputFile(User user) {
        try {
            UrlHandler.initializeAnalysisOutputFile(user);
        } catch (IOException e) {
            TrayNotificationHandler.notify(
                    "Sorry, can't configure analysis files",
                    e.getMessage(),
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
