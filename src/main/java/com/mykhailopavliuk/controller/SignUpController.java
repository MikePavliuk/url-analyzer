package com.mykhailopavliuk.controller;

import com.github.plushaze.traynotification.animations.Animations;
import com.github.plushaze.traynotification.notification.Notifications;
import com.github.plushaze.traynotification.notification.TrayNotification;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.mykhailopavliuk.exception.DatabaseOperationException;
import com.mykhailopavliuk.exception.EntityNotFoundException;
import com.mykhailopavliuk.exception.NullEntityReferenceException;
import com.mykhailopavliuk.model.User;
import com.mykhailopavliuk.service.UserService;
import com.mykhailopavliuk.util.TrayHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Component
@FxmlView("/view/sign-up.fxml")
public class SignUpController implements Initializable {

    private final UserService userService;

    private final FxWeaver fxWeaver;

    @FXML
    private JFXTextField inputEmail;

    @FXML
    private JFXPasswordField inputPassword;

    @FXML
    private JFXPasswordField inputConfirmPassword;

    @FXML
    private Label emailValidationLabel;

    @FXML
    private Label confirmPasswordValidationLabel;

    @FXML
    private Label passwordValidationLabel;

    @Autowired
    public SignUpController(UserService userService, FxWeaver fxWeaver) {
        this.userService = userService;
        this.fxWeaver = fxWeaver;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setVisibilityOfValidationLabels(false);

        inputEmail.textProperty().addListener(event -> {
            emailValidationLabel.setVisible(true);
            if (inputEmail.getText().isEmpty()) {
                emailValidationLabel.setText("Email can't be empty");
            } else if (!inputEmail.getText().matches("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")) {
                emailValidationLabel.setText("Email is not valid");
            } else {
                emailValidationLabel.setVisible(false);
            }
        });

        inputPassword.textProperty().addListener(event -> {
            passwordValidationLabel.setVisible(true);
            if (inputPassword.getText().isEmpty()) {
                passwordValidationLabel.setText("Password can't be empty");
            } else if (!inputPassword.getText().matches("^(?=.*[0-9]).{8,}$")) {
                passwordValidationLabel.setText("Must have at least 1 digit and min length of 8");
            } else {
                passwordValidationLabel.setVisible(false);
            }
        });

        inputConfirmPassword.textProperty().addListener(event -> {
            confirmPasswordValidationLabel.setVisible(true);
            if (inputConfirmPassword.getText().isEmpty()) {
                confirmPasswordValidationLabel.setText("Confirm your password");
            } else {
                confirmPasswordValidationLabel.setVisible(false);
            }
        });
    }

    @FXML
    public void handleSignInButton(ActionEvent event) {
        Stage stageTheEventSourceNodeBelongs = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stageTheEventSourceNodeBelongs.setScene(new Scene(fxWeaver.loadView(SignInController.class)));
    }

    @FXML
    public void handleSignUpButton(ActionEvent event) {
        if (!validateForm()) {
            return;
        }

        String email = inputEmail.getText();
        String password = inputPassword.getText();

        try {
            userService.readByEmail(email);
            emailValidationLabel.setVisible(true);
            emailValidationLabel.setText("Email is already used");

        } catch (EntityNotFoundException e) {
            try {
                userService.create(new User(
                        userService.getAvailableId(),
                        email,
                        password.toCharArray(),
                        null
                ));

                TrayHandler.notify(
                        "Congratulations",
                        "You successfully created an account! Now you can sign in",
                        Notifications.SUCCESS,
                        Animations.POPUP,
                        Paint.valueOf("#4883db"),
                        Duration.seconds(3)
                );

                handleSignInButton(event);

            } catch (DatabaseOperationException exception) {
                invokeTrayNotificationError(exception);
            }
        } catch (DatabaseOperationException exception) {
            invokeTrayNotificationError(exception);
        }
    }

    private boolean validateForm() {
        setVisibilityOfValidationLabels(true);

        if (inputEmail.getText().isEmpty()) {
            emailValidationLabel.setText("Email can't be empty");
        } else if (!inputEmail.getText().matches("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")) {
            emailValidationLabel.setText("Email is not valid");
        } else {
            emailValidationLabel.setVisible(false);
        }

        if (inputPassword.getText().isEmpty()) {
            passwordValidationLabel.setText("Password can't be empty");
        } else if (!inputPassword.getText().matches("^(?=.*[0-9]).{8,}$")) {
            passwordValidationLabel.setText("Must have at least 1 digit and min length of 8");
        } else {
            passwordValidationLabel.setVisible(false);
        }

        if (inputConfirmPassword.getText().isEmpty()) {
            confirmPasswordValidationLabel.setText("Confirm your password");
        } else if (!passwordValidationLabel.isVisible() && !inputPassword.getText().equals(inputConfirmPassword.getText())) {
            confirmPasswordValidationLabel.setText("Passwords not match");
            inputConfirmPassword.setText("");
        } else {
            confirmPasswordValidationLabel.setVisible(false);
        }

        return !emailValidationLabel.isVisible() && !passwordValidationLabel.isVisible() && !confirmPasswordValidationLabel.isVisible();
    }

    private void setVisibilityOfValidationLabels(boolean visibility) {
        emailValidationLabel.setVisible(visibility);
        passwordValidationLabel.setVisible(visibility);
        confirmPasswordValidationLabel.setVisible(visibility);
    }

    private void invokeTrayNotificationError(RuntimeException exception) {
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
