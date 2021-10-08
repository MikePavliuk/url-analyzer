package com.mykhailopavliuk.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.mykhailopavliuk.service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SignUpController {

    private UserService userService;

    @FXML
    private JFXTextField inputEmail;

    @FXML
    private JFXPasswordField inputPassword;

    @FXML
    private JFXButton signUpButton;

    @FXML
    private JFXButton goToSignInButton;

    @FXML
    private JFXPasswordField inputConfirmPassword;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @FXML
    void handleSignInButton(ActionEvent event) throws IOException {
        Stage stageTheEventSourceNodeBelongs = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stageTheEventSourceNodeBelongs.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/sign-in.fxml"))));
    }

}
