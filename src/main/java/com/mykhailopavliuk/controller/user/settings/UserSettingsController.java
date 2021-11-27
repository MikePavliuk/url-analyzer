package com.mykhailopavliuk.controller.user.settings;

import com.github.plushaze.traynotification.animations.Animations;
import com.github.plushaze.traynotification.notification.Notifications;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.mykhailopavliuk.controller.SignInController;
import com.mykhailopavliuk.model.Settings;
import com.mykhailopavliuk.service.SettingsService;
import com.mykhailopavliuk.util.SceneHandler;
import com.mykhailopavliuk.util.TrayNotificationHandler;
import com.mykhailopavliuk.util.urlHandler.UrlHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.stage.DirectoryChooser;
import javafx.util.Duration;
import net.rgielen.fxweaver.core.FxWeaver;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.util.Objects;
import java.util.ResourceBundle;

public abstract class UserSettingsController implements Initializable {

    private final FxWeaver fxWeaver;
    private final SettingsService settingsService;
    private boolean isWasEdit;
    private Settings.DisplayMode currentDisplayMode;

    @FXML
    private Label userEmailLabel;
    @FXML
    private JFXComboBox<Settings.DisplayMode> displayModeComboBox;
    @FXML
    private JFXComboBox<Settings.ScreenResolution> screenResolutionComboBox;
    @FXML
    private JFXTextField exportDirectoryTextField;
    @FXML
    private JFXComboBox<Settings.RequestsFrequency> requestsFrequencyComboBox;
    @FXML
    private Pane sidebarPane;
    @FXML
    private JFXButton overviewButton;
    @FXML
    private JFXButton urlsButton;
    @FXML
    private JFXButton settingsButton;
    @FXML
    private JFXButton signOutButton;
    @FXML
    private StackPane mainWindow;
    @FXML
    private Label label1;
    @FXML
    private Label label2;
    @FXML
    private Label label3;
    @FXML
    private Label label4;
    @FXML
    private JFXButton chooseDirectoryButton;
    @FXML
    private JFXButton saveChangesButton;
    @FXML
    private JFXButton refreshButton;
    @FXML
    private Label titleLabel;

