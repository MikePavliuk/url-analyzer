package com.mykhailopavliuk.controller.admin.settings;

import com.github.plushaze.traynotification.animations.Animations;
import com.github.plushaze.traynotification.notification.Notifications;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.mykhailopavliuk.controller.SignInController;
import com.mykhailopavliuk.controller.admin.overview.AdminOverviewController;
import com.mykhailopavliuk.controller.admin.users.AdminUsersController;
import com.mykhailopavliuk.dto.UserTableRowDTO;
import com.mykhailopavliuk.dto.UserTransformer;
import com.mykhailopavliuk.model.Settings;
import com.mykhailopavliuk.model.User;
import com.mykhailopavliuk.service.SettingsService;
import com.mykhailopavliuk.util.TrayNotificationHandler;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
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
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

@Component
@FxmlView("/view/admin/settings/medium-settings.fxml")
public class AdminSettingsController implements Initializable {

    private final FxWeaver fxWeaver;
    private final SettingsService settingsService;
    private boolean isWasEdit;

    @FXML
    private JFXComboBox<Settings.DisplayMode> displayModeComboBox;

    @FXML
    private JFXComboBox<Settings.ScreenResolution> screenResolutionComboBox;

    @FXML
    private JFXTextField exportDirectoryTextField;

    @Autowired
    public AdminSettingsController(FxWeaver fxWeaver, SettingsService settingsService) {
        this.fxWeaver = fxWeaver;
        this.settingsService = settingsService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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

        Settings settings = settingsService.read();
        if (settings.getExportDirectory().toString().equals("/")) {
            settings.setExportDirectory(Path.of(System.getProperty("user.home")).toAbsolutePath());
            settingsService.save(settings);
        }

        isWasEdit = true;
        refreshForm();
        isWasEdit = false;
    }

    private void refreshForm() {
        if (isWasEdit) {
            displayModeComboBox.setValue(settingsService.read().getDisplayMode());
            screenResolutionComboBox.setValue(settingsService.read().getScreenResolution());
            exportDirectoryTextField.setText(settingsService.read().getExportDirectory().toString());
        }
    }

    @FXML
    void goToOverviewPage(ActionEvent event) {
        Stage stageTheEventSourceNodeBelongs = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stageTheEventSourceNodeBelongs.setScene(new Scene(fxWeaver.loadView(AdminOverviewController.class)));
        stageTheEventSourceNodeBelongs.centerOnScreen();
    }

    @FXML
    void goToUsersPage(ActionEvent event) {
        Stage stageTheEventSourceNodeBelongs = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stageTheEventSourceNodeBelongs.setScene(new Scene(fxWeaver.loadView(AdminUsersController.class)));
        stageTheEventSourceNodeBelongs.centerOnScreen();
    }

    @FXML
    void signOut(ActionEvent event) {
        Stage stageTheEventSourceNodeBelongs = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stageTheEventSourceNodeBelongs.setScene(new Scene(fxWeaver.loadView(SignInController.class)));
        stageTheEventSourceNodeBelongs.centerOnScreen();
    }

    @FXML
    void refreshSettings(ActionEvent event) {
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
    void saveChanges(ActionEvent event) {
        if (!isWasEdit) return;

        Settings settings = new Settings();
        settings.setDisplayMode(displayModeComboBox.getValue());
        settings.setScreenResolution(screenResolutionComboBox.getValue());
        settings.setExportDirectory(Path.of(exportDirectoryTextField.getText()));
        settings.setRequestsFrequency(settingsService.read().getRequestsFrequency());

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
}
