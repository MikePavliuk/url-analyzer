package com.mykhailopavliuk.controller.admin.users;

import com.github.plushaze.traynotification.animations.Animations;
import com.github.plushaze.traynotification.notification.Notifications;
import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.mykhailopavliuk.configuration.application.AdminProperties;
import com.mykhailopavliuk.controller.SignInController;
import com.mykhailopavliuk.controller.admin.overview.AdminOverviewController;
import com.mykhailopavliuk.controller.admin.settings.AdminSettingsController;
import com.mykhailopavliuk.dto.UserTableRowDTO;
import com.mykhailopavliuk.dto.UserTransformer;
import com.mykhailopavliuk.exception.DatabaseOperationException;
import com.mykhailopavliuk.exception.EntityNotFoundException;
import com.mykhailopavliuk.model.User;
import com.mykhailopavliuk.service.SettingsService;
import com.mykhailopavliuk.service.UserService;
import com.mykhailopavliuk.util.ExcelHandler;
import com.mykhailopavliuk.util.TrayNotificationHandler;
import com.mykhailopavliuk.util.ValidationHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTreeTableCell;
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
import java.util.*;

@Component
@FxmlView("/view/admin/users/medium-users.fxml")
public class AdminUsersController implements Initializable {

    private final UserService userService;

    private final SettingsService settingsService;

    private final AdminProperties adminProperties;

    private final FxWeaver fxWeaver;

    private ObservableList<UserTableRowDTO> usersDtoObservableList;
    private List<User> usersToDelete;
    private boolean doesWeHaveUnsavedChanges;


    @FXML
    private JFXTextField inputEmail;

    @FXML
    private JFXTextField inputPassword;

    @FXML
    private JFXButton saveNewUserButton;

    @FXML
    private JFXButton exportButton;

    @FXML
    private JFXButton saveChangesButton;

    @FXML
    private JFXButton refreshButton;

    @FXML
    private Label emailValidationLabel;

    @FXML
    private Label passwordValidationLabel;

    @FXML
    private JFXTreeTableView<UserTableRowDTO> usersTableView;

    @FXML
    private TreeTableColumn<UserTableRowDTO, Long> idColumn;

    @FXML
    private TreeTableColumn<UserTableRowDTO, String> emailColumn;

    @FXML
    private TreeTableColumn<UserTableRowDTO, Integer> numberOfUrlsColumn;


    @Autowired
    public AdminUsersController(UserService userService, SettingsService settingsService, AdminProperties adminProperties, FxWeaver fxWeaver) {
        this.userService = userService;
        this.settingsService = settingsService;
        this.adminProperties = adminProperties;
        this.fxWeaver = fxWeaver;
    }

    @FXML
    void goToOverviewPage(ActionEvent event) {
        if (doesWeHaveUnsavedChanges && !getLeaveWithoutChanges()) {
            return;
        }

        Stage stageTheEventSourceNodeBelongs = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stageTheEventSourceNodeBelongs.setScene(new Scene(fxWeaver.loadView(AdminOverviewController.class)));
        stageTheEventSourceNodeBelongs.centerOnScreen();
    }

    @FXML
    void goToSettingsPage(ActionEvent event) {
        if (doesWeHaveUnsavedChanges && !getLeaveWithoutChanges()) {
            return;
        }

        Stage stageTheEventSourceNodeBelongs = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stageTheEventSourceNodeBelongs.setScene(new Scene(fxWeaver.loadView(AdminSettingsController.class)));
        stageTheEventSourceNodeBelongs.centerOnScreen();
    }

    @FXML
    void signOut(ActionEvent event) {
        if (doesWeHaveUnsavedChanges && !getLeaveWithoutChanges()) {
            return;
        }

        Stage stageTheEventSourceNodeBelongs = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stageTheEventSourceNodeBelongs.setScene(new Scene(fxWeaver.loadView(SignInController.class)));
        stageTheEventSourceNodeBelongs.centerOnScreen();
    }

    private boolean getLeaveWithoutChanges() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Reminder");
        alert.setHeaderText("Do you really want to leave WITHOUT SAVING CHANGES?");
        alert.setContentText("Press OK to leave or Cancel to stay here");