    public UserSettingsController(FxWeaver fxWeaver, SettingsService settingsService) {
        this.fxWeaver = fxWeaver;
        this.settingsService = settingsService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currentDisplayMode = settingsService.read().getDisplayMode();
        initializeStyles();

        userEmailLabel.setText(fxWeaver.loadController(SignInController.class).getSignedInUser().getEmail());

        isWasEdit = false;

        for (Settings.DisplayMode displayMode : Settings.DisplayMode.values()) {
            displayModeComboBox.getItems().add(displayMode);
        }

        displayModeComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if (!Objects.equals(settingsService.read().getDisplayMode(), newValue)) {
                isWasEdit = true;
            }
        });

        for (Settings.ScreenResolution screenResolution : Settings.ScreenResolution.values()) {
            screenResolutionComboBox.getItems().add(screenResolution);
        }

        screenResolutionComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if (!Objects.equals(settingsService.read().getScreenResolution(), newValue)) {
                isWasEdit = true;
            }
        });

        for (Settings.RequestsFrequency requestsFrequency : Settings.RequestsFrequency.values()) {
            requestsFrequencyComboBox.getItems().add(requestsFrequency);
        }

        requestsFrequencyComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if (!Objects.equals(settingsService.read().getRequestsFrequency(), newValue)) {
                isWasEdit = true;
            }
        });

        isWasEdit = true;
        refreshForm();

    }

    private void initializeStyles() {
        userEmailLabel.setTextFill(Paint.valueOf(currentDisplayMode.getFontColorOnPrimary()));

        if (currentDisplayMode == Settings.DisplayMode.LIGHT) {
            mainWindow.setStyle("-fx-background-color: " + currentDisplayMode.getBackgroundColor());
            label1.setStyle("-fx-text-fill: " + currentDisplayMode.getFontColorOnBackground());
            label2.setStyle("-fx-text-fill: " + currentDisplayMode.getFontColorOnBackground());
            label3.setStyle("-fx-text-fill: " + currentDisplayMode.getFontColorOnBackground());
            label4.setStyle("-fx-text-fill: " + currentDisplayMode.getFontColorOnBackground());
            titleLabel.setTextFill(Paint.valueOf(currentDisplayMode.getFontColorOnBackground()));
            exportDirectoryTextField.setStyle("-fx-text-fill: " + currentDisplayMode.getFontColorOnBackground());
        } else {
            mainWindow.setStyle("-fx-background-color: " + currentDisplayMode.getFontColorOnBackground());
            label1.setStyle("-fx-text-fill: " + currentDisplayMode.getPrimaryColor());
            label2.setStyle("-fx-text-fill: " + currentDisplayMode.getPrimaryColor());
            label3.setStyle("-fx-text-fill: " + currentDisplayMode.getPrimaryColor());
            label4.setStyle("-fx-text-fill: " + currentDisplayMode.getPrimaryColor());
            titleLabel.setTextFill(Paint.valueOf(currentDisplayMode.getPrimaryColor()));
        }

        sidebarPane.setStyle("-fx-background-color: " + currentDisplayMode.getPrimaryColor());

        overviewButton.setStyle("-fx-background-color: " + currentDisplayMode.getPrimaryColor());
        overviewButton.setTextFill(Paint.valueOf(currentDisplayMode.getFontColorOnPrimary()));

        urlsButton.setStyle("-fx-background-color: " + currentDisplayMode.getPrimaryColor());
        urlsButton.setTextFill(Paint.valueOf(currentDisplayMode.getFontColorOnPrimary()));

        settingsButton.setStyle("-fx-background-color: " + currentDisplayMode.getPrimaryColor());
        settingsButton.setTextFill(Paint.valueOf(currentDisplayMode.getFontColorOnPrimary()));

        signOutButton.setStyle("-fx-background-color: " + currentDisplayMode.getPrimaryColor());
        signOutButton.setTextFill(Paint.valueOf(currentDisplayMode.getFontColorOnPrimary()));

        refreshButton.setStyle("-fx-background-color: " + currentDisplayMode.getSecondaryColor());
        chooseDirectoryButton.setStyle("-fx-background-color: " + currentDisplayMode.getPrimaryColor());
        saveChangesButton.setStyle("-fx-background-color: " + currentDisplayMode.getPrimaryColor());
    }

    @FXML
    void chooseExportDirectory(ActionEvent event) {
        String oldValue = exportDirectoryTextField.getText();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(settingsService.read().getExportDirectory().toFile());
        File directory = directoryChooser.showDialog(exportDirectoryTextField.getScene().getWindow());
        if (directory != null && !directory.getAbsolutePath().equals(oldValue)) {
            if (!directory.getAbsolutePath().equals(oldValue)) {
                exportDirectoryTextField.setText(directory.getAbsolutePath());
                isWasEdit = true;
            }
        }
    }

    @FXML
    void goToOverviewPage(ActionEvent event) {
        SceneHandler.goToOverviewUserScene(event, settingsService, fxWeaver);
    }

    @FXML
    void goToUrlsPage(ActionEvent event) {
        SceneHandler.goToUrlsUserScene(event, settingsService, fxWeaver);
    }

    @FXML
    void refreshSettings(ActionEvent event) {
        refreshForm();
    }

    private void refreshForm() {
        if (isWasEdit) {
            displayModeComboBox.setValue(settingsService.read().getDisplayMode());
            screenResolutionComboBox.setValue(settingsService.read().getScreenResolution());
            requestsFrequencyComboBox.setValue(settingsService.read().getRequestsFrequency());
            exportDirectoryTextField.setText(settingsService.read().getExportDirectory().toString());
            isWasEdit = false;
        }
    }

    @FXML
    void saveChanges(ActionEvent event) {
        if (!isWasEdit) return;

        Settings settings = new Settings();
        settings.setDisplayMode(displayModeComboBox.getValue());
        settings.setScreenResolution(screenResolutionComboBox.getValue());
        settings.setRequestsFrequency(requestsFrequencyComboBox.getValue());
        settings.setExportDirectory(Path.of(exportDirectoryTextField.getText()));

        settingsService.save(settings);

        TrayNotificationHandler.notify(
                "Congratulations",
                "You successfully saved settings!",
                Notifications.SUCCESS,
                Animations.POPUP,
                Paint.valueOf(settings.getDisplayMode().getPrimaryColor()),
                Duration.seconds(5)
        );
        isWasEdit = false;
        SceneHandler.goToSettingsUserScene(event, settingsService, fxWeaver);
    }

    @FXML
    void signOut(ActionEvent event) {
        UrlHandler.stopUrlAnalysis();
        SceneHandler.goToSignInScene(event, settingsService, fxWeaver);
    }
}
