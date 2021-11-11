package com.mykhailopavliuk.controller.user.settings;

import com.github.plushaze.traynotification.animations.Animations;
import com.github.plushaze.traynotification.notification.Notifications;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.mykhailopavliuk.controller.SignInController;
import com.mykhailopavliuk.controller.admin.overview.AdminOverviewController;
import com.mykhailopavliuk.controller.admin.users.AdminUsersController;
import com.mykhailopavliuk.controller.user.overview.MediumUserOverviewController;
import com.mykhailopavliuk.controller.user.overview.UserOverviewController;
import com.mykhailopavliuk.model.Settings;
import com.mykhailopavliuk.model.User;
import com.mykhailopavliuk.service.SettingsService;
import com.mykhailopavliuk.util.TrayNotificationHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.util.Objects;
import java.util.ResourceBundle;

@Component
@FxmlView("/view/user/settings/medium-settings.fxml")
public class UserSettingsController implements Initializable {

    private final FxWeaver fxWeaver;
    private final SettingsService settingsService;
    private boolean isWasEdit;

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

    @Autowired
    public UserSettingsController(FxWeaver fxWeaver, SettingsService settingsService) {
        this.fxWeaver = fxWeaver;
        this.settingsService = settingsService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userEmailLabel.setText(fxWeaver.loadController(MediumUserOverviewController.class).getUser().getEmail());

        isWasEdit = false;

        for (Settings.DisplayMode displayMode: Settings.DisplayMode.values()) {
            displayModeComboBox.getItems().add(displayMode);
        }

        displayModeComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if (!Objects.equals(settingsService.read().getDisplayMode(), newValue)) {
                isWasEdit = true;
            }
        });

        for (Settings.ScreenResolution screenResolution: Settings.ScreenResolution.values()) {
            screenResolutionComboBox.getItems().add(screenResolution);
        }

        screenResolutionComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if (!Objects.equals(settingsService.read().getScreenResolution(), newValue)) {
                isWasEdit = true;
            }
        });

        for (Settings.RequestsFrequency requestsFrequency: Settings.RequestsFrequency.values()) {
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

    @FXML
    void chooseExportDirectory(ActionEvent event) {
        String oldValue = exportDirectoryTextField.getText();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(settingsService.read().getExportDirectory().toFile());
        File directory = directoryChooser.showDialog(exportDirectoryTextField.getScene().getWindow());
        if (directory != null && !directory.getAbsolutePath().equals(oldValue)){
            if (!directory.getAbsolutePath().equals(oldValue)) {
                exportDirectoryTextField.setText(directory.getAbsolutePath());
                isWasEdit = true;
            }
        }
    }

    @FXML
    void goToOverviewPage(ActionEvent event) {
        Stage stageTheEventSourceNodeBelongs = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stageTheEventSourceNodeBelongs.setScene(new Scene(fxWeaver.loadView(MediumUserOverviewController.class)));
        stageTheEventSourceNodeBelongs.centerOnScreen();
    }

    @FXML
    void goToUrlsPage(ActionEvent event) {

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
                Paint.valueOf("#4883db"),
                Duration.seconds(3)
        );

        isWasEdit = false;
    }

    @FXML
    void signOut(ActionEvent event) {
        Stage stageTheEventSourceNodeBelongs = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stageTheEventSourceNodeBelongs.setScene(new Scene(fxWeaver.loadView(SignInController.class)));
        stageTheEventSourceNodeBelongs.centerOnScreen();
    }
}
