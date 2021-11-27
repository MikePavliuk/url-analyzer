package com.mykhailopavliuk.controller.admin.overview;


import com.jfoenix.controls.JFXButton;
import com.mykhailopavliuk.controller.SignInController;
import com.mykhailopavliuk.controller.admin.settings.LargeAdminSettingsController;
import com.mykhailopavliuk.controller.admin.settings.MediumAdminSettingsController;
import com.mykhailopavliuk.controller.admin.settings.SmallAdminSettingsController;
import com.mykhailopavliuk.controller.admin.users.LargeAdminUsersController;
import com.mykhailopavliuk.controller.admin.users.MediumAdminUsersController;
import com.mykhailopavliuk.controller.admin.users.SmallAdminUsersController;
import com.mykhailopavliuk.model.Settings;
import com.mykhailopavliuk.model.User;
import com.mykhailopavliuk.service.SettingsService;
import com.mykhailopavliuk.service.UrlService;
import com.mykhailopavliuk.service.UserService;
import com.mykhailopavliuk.util.SceneHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public abstract class AdminOverviewController implements Initializable {

    private final FxWeaver fxWeaver;

    private final UserService userService;
    private final UrlService urlService;
    private final SettingsService settingsService;

    private Settings.DisplayMode currentDisplayMode;

    @FXML
    private Label adminLabel;
    @FXML
    private Pane sidebarPane;
    @FXML
    private JFXButton overviewButton;
    @FXML
    private JFXButton usersButton;
    @FXML
    private JFXButton settingsButton;
    @FXML
    private JFXButton signOutButton;
    @FXML
    private BorderPane mainWindow;
    @FXML
    private Pane usersCard;
    @FXML
    private Label usersNumberLabel;
    @FXML
    private Pane urlsCard;
    @FXML
    private Label urlsNumberLabel;
    @FXML
    private AreaChart<String, Number> areaChart;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private Circle circle1;
    @FXML
    private Circle circle2;
    @FXML
    private Label xAxisLabel;
    @FXML
    private Label yAxisLabel;

    public AdminOverviewController(FxWeaver fxWeaver, UserService userService, UrlService urlService, SettingsService settingsService) {
        this.fxWeaver = fxWeaver;
        this.userService = userService;
        this.urlService = urlService;
        this.settingsService = settingsService;
    }

    @FXML
    void goToUsersPage(ActionEvent event) {
        SceneHandler.goToUsersAdminScene(event, settingsService, fxWeaver);
    }

    @FXML
    void goToSettingsPage(ActionEvent event) {
        SceneHandler.goToSettingsAdminScene(event, settingsService, fxWeaver);
    }

    @FXML
    void signOut(ActionEvent event) {
        Stage stageTheEventSourceNodeBelongs = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stageTheEventSourceNodeBelongs.setScene(new Scene(fxWeaver.loadView(SignInController.class)));
        stageTheEventSourceNodeBelongs.centerOnScreen();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currentDisplayMode = settingsService.read().getDisplayMode();
        initializeStyles();

        List<User> usersList = userService.getAll();

        usersNumberLabel.setText(String.valueOf(usersList.size()));
        urlsNumberLabel.setText(String.valueOf(urlService.getAll().size()));

        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(0);
        yAxis.setUpperBound(getMaxNumberOfUrlsFromUser(usersList) + 1);

        XYChart.Series<String, Number> series = new XYChart.Series<>();

        for (int i = 0; i < usersList.size(); i++) {
            series.getData().add(new XYChart.Data<>(String.valueOf(i), usersList.get(i).getUrls().size()));
        }

        areaChart.getData().add(series);

    }

    private void initializeStyles() {
        adminLabel.setTextFill(Paint.valueOf(currentDisplayMode.getFontColorOnPrimary()));

        mainWindow.setStyle("-fx-background-color: " + currentDisplayMode.getBackgroundColor());
        sidebarPane.setStyle("-fx-background-color: " + currentDisplayMode.getPrimaryColor());

        overviewButton.setStyle("-fx-background-color: " + currentDisplayMode.getPrimaryColor());
        overviewButton.setTextFill(Paint.valueOf(currentDisplayMode.getFontColorOnPrimary()));

        usersButton.setStyle("-fx-background-color: " + currentDisplayMode.getPrimaryColor());
        usersButton.setTextFill(Paint.valueOf(currentDisplayMode.getFontColorOnPrimary()));

        settingsButton.setStyle("-fx-background-color: " + currentDisplayMode.getPrimaryColor());
        settingsButton.setTextFill(Paint.valueOf(currentDisplayMode.getFontColorOnPrimary()));

        signOutButton.setStyle("-fx-background-color: " + currentDisplayMode.getPrimaryColor());
        signOutButton.setTextFill(Paint.valueOf(currentDisplayMode.getFontColorOnPrimary()));

        usersCard.setStyle("-fx-background-color: " + currentDisplayMode.getSecondaryColor() + "; -fx-background-radius: 20px");
        urlsCard.setStyle("-fx-background-color: " + currentDisplayMode.getSecondaryColor() + "; -fx-background-radius: 20px");

        circle1.setFill(Paint.valueOf(currentDisplayMode.getPrimaryColor()));
        circle2.setFill(Paint.valueOf(currentDisplayMode.getPrimaryColor()));

        xAxis.setTickLabelFill(Paint.valueOf(currentDisplayMode.getFontColorOnBackground()));
        xAxisLabel.setStyle("-fx-text-fill: " + currentDisplayMode.getFontColorOnBackground());

        yAxis.setTickLabelFill(Paint.valueOf(currentDisplayMode.getFontColorOnBackground()));
        yAxisLabel.setStyle("-fx-text-fill: " + currentDisplayMode.getFontColorOnBackground());
    }

    private int getMaxNumberOfUrlsFromUser(List<User> usersList) {
        int max = 0;
        for (User user : usersList) {
            if (user.getUrls().size() > max) {
                max = user.getUrls().size();
            }
        }

        return max;
    }

}
