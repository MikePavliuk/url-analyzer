package com.mykhailopavliuk.controller.user.urls;

import com.github.plushaze.traynotification.animations.Animations;
import com.github.plushaze.traynotification.notification.Notifications;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.mykhailopavliuk.controller.SignInController;
import com.mykhailopavliuk.controller.user.overview.MediumUserOverviewController;
import com.mykhailopavliuk.controller.user.overview.UserOverviewController;
import com.mykhailopavliuk.controller.user.settings.UserSettingsController;
import com.mykhailopavliuk.controller.user.url.UserUrlController;
import com.mykhailopavliuk.dto.UrlTableRowDTO;
import com.mykhailopavliuk.dto.UrlTransformer;
import com.mykhailopavliuk.exception.DatabaseOperationException;
import com.mykhailopavliuk.exception.EntityNotFoundException;
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
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

@Component
@FxmlView("/view/user/urls/medium-urls.fxml")
public class UserUrlsController implements Initializable {

    private final FxWeaver fxWeaver;
    private final UrlService urlService;
    private final SettingsService settingsService;
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


    public Url getSentUrlForViewingDetails() {
        return sentUrlForViewingDetails;
    }

    @Autowired
    public UserUrlsController(FxWeaver fxWeaver, UrlService urlService, SettingsService settingsService) {
        this.fxWeaver = fxWeaver;
        this.urlService = urlService;
        this.settingsService = settingsService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
                                    btn.setRipplerFill(Color.web("#4883db"));
                                    btn.setOnAction(event -> {
                                        sentUrlForViewingDetails = UrlTransformer.convertToEntity(getTableRow().getItem(), user);
                                        Stage stageTheEventSourceNodeBelongs = (Stage) ((Node)event.getSource()).getScene().getWindow();
                                        stageTheEventSourceNodeBelongs.setScene(new Scene(fxWeaver.loadView(UserUrlController.class)));
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
        stageTheEventSourceNodeBelongs.setScene(new Scene(fxWeaver.loadView(MediumUserOverviewController.class)));
        stageTheEventSourceNodeBelongs.centerOnScreen();
    }

    @FXML
    void goToSettingsPage(ActionEvent event) {
        Stage stageTheEventSourceNodeBelongs = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stageTheEventSourceNodeBelongs.setScene(new Scene(fxWeaver.loadView(UserSettingsController.class)));
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
                Paint.valueOf("#4883db"),
                Duration.seconds(3)
        );
    }

    @FXML
    void refreshTable(ActionEvent event) {
        if (doesWeHaveUnsavedChanges) {
            updateTable();
        }
    }

    @FXML
    void saveChanges(ActionEvent event) {
        if (!doesWeHaveUnsavedChanges) return;

        for (Url url : urlsToDelete) {
            urlService.deleteByIdAndUserId(url.getId(), user.getId());
            user.getUrls().remove(url);
        }

        updateTable();
        urlsToDelete.clear();

        TrayNotificationHandler.notify(
                "Well done!",
                "All changes were saved",
                Notifications.SUCCESS,
                Animations.POPUP,
                Paint.valueOf("#4883db"),
                Duration.seconds(5)
        );
    }

    @FXML
    void onResponseTimeButtonClick(ActionEvent event) {
        if (!UrlHandler.isIsAnalysingResponseTimes()) {
            ((Button)event.getSource()).setText("Stop response time analysis");
            ((Button)event.getSource()).setStyle("-fx-background-color: #f44336;");

            setEditTableButtonsDisability(true);
            try {
                UrlHandler.startUrlAnalysis(user);
            } catch (IOException e) {
                UrlHandler.setIsAnalysingResponseTimes(false);
                TrayNotificationHandler.notify(
                        "Sorry, can't start analysis",
                        e.getMessage(),
                        Notifications.ERROR,
                        Animations.POPUP,
                        Paint.valueOf("#fc5b5b"),
                        Duration.seconds(3)
                );
            }
        } else {
            ((Button)event.getSource()).setText("Start response time analysis");
            ((Button)event.getSource()).setStyle("-fx-background-color: #4883db;");

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
            responseTimeButton.setStyle("-fx-background-color: #4883db;");

            setEditTableButtonsDisability(false);
        }
    }

    private void setEditTableButtonsDisability(boolean isDisable) {
        saveEditChangesButton.setDisable(isDisable);
        saveNewPathButton.setDisable(isDisable);
        refreshTableButton.setDisable(isDisable);
    }

}
