package com.mykhailopavliuk.controller.user.urls;

import com.github.plushaze.traynotification.animations.Animations;
import com.github.plushaze.traynotification.notification.Notifications;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.mykhailopavliuk.controller.SignInController;
import com.mykhailopavliuk.controller.user.overview.LargeUserOverviewController;
import com.mykhailopavliuk.controller.user.overview.MediumUserOverviewController;
import com.mykhailopavliuk.controller.user.overview.SmallUserOverviewController;
import com.mykhailopavliuk.controller.user.settings.LargeUserSettingsController;
import com.mykhailopavliuk.controller.user.settings.MediumUserSettingsController;
import com.mykhailopavliuk.controller.user.settings.SmallUserSettingsController;
import com.mykhailopavliuk.controller.user.url.LargeUserUrlController;
import com.mykhailopavliuk.controller.user.url.MediumUserUrlController;
import com.mykhailopavliuk.controller.user.url.SmallUserUrlController;
import com.mykhailopavliuk.dto.UrlTableRowDTO;
import com.mykhailopavliuk.dto.UrlTransformer;
import com.mykhailopavliuk.exception.DatabaseOperationException;
import com.mykhailopavliuk.exception.EntityNotFoundException;
import com.mykhailopavliuk.model.Settings;
import com.mykhailopavliuk.model.Url;
import com.mykhailopavliuk.model.User;
import com.mykhailopavliuk.service.SettingsService;
import com.mykhailopavliuk.service.UrlService;
import com.mykhailopavliuk.util.TrayNotificationHandler;
import com.mykhailopavliuk.util.urlHandler.UrlHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import net.rgielen.fxweaver.core.FxWeaver;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public abstract class UserUrlsController implements Initializable {

    private final FxWeaver fxWeaver;
    private final UrlService urlService;
    private final SettingsService settingsService;
    private Settings.DisplayMode currentDisplayMode;
    private ObservableList<UrlTableRowDTO> urlsDtoObservableList;
    private List<Url> urlsToDelete;
    private boolean doesWeHaveUnsavedChanges;
    private User user;
    private Url sentUrlForViewingDetails;

    @FXML
    private Label userEmailLabel;
    @FXML
    private JFXTextField inputPath;
    @FXML
    private JFXButton saveNewPathButton;
    @FXML
    private Label pathValidationLabel;
    @FXML
    private JFXButton refreshTableButton;
    @FXML
    private JFXButton responseTimeButton;
    @FXML
    private JFXButton saveEditChangesButton;
    @FXML
    private JFXTreeTableView<UrlTableRowDTO> urlsTableView;
    @FXML
    private TreeTableColumn<UrlTableRowDTO, String> idColumn;
    @FXML
    private TreeTableColumn<UrlTableRowDTO, String> pathColumn;
    @FXML
    private TreeTableColumn<UrlTableRowDTO, String> detailsColumn;
    @FXML
    private TreeTableColumn<UrlTableRowDTO, String> deleteColumn;
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

    public UserUrlsController(FxWeaver fxWeaver, UrlService urlService, SettingsService settingsService) {
        this.fxWeaver = fxWeaver;
        this.urlService = urlService;
        this.settingsService = settingsService;
    }

    public Url getSentUrlForViewingDetails() {
        return sentUrlForViewingDetails;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currentDisplayMode = settingsService.read().getDisplayMode();
        initializeStyles();

        user = fxWeaver.loadController(SignInController.class).getSignedInUser();
        userEmailLabel.setText(user.getEmail());

        urlsDtoObservableList = FXCollections.observableArrayList();
        urlsToDelete = new ArrayList<>();
        pathValidationLabel.setVisible(false);
        initializeButtonsState();

        idColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("id"));
        pathColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("path"));

        initializeViewButtonColumn();
        initializeDeleteButtonColumn();

        urlsTableView.setShowRoot(false);
        updateTable();
    }

    private void initializeStyles() {
        userEmailLabel.setTextFill(Paint.valueOf(currentDisplayMode.getFontColorOnPrimary()));

        mainWindow.setStyle("-fx-background-color: " + currentDisplayMode.getBackgroundColor());
        sidebarPane.setStyle("-fx-background-color: " + currentDisplayMode.getPrimaryColor());

        overviewButton.setStyle("-fx-background-color: " + currentDisplayMode.getPrimaryColor());
        overviewButton.setTextFill(Paint.valueOf(currentDisplayMode.getFontColorOnPrimary()));

        urlsButton.setStyle("-fx-background-color: " + currentDisplayMode.getPrimaryColor());
        urlsButton.setTextFill(Paint.valueOf(currentDisplayMode.getFontColorOnPrimary()));

        settingsButton.setStyle("-fx-background-color: " + currentDisplayMode.getPrimaryColor());
        settingsButton.setTextFill(Paint.valueOf(currentDisplayMode.getFontColorOnPrimary()));

        signOutButton.setStyle("-fx-background-color: " + currentDisplayMode.getPrimaryColor());
        signOutButton.setTextFill(Paint.valueOf(currentDisplayMode.getFontColorOnPrimary()));

        refreshTableButton.setStyle("-fx-background-color: " + currentDisplayMode.getSecondaryColor());
        saveEditChangesButton.setStyle("-fx-background-color: " + currentDisplayMode.getSecondaryColor());
        saveNewPathButton.setStyle("-fx-background-color: " + currentDisplayMode.getPrimaryColor());

        inputPath.setFocusColor(Paint.valueOf(currentDisplayMode.getFontColorOnFormItems()));
        inputPath.setStyle("-fx-text-fill: " + currentDisplayMode.getFontColorOnBackground());
    }

    private void initializeViewButtonColumn() {
        Callback<TreeTableColumn<UrlTableRowDTO, String>, TreeTableCell<UrlTableRowDTO, String>> viewCellFactory
                =
                new Callback<>() {
                    @Override
                    public TreeTableCell<UrlTableRowDTO, String> call(final TreeTableColumn<UrlTableRowDTO, String> param) {
                        return new TreeTableCell<>() {

                            final ImageView imageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/icons8-view.png"))));
                            final JFXButton btn = new JFXButton("", imageView);

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                } else {
                                    imageView.setFitWidth(25);
                                    imageView.setFitHeight(25);
                                    btn.setButtonType(JFXButton.ButtonType.RAISED);
                                    btn.setRipplerFill(Color.web(currentDisplayMode.getPrimaryColor()));
                                    btn.setOnAction(event -> {
                                        sentUrlForViewingDetails = UrlTransformer.convertToEntity(getTableRow().getItem(), user);
                                        Stage stageTheEventSourceNodeBelongs = (Stage) ((Node) event.getSource()).getScene().getWindow();
                                        switch (settingsService.read().getScreenResolution()) {
                                            case SMALL:
                                                stageTheEventSourceNodeBelongs.setScene(new Scene(fxWeaver.loadView(SmallUserUrlController.class)));
                                                break;

                                            case LARGE:
                                                stageTheEventSourceNodeBelongs.setScene(new Scene(fxWeaver.loadView(LargeUserUrlController.class)));
                                                break;

                                            default:
                                                stageTheEventSourceNodeBelongs.setScene(new Scene(fxWeaver.loadView(MediumUserUrlController.class)));
                                                break;
                                        }
                                        stageTheEventSourceNodeBelongs.centerOnScreen();
                                    });
                                    setGraphic(btn);
                                }
                                setText(null);
                            }
                        };
                    }
                };

        detailsColumn.setCellFactory(viewCellFactory);
    }

    private void initializeDeleteButtonColumn() {
        Callback<TreeTableColumn<UrlTableRowDTO, String>, TreeTableCell<UrlTableRowDTO, String>> deleteCellFactory
                =
                new Callback<>() {
                    @Override
                    public TreeTableCell<UrlTableRowDTO, String> call(final TreeTableColumn<UrlTableRowDTO, String> param) {
                        return new TreeTableCell<>() {

                            final ImageView imageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/icons8-delete.png"))));
                            final JFXButton btn = new JFXButton("", imageView);

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                } else {
                                    imageView.setFitWidth(25);
                                    imageView.setFitHeight(25);
                                    btn.setButtonType(JFXButton.ButtonType.RAISED);
                                    btn.setRipplerFill(Color.web("#f44336"));
                                    btn.setOnAction(event -> {
                                        UrlTableRowDTO urlTableRowDTO = getTableRow().getItem();
                                        urlsToDelete.add(UrlTransformer.convertToEntity(urlTableRowDTO, user));
                                        urlsDtoObservableList.remove(urlTableRowDTO);
                                        doesWeHaveUnsavedChanges = true;
                                    });
                                    setGraphic(btn);
                                }
                                setText(null);
                            }
                        };
                    }
                };

        deleteColumn.setCellFactory(deleteCellFactory);
    }

    private void updateTable() {
        urlsDtoObservableList.clear();
        urlsToDelete.clear();

        for (Url url : user.getUrls()) {
            urlsDtoObservableList.add(UrlTransformer.convertToDto(url));
        }

        TreeItem<UrlTableRowDTO> root = new RecursiveTreeItem<>(urlsDtoObservableList, RecursiveTreeObject::getChildren);
        urlsTableView.setRoot(root);
        doesWeHaveUnsavedChanges = false;
    }

    @FXML
    void goToOverviewPage(ActionEvent event) {
        Stage stageTheEventSourceNodeBelongs = (Stage) ((Node) event.getSource()).getScene().getWindow();
        switch (settingsService.read().getScreenResolution()) {
            case SMALL:
                stageTheEventSourceNodeBelongs.setScene(new Scene(fxWeaver.loadView(SmallUserOverviewController.class)));
                break;

            case LARGE:
                stageTheEventSourceNodeBelongs.setScene(new Scene(fxWeaver.loadView(LargeUserOverviewController.class)));
                break;

            default:
                stageTheEventSourceNodeBelongs.setScene(new Scene(fxWeaver.loadView(MediumUserOverviewController.class)));
                break;
        }
        stageTheEventSourceNodeBelongs.centerOnScreen();
    }

    @FXML
    void goToSettingsPage(ActionEvent event) {
        Stage stageTheEventSourceNodeBelongs = (Stage) ((Node) event.getSource()).getScene().getWindow();
        switch (settingsService.read().getScreenResolution()) {
            case SMALL:
                stageTheEventSourceNodeBelongs.setScene(new Scene(fxWeaver.loadView(SmallUserSettingsController.class)));
                break;

            case LARGE:
                stageTheEventSourceNodeBelongs.setScene(new Scene(fxWeaver.loadView(LargeUserSettingsController.class)));
                break;

            default:
                stageTheEventSourceNodeBelongs.setScene(new Scene(fxWeaver.loadView(MediumUserSettingsController.class)));
                break;
        }
        stageTheEventSourceNodeBelongs.centerOnScreen();
    }

    @FXML
    void signOut(ActionEvent event) {
        UrlHandler.stopUrlAnalysis();
        Stage stageTheEventSourceNodeBelongs = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stageTheEventSourceNodeBelongs.setScene(new Scene(fxWeaver.loadView(SignInController.class)));
        stageTheEventSourceNodeBelongs.centerOnScreen();
    }

    @FXML
    void addPath(ActionEvent event) {
        if (inputPath.getText().isEmpty()) {
            return;
        }

        String path = inputPath.getText();
        Url url = new Url();
        url.setPath(path);

        if (user.getUrls().contains(url)) {
            TrayNotificationHandler.notify(
                    "You have already that path",
                    path,
                    Notifications.WARNING,
                    Animations.POPUP,
                    Paint.valueOf("#fc5b5b"),
                    Duration.seconds(3)
            );
            pathValidationLabel.setVisible(false);
            inputPath.setText("");
        } else {

            try {
                Url urlByPath = urlService.readByPath(path);
                urlByPath.setOwner(user);
                createAndAddUrlToTable(urlByPath);
            } catch (EntityNotFoundException e) {
                try {
                    if (UrlHandler.isUrlPathExists(path)) {
                        url.setId(urlService.getAvailableId());
                        url.setOwner(user);

                        try {
                            createAndAddUrlToTable(url);
                        } catch (DatabaseOperationException databaseException) {
                            TrayNotificationHandler.notify(
                                    "Sorry, can't add path",
                                    databaseException.getMessage(),
                                    Notifications.ERROR,
                                    Animations.POPUP,
                                    Paint.valueOf("#fc5b5b"),
                                    Duration.seconds(3)
                            );
                        }
                    } else {
                        pathValidationLabel.setVisible(true);
                    }
                } catch (IOException exception) {
                    pathValidationLabel.setVisible(true);
                }
            }
        }
    }

    private void createAndAddUrlToTable(Url url) {
        urlService.create(url);
        user.getUrls().add(url);
        updateTable();
        pathValidationLabel.setVisible(false);
        inputPath.setText("");

        TrayNotificationHandler.notify(
                "Congratulations",
                "You successfully created and saved a new path!",
                Notifications.SUCCESS,
                Animations.POPUP,
                Paint.valueOf(currentDisplayMode.getPrimaryColor()),
                Duration.seconds(5)
        );
    }

    @FXML
    void refreshTable(ActionEvent event) {
        if (doesWeHaveUnsavedChanges) {
            refreshTableButton.setDisable(true);
            updateTable();
            refreshTableButton.setDisable(false);
        }
    }

    @FXML
    void saveChanges(ActionEvent event) {
        if (!doesWeHaveUnsavedChanges) return;

        saveEditChangesButton.setDisable(true);

        for (Url url : urlsToDelete) {
            urlService.deleteByIdAndUserId(url.getId(), user.getId());
            user.getUrls().remove(url);
            UrlHandler.deleteUrlFromLog(url.getId());
        }

        updateTable();
        urlsToDelete.clear();

        TrayNotificationHandler.notify(
                "Well done!",
                "All changes were saved",
                Notifications.SUCCESS,
                Animations.POPUP,
                Paint.valueOf(currentDisplayMode.getPrimaryColor()),
                Duration.seconds(5)
        );

        saveEditChangesButton.setDisable(false);
    }

    @FXML
    void onResponseTimeButtonClick(ActionEvent event) {
        if (!UrlHandler.isIsAnalysingResponseTimes()) {
            ((Button) event.getSource()).setText("Stop response time analysis");
            ((Button) event.getSource()).setStyle("-fx-background-color: #f44336;");

            setEditTableButtonsDisability(true);
            UrlHandler.startUrlAnalysis(user);
        } else {
            ((Button) event.getSource()).setText("Start response time analysis");
            ((Button) event.getSource()).setStyle("-fx-background-color: " + currentDisplayMode.getPrimaryColor());

            setEditTableButtonsDisability(false);
            UrlHandler.stopUrlAnalysis();
        }

    }

    private void initializeButtonsState() {
        if (UrlHandler.isIsAnalysingResponseTimes()) {
            responseTimeButton.setText("Stop response time analysis");
            responseTimeButton.setStyle("-fx-background-color: #f44336;");

            setEditTableButtonsDisability(true);
        } else {
            responseTimeButton.setText("Start response time analysis");
            responseTimeButton.setStyle("-fx-background-color: " + currentDisplayMode.getPrimaryColor());

            setEditTableButtonsDisability(false);
        }
    }

    private void setEditTableButtonsDisability(boolean isDisable) {
        saveEditChangesButton.setDisable(isDisable);
        saveNewPathButton.setDisable(isDisable);
        refreshTableButton.setDisable(isDisable);
    }

}
