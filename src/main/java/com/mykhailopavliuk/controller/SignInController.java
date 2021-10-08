package com.mykhailopavliuk.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.mykhailopavliuk.model.User;
import com.mykhailopavliuk.repository.impl.UserRepositoryImpl;
import com.mykhailopavliuk.service.UserService;
import com.mykhailopavliuk.service.impl.UserServiceImpl;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SignInController {

    private UserService userService;

    @FXML
    private JFXTextField inputEmail;

    @FXML
    private JFXPasswordField inputPassword;

    @FXML
    private JFXButton signInButton;

    @FXML
    private JFXButton goToSignUpButton;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @FXML
    void handleSignUpButton(ActionEvent event) throws IOException {
        Stage stageTheEventSourceNodeBelongs = (Stage) ((Node)event.getSource()).getScene().getWindow();
        //userService.create(new User(1, "email", new char[]{'1', '3', '4'}, null));
        stageTheEventSourceNodeBelongs.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/sign-up.fxml"))));
    }
}
