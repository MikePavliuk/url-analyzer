package com.mykhailopavliuk.controller.admin.settings;

import com.github.plushaze.traynotification.animations.Animations;
import com.github.plushaze.traynotification.notification.Notifications;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.mykhailopavliuk.controller.SignInController;
import com.mykhailopavliuk.controller.admin.overview.LargeAdminOverviewController;
import com.mykhailopavliuk.controller.admin.overview.MediumAdminOverviewController;
import com.mykhailopavliuk.controller.admin.overview.SmallAdminOverviewController;
import com.mykhailopavliuk.controller.admin.users.LargeAdminUsersController;
import com.mykhailopavliuk.controller.admin.users.MediumAdminUsersController;
import com.mykhailopavliuk.controller.admin.users.SmallAdminUsersController;
import com.mykhailopavliuk.model.Settings;
import com.mykhailopavliuk.service.SettingsService;
import com.mykhailopavliuk.util.TrayNotificationHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.rgielen.fxweaver.core.FxWeaver;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.util.Objects;
import java.util.ResourceBundle;

public abstract class AdminSettingsController implements Initializable {

    private final FxWeaver fxWeaver;
    private final SettingsService settingsService;
    private boolean isWasEdit;
    private Settings.DisplayMode currentDisplayMode;

    @FXML
    private JFXComboBox<Settings.DisplayMode> displayModeComboBox;
    @FXML
    private JFXComboBox<Settings.ScreenResolution> screenResolutionComboBox;
    @FXML
    private JFXTextField exportDirectoryTextField;
    @FXML
    private StackPane mainWindow;
    @FXML
    private Label label1;
    @FXML
    private Label label2;
    @FXML
    private Label label3;
    @FXML
    private JFXButton chooseDirectoryButton;
    @FXML
    private JFXButton saveChangesButton;
    @FXML
    private JFXButton refreshButton;
    @FXML
    private Label titleLabel;
    @FXML
    private Pane sidebarPane;
    @FXML
    private Label adminLabel;
    @FXML
    private JFXButton overviewButton;
    @FXML
    private JFXButton usersButton;
    @FXML
    private JFXButton settingsButton;
    @FXML
    private JFXButton signOutButton;

    public AdminSettingsController(FxWeaver fxWeaver, SettingsService settingsService) {
        this.fxWeaver = fxWeaver;
        this.settingsService = settingsService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currentDisplayMode = settingsService.read().getDisplayMode();
        initializeStyles();

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

        isWasEdit = true;
        refreshForm();
    }

    private void initializeStyles() {
        adminLabel.setTextFill(Paint.valueOf(currentDisplayMode.getFontColorOnPrimary()));

        if (currentDisplayMode == Settings.DisplayMode.LIGHT) {
            mainWindow.setStyle("-fx-background-color: " + currentDisplayMode.getBackgroundColor());
            label1.setStyle("-fx-text-fill: " + currentDisplayMode.getFontColorOnBackground());
            label2.setStyle("-fx-text-fill: " + currentDisplayMode.getFontColorOnBackground());
            label3.setStyle("-fx-text-fill: " + currentDisplayMode.getFontColorOnBackground());
            titleLabel.setTextFill(Paint.valueOf(currentDisplayMode.getFontColorOnBackground()));
            exportDirectoryTextField.setStyle("-fx-text-fill: " + currentDisplayMode.getFontColorOnBackground());
        } else {
            mainWindow.setStyle("-fx-background-color: " + currentDisplayMode.getFontColorOnBackground());
            label1.setStyle("-fx-text-fill: " + currentDisplayMode.getPrimaryColor());
            label2.setStyle("-fx-text-fill: " + currentDisplayMode.getPrimaryColor());
            label3.setStyle("-fx-text-fill: " + currentDisplayMode.getPrimaryColor());
            titleLabel.setTextFill(Paint.valueOf(currentDisplayMode.getPrimaryColor()));
        }

        sidebarPane.setStyle("-fx-background-color: " + currentDisplayMode.getPrimaryColor());

        overviewButton.setStyle("-fx-background-color: " + currentDisplayMode.getPrimaryColor());
        overviewButton.setTextFill(Paint.valueOf(currentDisplayMode.getFontColorOnPrimary()));

        usersButton.setStyle("-fx-background-color: " + currentDisplayMode.getPrimaryColor());
        usersButton.setTextFill(Paint.valueOf(currentDisplayMode.getFontColorOnPrimary()));

        settingsButton.setStyle("-fx-background-color: " + currentDisplayMode.getPrimaryColor());
        settingsButton.setTextFill(Paint.valueOf(currentDisplayMode.getFontColorOnPrimary()));

        signOutButton.setStyle("-fx-background-color: " + currentDisplayMode.getPrimaryColor());
        signOutButton.setTextFill(Paint.valueOf(currentDisplayMode.getFontColorOnPrimary()));

        refreshButton.setStyle("-fx-background-color: " + currentDisplayMode.getSecondaryColor());
        chooseDirectoryButton.setStyle("-fx-background-color: " + currentDisplayMode.getPrimaryColor());
        saveChangesButton.setStyle("-fx-background-color: " + currentDisplayMode.getPrimaryColor());
    }

    private void refreshForm() {
        if (isWasEdit) {
            displayModeComboBox.setValue(settingsService.read().getDisplayMode());
            screenResolutionComboBox.setValue(settingsService.read().getScreenResolution());
            exportDirectoryTextField.setText(settingsService.read().getExportDirectory().toString());
            isWasEdit = false;
        }
    }

    @FXML
    void goToOverviewPage(ActionEvent event) {
        Stage stageTheEventSourceNodeBelongs = (Stage) ((Node) event.getSource()).getScene().getWindow();
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
    }

    @FXML
    void goToUsersPage(ActionEvent event) {
        Stage stageTheEventSourceNodeBelongs = (Stage) ((Node) event.getSource()).getScene().getWindow();
        switch (settingsService.read().getScreenResolution()) {
            case SMALL:
                stageTheEventSourceNodeBelongs.setScene(new Scene(fxWeaver.loadView(SmallAdminUsersController.class)));
                break;

            case MEDIUM:
                stageTheEventSourceNodeBelongs.setScene(new Scene(fxWeaver.loadView(MediumAdminUsersController.class)));
                break;

            case LARGE:
                stageTheEventSourceNodeBelongs.setScene(new Scene(fxWeaver.loadView(LargeAdminUsersController.class)));
                break;

            default:
                stageTheEventSourceNodeBelongs.setScene(new Scene(fxWeaver.loadView(MediumAdminUsersController.class)));
                break;
        }
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
                Paint.valueOf(settings.getDisplayMode().getPrimaryColor()),
                Duration.seconds(5)
        );

        isWasEdit = false;
        Stage stageTheEventSourceNodeBelongs = (Stage) ((Node) event.getSource()).getScene().getWindow();
        switch (settingsService.read().getScreenResolution()) {
            case SMALL:
                stageTheEventSourceNodeBelongs.setScene(new Scene(fxWeaver.loadView(SmallAdminSettingsController.class)));
                break;

            case MEDIUM:
                stageTheEventSourceNodeBelongs.setScene(new Scene(fxWeaver.loadView(MediumAdminSettingsController.class)));
                break;

            case LARGE:
                stageTheEventSourceNodeBelongs.setScene(new Scene(fxWeaver.loadView(LargeAdminSettingsController.class)));
                break;

            default:
                stageTheEventSourceNodeBelongs.setScene(new Scene(fxWeaver.loadView(MediumAdminSettingsController.class)));
                break;
        };
        stageTheEventSourceNodeBelongs.centerOnScreen();
    }
}
