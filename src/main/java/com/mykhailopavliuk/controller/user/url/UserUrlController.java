package com.mykhailopavliuk.controller.user.url;

import com.jfoenix.controls.JFXButton;
import com.mykhailopavliuk.controller.SignInController;
import com.mykhailopavliuk.controller.user.overview.LargeUserOverviewController;
import com.mykhailopavliuk.controller.user.overview.MediumUserOverviewController;
import com.mykhailopavliuk.controller.user.overview.SmallUserOverviewController;
import com.mykhailopavliuk.controller.user.settings.LargeUserSettingsController;
import com.mykhailopavliuk.controller.user.settings.MediumUserSettingsController;
import com.mykhailopavliuk.controller.user.settings.SmallUserSettingsController;
import com.mykhailopavliuk.controller.user.urls.LargeUserUrlsController;
import com.mykhailopavliuk.controller.user.urls.MediumUserUrlsController;
import com.mykhailopavliuk.controller.user.urls.SmallUserUrlsController;
import com.mykhailopavliuk.model.Settings;
import com.mykhailopavliuk.model.Url;
import com.mykhailopavliuk.service.SettingsService;
import com.mykhailopavliuk.util.SceneHandler;
import com.mykhailopavliuk.util.urlHandler.PingStatistics;
import com.mykhailopavliuk.util.urlHandler.Response;
import com.mykhailopavliuk.util.urlHandler.UrlHandler;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.rgielen.fxweaver.core.FxWeaver;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public abstract class UserUrlController implements Initializable {

    private final FxWeaver fxWeaver;
    private final SettingsService settingsService;
    private Settings.DisplayMode currentDisplayMode;
    private Url urlForAnalysing;
    private List<Response> responses;
    private PingStatistics pingStatistics;

    @FXML
    private Label userEmailLabel;
    @FXML
    private Label idLabel;
    @FXML
    private JFXButton refreshButton;
    @FXML
    private Label totalNumberOfRequestsLabel;
    @FXML
    private Label slowestResponseTimeLabel;
    @FXML
    private Label fastestResponseTimeLabel;
    @FXML
    private Label averageResponseTimeLabel;
    @FXML
    private PieChart pieChart;
    @FXML
    private JFXButton overviewButton;
    @FXML
    private JFXButton urlsButton;
    @FXML
    private JFXButton settingsButton;
    @FXML
    private JFXButton signOutButton;
    @FXML
    private Pane topPane;
    @FXML
    private Pane sidebarPane;
    @FXML
    private BorderPane mainWindow;
    @FXML
    private Pane totalCard;
    @FXML
    private Circle circle1;
    @FXML
    private Pane slowestCard;
    @FXML
    private Circle circle2;
    @FXML
    private Pane fastestCard;
    @FXML
    private Circle circle3;
    @FXML
    private Pane averageCard;
    @FXML
    private Circle circle4;

    public UserUrlController(FxWeaver fxWeaver, SettingsService settingsService) {
        this.fxWeaver = fxWeaver;
        this.settingsService = settingsService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currentDisplayMode = settingsService.read().getDisplayMode();
        initializeStyles();
        switch (settingsService.read().getScreenResolution()) {
            case SMALL:
                urlForAnalysing = fxWeaver.loadController(SmallUserUrlsController.class).getSentUrlForViewingDetails();
                break;

            case LARGE:
                urlForAnalysing = fxWeaver.loadController(LargeUserUrlsController.class).getSentUrlForViewingDetails();
                break;

            default:
                urlForAnalysing = fxWeaver.loadController(MediumUserUrlsController.class).getSentUrlForViewingDetails();
                break;
        }
        userEmailLabel.setText(urlForAnalysing.getOwner().getEmail());
        idLabel.setText("Id: " + urlForAnalysing.getId());
        initializeStatistics();
        initializeChart();
        customizeChartHoverEffects();
    }

    private void initializeStyles() {
        userEmailLabel.setTextFill(Paint.valueOf(currentDisplayMode.getFontColorOnPrimary()));

        if (currentDisplayMode == Settings.DisplayMode.LIGHT) {
            mainWindow.setStyle("-fx-background-color: " + currentDisplayMode.getBackgroundColor());
        } else {
            mainWindow.setStyle("-fx-background-color: " + currentDisplayMode.getFontColorOnBackground());
        }

        sidebarPane.setStyle("-fx-background-color: " + currentDisplayMode.getPrimaryColor());
        topPane.setStyle("-fx-background-color: " + currentDisplayMode.getSecondaryColor());

        overviewButton.setStyle("-fx-background-color: " + currentDisplayMode.getPrimaryColor());
        overviewButton.setTextFill(Paint.valueOf(currentDisplayMode.getFontColorOnPrimary()));

        urlsButton.setStyle("-fx-background-color: " + currentDisplayMode.getPrimaryColor());
        urlsButton.setTextFill(Paint.valueOf(currentDisplayMode.getFontColorOnPrimary()));

        settingsButton.setStyle("-fx-background-color: " + currentDisplayMode.getPrimaryColor());
        settingsButton.setTextFill(Paint.valueOf(currentDisplayMode.getFontColorOnPrimary()));

        signOutButton.setStyle("-fx-background-color: " + currentDisplayMode.getPrimaryColor());
        signOutButton.setTextFill(Paint.valueOf(currentDisplayMode.getFontColorOnPrimary()));

        refreshButton.setStyle("-fx-background-color: " + currentDisplayMode.getPrimaryColor());

        totalCard.setStyle("-fx-background-color: " + currentDisplayMode.getPrimaryColor() + "; -fx-background-radius: 20px");
        slowestCard.setStyle("-fx-background-color: " + currentDisplayMode.getPrimaryColor() + "; -fx-background-radius: 20px");
        fastestCard.setStyle("-fx-background-color: " + currentDisplayMode.getPrimaryColor() + "; -fx-background-radius: 20px");
        averageCard.setStyle("-fx-background-color: " + currentDisplayMode.getPrimaryColor() + "; -fx-background-radius: 20px");

        circle1.setFill(Paint.valueOf(currentDisplayMode.getSecondaryColor()));
        circle2.setFill(Paint.valueOf(currentDisplayMode.getSecondaryColor()));
        circle3.setFill(Paint.valueOf(currentDisplayMode.getSecondaryColor()));
        circle4.setFill(Paint.valueOf(currentDisplayMode.getSecondaryColor()));

        pieChart.setStyle("-fx-fill: " + currentDisplayMode.getFontColorOnBackground());
    }

    private void initializeStatistics() {
        responses = UrlHandler.getAllResponsesByUrlId(urlForAnalysing.getId());
        if (!responses.isEmpty()) {
            pingStatistics = new PingStatistics(responses);

            totalNumberOfRequestsLabel.setText(String.valueOf(pingStatistics.getTotalNumberOfRequests()));
            slowestResponseTimeLabel.setText(pingStatistics.getSlowestResponseTime().toMillis() + "ms");
            fastestResponseTimeLabel.setText(pingStatistics.getFastestResponseTime().toMillis() + "ms");
            averageResponseTimeLabel.setText(pingStatistics.getAverageResponseTime().toMillis() + "ms");
        } else {
            totalNumberOfRequestsLabel.setText("0");
            slowestResponseTimeLabel.setText("-");
            fastestResponseTimeLabel.setText("-");
            averageResponseTimeLabel.setText("-");
        }
    }

    private void initializeChart() {
        pieChart.getData().clear();

        PieChart.Data slice1;
        PieChart.Data slice2;
        if (!responses.isEmpty()) {
            slice1 = new PieChart.Data(
                    "Correct responses",
                    pingStatistics.getTotalNumberOfRequests() - pingStatistics.getNumberOfNotTimeoutResponses());
            slice2 = new PieChart.Data(
                    "Timeout responses",
                    pingStatistics.getNumberOfNotTimeoutResponses());
        } else {
            slice1 = new PieChart.Data(
                    "Correct responses",
                    1);
            slice2 = new PieChart.Data(
                    "Timeout responses",
                    1);
        }

        pieChart.getData().add(slice1);
        pieChart.getData().add(slice2);

    }

    private void customizeChartHoverEffects() {
        for (final PieChart.Data data : pieChart.getData()) {
            Tooltip tooltip;

            if (!responses.isEmpty()) {
                tooltip = new Tooltip(
                        Math.round(data.getPieValue()) +
                                " (" + ((data.getPieValue() * 100) / pingStatistics.getTotalNumberOfRequests() + "%)"));
            } else {
                tooltip = new Tooltip("Not analyzed yet");
            }

            tooltip.setFont(Font.font("Arial Black"));
            tooltip.setStyle("-fx-background-color: white; -fx-text-fill: black;");
            tooltip.setShowDelay(Duration.seconds(0.5));
            Tooltip.install(data.getNode(), tooltip);
        }
    }

    @FXML
    void goToOverviewPage(ActionEvent event) {
        SceneHandler.goToOverviewUserScene(event, settingsService, fxWeaver);
    }

    @FXML
    void goToUrlsPage(Event event) {
        SceneHandler.goToUrlsUserScene(event, settingsService, fxWeaver);
    }

    @FXML
    void goToSettingsPage(ActionEvent event) {
        SceneHandler.goToSettingsUserScene(event, settingsService, fxWeaver);
    }

    @FXML
    void signOut(ActionEvent event) {
        UrlHandler.stopUrlAnalysis();
        SceneHandler.goToSignInScene(event, settingsService, fxWeaver);
    }

    @FXML
    void refreshStatistics(ActionEvent event) {
        refreshButton.setDisable(true);
        initializeStatistics();
        initializeChart();
        customizeChartHoverEffects();
        refreshButton.setDisable(false);
    }

}
