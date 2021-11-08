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
import java.util.ResourceBundle;

@Component
@FxmlView("/view/admin/settings/medium-settings.fxml")
public class AdminSettingsController implements Initializable {

    @FXML
    private JFXComboBox<Settings.DisplayMode> displayModeComboBox;

    @FXML
    private JFXComboBox<Settings.ScreenResolution> screenResolutionComboBox;

    @FXML
    private JFXTextField exportDirectoryTextField;

    private final FxWeaver fxWeaver;
    private final SettingsService settingsService;


    @Autowired
    public AdminSettingsController(FxWeaver fxWeaver, SettingsService settingsService) {
        this.fxWeaver = fxWeaver;
        this.settingsService = settingsService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for (Settings.DisplayMode displayMode: Settings.DisplayMode.values()) {
            displayModeComboBox.getItems().add(displayMode);
        }

        for (Settings.ScreenResolution screenResolution: Settings.ScreenResolution.values()) {
            screenResolutionComboBox.getItems().add(screenResolution);
        }

        refreshForm();
    }

    private void refreshForm() {
        displayModeComboBox.setValue(settingsService.read().getDisplayMode());
        screenResolutionComboBox.setValue(settingsService.read().getScreenResolution());
        exportDirectoryTextField.setText(settingsService.read().getExportDirectory().toString());
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
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(settingsService.read().getExportDirectory().toFile());
        File directory = directoryChooser.showDialog(exportDirectoryTextField.getScene().getWindow());
        if (directory != null) exportDirectoryTextField.setText(directory.getAbsolutePath());
    }

    @FXML
    void saveChanges(ActionEvent event) {
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
    }
}