        Optional<ButtonType> option = alert.showAndWait();

        return option.isPresent() && option.get() != ButtonType.CANCEL;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        usersDtoObservableList = FXCollections.observableArrayList();
        usersToDelete = new ArrayList<>();
        doesWeHaveUnsavedChanges = false;
        emailValidationLabel.setText(ValidationHandler.getEmailRegex().getMessage());
        passwordValidationLabel.setText(ValidationHandler.getPasswordValidation().getMessage());
        emailValidationLabel.setVisible(false);
        passwordValidationLabel.setVisible(false);


        idColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("id"));

        emailColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("email"));
        emailColumn.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
        emailColumn.setOnEditCommit(event -> {
            UserTableRowDTO editingUser = usersTableView.getTreeItem(event.getTreeTablePosition().getRow()).getValue();
            if (!event.getNewValue().matches(ValidationHandler.getEmailRegex().getRegex())) {
                editingUser.setEmail(event.getOldValue());
                usersTableView.refresh();
                TrayNotificationHandler.notify(
                        "Validation error",
                        ValidationHandler.getEmailRegex().getMessage()+ ". So old value was restored",
                        Notifications.ERROR,
                        Animations.POPUP,
                        Paint.valueOf("#4883db"),
                        Duration.seconds(5)
                );
            } else {
                if (!Objects.equals(event.getOldValue(), event.getNewValue())) {
                    editingUser.setEmail(event.getNewValue());
                    editingUser.setWasEdited(true);
                    doesWeHaveUnsavedChanges = true;
                }
            }
        });

        emailColumn.setEditable(true);
        usersTableView.setEditable(true);

        numberOfUrlsColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("numberOfUrls"));

        usersTableView.setShowRoot(false);
        addDeleteButtonColumn();
        updateTable();
    }

    @FXML
    void refreshTable(ActionEvent event) {
        if (doesWeHaveUnsavedChanges) {
            refreshButton.setDisable(true);
            updateTable();
            refreshButton.setDisable(false);
        }
    }

    private void updateTable() {
        usersDtoObservableList.clear();
        usersToDelete.clear();

        List<User> userList = userService.getAll();
        for (User user : userList) {
            usersDtoObservableList.add(UserTransformer.convertToDto(user));
        }

        TreeItem<UserTableRowDTO> root = new RecursiveTreeItem<>(usersDtoObservableList, RecursiveTreeObject::getChildren);
        usersTableView.setRoot(root);
        doesWeHaveUnsavedChanges = false;
    }

    private void addDeleteButtonColumn() {
        JFXTreeTableColumn<UserTableRowDTO, String> actionColumn = new JFXTreeTableColumn<>("Action");
        actionColumn.setPrefWidth(100);
        Callback<TreeTableColumn<UserTableRowDTO, String>, TreeTableCell<UserTableRowDTO, String>> cellFactory
                =
                new Callback<>() {
                    @Override
                    public TreeTableCell<UserTableRowDTO, String> call(final TreeTableColumn<UserTableRowDTO, String> param) {
                        return new TreeTableCell<>() {

                            final ImageView imageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/icons8-delete.png"))));
                            final JFXButton btn = new JFXButton("",imageView);

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
                                        UserTableRowDTO userTableRowDTO = getTableRow().getItem();
                                        usersToDelete.add(UserTransformer.convertToEntity(userTableRowDTO, userService));
                                        usersDtoObservableList.remove(userTableRowDTO);
                                        doesWeHaveUnsavedChanges = true;
                                    });
                                    setGraphic(btn);
                                }
                                setText(null);
                            }
                        };
                    }
                };

        actionColumn.setCellFactory(cellFactory);
        usersTableView.getColumns().add(actionColumn);
    }

    @FXML
    void addUser(ActionEvent event) {
        if (inputEmail.getText().isEmpty() || inputPassword.getText().isEmpty() || !validateForm()) {
            return;
        }

        saveNewUserButton.setDisable(true);

        String email = inputEmail.getText();
        String password = inputPassword.getText();

        if (email.equals(adminProperties.getEmail())) {
            emailValidationLabel.setVisible(true);
            emailValidationLabel.setText("Email is not valid");
            return;
        }

        try {
            userService.readByEmail(email);
            emailValidationLabel.setVisible(true);
            emailValidationLabel.setText("Email is already used");

        } catch (EntityNotFoundException e) {
            try {
                User user = userService.create(new User(
                        userService.getAvailableId(),
                        email,
                        password.toCharArray(),
                        new ArrayList<>()
                ));

                TrayNotificationHandler.notify(
                        "Congratulations",
                        "You successfully created and saved a new account!",
                        Notifications.SUCCESS,
                        Animations.POPUP,
                        Paint.valueOf("#4883db"),
                        Duration.seconds(3)
                );

                usersDtoObservableList.add(UserTransformer.convertToDto(user));
                updateTable();
                setVisibilityOfValidationLabels(false);
                inputEmail.setText("");
                inputPassword.setText("");

            } catch (DatabaseOperationException exception) {
                invokeTrayNotificationError(exception);
            }
        } catch (DatabaseOperationException exception) {
            invokeTrayNotificationError(exception);
        }

        saveNewUserButton.setDisable(false);
    }

    private void setVisibilityOfValidationLabels(boolean visibility) {
        emailValidationLabel.setVisible(visibility);
        passwordValidationLabel.setVisible(visibility);
    }

    private boolean validateForm() {
        setVisibilityOfValidationLabels(true);

        if (!inputEmail.getText().matches(ValidationHandler.getEmailRegex().getRegex())) {
            emailValidationLabel.setText(ValidationHandler.getEmailRegex().getMessage());
        } else {
            emailValidationLabel.setVisible(false);
        }

        if (!inputPassword.getText().matches(ValidationHandler.getPasswordValidation().getRegex())) {
            passwordValidationLabel.setText(ValidationHandler.getPasswordValidation().getMessage());
        } else {
            passwordValidationLabel.setVisible(false);
        }

        return !emailValidationLabel.isVisible() && !passwordValidationLabel.isVisible();
    }

    private void invokeTrayNotificationError(RuntimeException exception) {
        TrayNotificationHandler.notify(
                "Error",
                exception.getMessage(),
                Notifications.ERROR,
                Animations.POPUP,
                Paint.valueOf("#fc5b5b"),
                Duration.seconds(3)
        );
    }

    @FXML
    void saveChanges(ActionEvent event) {
        if (!doesWeHaveUnsavedChanges) return;

        saveChangesButton.setDisable(true);

        for (User user: usersToDelete) {
            userService.deleteById(user.getId());
        }

        for (UserTableRowDTO userDto : usersDtoObservableList) {
            User user = UserTransformer.convertToEntity(userDto, userService);
            if (userDto.isWasEdited()) {
                userService.update(user);
            }
        }

        TrayNotificationHandler.notify(
                "Well done!",
                "All changes were saved",
                Notifications.SUCCESS,
                Animations.POPUP,
                Paint.valueOf("#4883db"),
                Duration.seconds(5)
        );

        updateTable();
        usersToDelete.clear();
        doesWeHaveUnsavedChanges = false;
        saveChangesButton.setDisable(false);
    }

    @FXML
    void exportExcel(ActionEvent event) {
        exportButton.setDisable(true);
        try {
            ExcelHandler.exportUsersToTable(settingsService.read().getExportDirectory(), userService.getAll());

            TrayNotificationHandler.notify(
                    "Well done!",
                    "File was created in " + settingsService.read().getExportDirectory() + " folder",
                    Notifications.SUCCESS,
                    Animations.POPUP,
                    Paint.valueOf("#4883db"),
                    Duration.seconds(5)
            );

        } catch (IOException e) {
            TrayNotificationHandler.notify(
                    "Error",
                    "Exception has occurred while writing to xlsx file",
                    Notifications.ERROR,
                    Animations.POPUP,
                    Paint.valueOf("#fc5b5b"),
                    Duration.seconds(3)
            );
        }
        exportButton.setDisable(false);
    }

}
