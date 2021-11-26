package com.mykhailopavliuk.controller.user.overview;

import com.github.plushaze.traynotification.animations.Animations;
import com.github.plushaze.traynotification.notification.Notifications;
import com.jfoenix.controls.JFXButton;
import com.mykhailopavliuk.controller.SignInController;
import com.mykhailopavliuk.controller.user.settings.LargeUserSettingsController;
import com.mykhailopavliuk.controller.user.settings.MediumUserSettingsController;
import com.mykhailopavliuk.controller.user.settings.SmallUserSettingsController;
import com.mykhailopavliuk.controller.user.urls.LargeUserUrlsController;
import com.mykhailopavliuk.controller.user.urls.MediumUserUrlsController;
import com.mykhailopavliuk.controller.user.urls.SmallUserUrlsController;
import com.mykhailopavliuk.controller.user.urls.UserUrlsController;
import com.mykhailopavliuk.model.Settings;
import com.mykhailopavliuk.model.Url;
import com.mykhailopavliuk.model.User;
import com.mykhailopavliuk.service.SettingsService;
import com.mykhailopavliuk.util.ExcelHandler;
import com.mykhailopavliuk.util.TrayNotificationHandler;
import com.mykhailopavliuk.util.urlHandler.PingStatistics;
import com.mykhailopavliuk.util.urlHandler.Response;
import com.mykhailopavliuk.util.urlHandler.UrlHandler;
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
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.rgielen.fxweaver.core.FxWeaver;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public abstract class UserOverviewController implements Initializable {
    private User user;
    private final FxWeaver fxWeaver;
    private final SettingsService settingsService;
    private Map<Long, PingStatistics> pingStatisticsMap;
    private PingStatistics globalPingStatistics;
    private Settings.DisplayMode currentDisplayMode;

    @FXML
    private Label userEmailLabel;
    @FXML
    private Label totalNumberOfRequestsLabel;
    @FXML
    private Label slowestResponseTimeLabel;
    @FXML
    private Label fastestResponseTimeLabel;
    @FXML
    private Label averageResponseTimeLabel;
    @FXML
    private JFXButton refreshButton;
    @FXML
    private JFXButton exportButton;
    @FXML
    private AreaChart<String, Number> responseChart;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private StackPane mainWindow;
    @FXML
    private Label chartTitleLabel;
    @FXML
    private Pane totalCard;
    @FXML
    private Pane slowestCard;
    @FXML
    private Pane fastestCard;
    @FXML
    private Pane averageCard;
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
    private Circle circle1;
    @FXML
    private Circle circle2;
    @FXML
    private Circle circle3;
    @FXML
    private Circle circle4;
    @FXML
    private Label yAxisLabel;
    @FXML
    private Label xAxisLabel;


    public UserOverviewController(FxWeaver fxWeaver, SettingsService settingsService) {
        this.fxWeaver = fxWeaver;
        this.settingsService = settingsService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        user = fxWeaver.loadController(SignInController.class).getSignedInUser();
        userEmailLabel.setText(user.getEmail());
        pingStatisticsMap = new HashMap<>();
        currentDisplayMode = settingsService.read().getDisplayMode();
        initializeStatistics();
        initializeChart();
        initializeStyles();
    }

    private void initializeStyles() {
        userEmailLabel.setTextFill(Paint.valueOf(currentDisplayMode.getFontColorOnPrimary()));
        chartTitleLabel.setTextFill(Paint.valueOf(currentDisplayMode.getFontColorOnBackground()));

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

        totalCard.setStyle("-fx-background-color: " + currentDisplayMode.getSecondaryColor() + "; -fx-background-radius: 20px");
        slowestCard.setStyle("-fx-background-color: " + currentDisplayMode.getSecondaryColor() + "; -fx-background-radius: 20px");
        fastestCard.setStyle("-fx-background-color: " + currentDisplayMode.getSecondaryColor() + "; -fx-background-radius: 20px");
        averageCard.setStyle("-fx-background-color: " + currentDisplayMode.getSecondaryColor() + "; -fx-background-radius: 20px");

        exportButton.setStyle("-fx-background-color: " + currentDisplayMode.getSecondaryColor());
        refreshButton.setStyle("-fx-background-color: " + currentDisplayMode.getPrimaryColor());

        circle1.setFill(Paint.valueOf(currentDisplayMode.getPrimaryColor()));
        circle2.setFill(Paint.valueOf(currentDisplayMode.getPrimaryColor()));
        circle3.setFill(Paint.valueOf(currentDisplayMode.getPrimaryColor()));
        circle4.setFill(Paint.valueOf(currentDisplayMode.getPrimaryColor()));

        xAxis.setTickLabelFill(Paint.valueOf(currentDisplayMode.getFontColorOnBackground()));
        xAxisLabel.setStyle("-fx-text-fill: " + currentDisplayMode.getFontColorOnBackground());

        yAxis.setTickLabelFill(Paint.valueOf(currentDisplayMode.getFontColorOnBackground()));
        yAxisLabel.setStyle("-fx-text-fill: " + currentDisplayMode.getFontColorOnBackground());
    }

    @FXML
    void exportStatistics(ActionEvent event) {
        exportButton.setDisable(true);
        List<PingStatistics> pingStatisticsList = new ArrayList<>();
        pingStatisticsMap.forEach((key,value) -> {
            if (value != null) {
                pingStatisticsList.add(value);
            }
        });

        if (pingStatisticsList.isEmpty()) {
            TrayNotificationHandler.notify(
                    "Nothing to export",
                    "First analyze your urls",
                    Notifications.WARNING,
                    Animations.POPUP,
                    Paint.valueOf("#fc5b5b"),
                    Duration.seconds(5)
            );
        } else {
            try {
                ExcelHandler.exportUrlStatisticsToTable(settingsService.read().getExportDirectory(), pingStatisticsList);
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
                        Duration.seconds(5)
                );
            }
        }

        exportButton.setDisable(false);
    }

    @FXML
    void refreshStatistics(ActionEvent event) {
        refreshButton.setDisable(true);
        initializeStatistics();
        initializeChart();
        refreshButton.setDisable(false);
    }

    private void initializeStatistics() {
        pingStatisticsMap.clear();
        List<Response> globalResponses = new ArrayList<>();
        for (Url url : user.getUrls()) {
            List<Response> responses = UrlHandler.getAllResponsesByUrlId(url.getId());
            if (!responses.isEmpty()) {
                pingStatisticsMap.put(url.getId(), new PingStatistics(url, responses));
                globalResponses.addAll(responses);
            }
        }
        if (!globalResponses.isEmpty()) {
            globalPingStatistics = new PingStatistics(globalResponses);

            totalNumberOfRequestsLabel.setText(String.valueOf(globalPingStatistics.getTotalNumberOfRequests()));
            slowestResponseTimeLabel.setText(globalPingStatistics.getSlowestResponseTime().toMillis() + "ms");
            fastestResponseTimeLabel.setText(globalPingStatistics.getFastestResponseTime().toMillis() + "ms");
            averageResponseTimeLabel.setText(globalPingStatistics.getAverageResponseTime().toMillis() + "ms");
        } else {
            totalNumberOfRequestsLabel.setText("0");
            slowestResponseTimeLabel.setText("-");
            fastestResponseTimeLabel.setText("-");
            averageResponseTimeLabel.setText("-");
        }

    }

    private void initializeChart() {
        responseChart.getData().clear();
        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(0);
        yAxis.setUpperBound(6000);

        XYChart.Series<String, Number> seriesSlow = new XYChart.Series<>();
        XYChart.Series<String, Number> seriesFast = new XYChart.Series<>();
        XYChart.Series<String, Number> seriesAverage = new XYChart.Series<>();

        seriesSlow.setName("Slowest");
        seriesFast.setName("Fastest");
        seriesAverage.setName("Average");

        pingStatisticsMap.forEach((key,value) -> {
            if (value != null) {
                seriesSlow.getData().add(new XYChart.Data<>(String.valueOf(key), value.getSlowestResponseTime().toMillis()));
                seriesFast.getData().add(new XYChart.Data<>(String.valueOf(key), value.getFastestResponseTime().toMillis()));
                seriesAverage.getData().add(new XYChart.Data<>(String.valueOf(key), value.getAverageResponseTime().toMillis()));
            }
        });

        responseChart.getData().add(seriesSlow);
        responseChart.getData().add(seriesFast);
        responseChart.getData().add(seriesAverage);
    }

    @FXML
    void goToUrlsPage(ActionEvent event) {
        Stage stageTheEventSourceNodeBelongs = (Stage) ((Node) event.getSource()).getScene().getWindow();
        switch (settingsService.read().getScreenResolution()) {
            case SMALL:
                stageTheEventSourceNodeBelongs.setScene(new Scene(fxWeaver.loadView(SmallUserUrlsController.class)));
                break;

            case MEDIUM:
                stageTheEventSourceNodeBelongs.setScene(new Scene(fxWeaver.loadView(MediumUserUrlsController.class)));
                break;

            case LARGE:
                stageTheEventSourceNodeBelongs.setScene(new Scene(fxWeaver.loadView(LargeUserUrlsController.class)));
                break;

            default:
                stageTheEventSourceNodeBelongs.setScene(new Scene(fxWeaver.loadView(MediumUserUrlsController.class)));
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

            case MEDIUM:
                stageTheEventSourceNodeBelongs.setScene(new Scene(fxWeaver.loadView(MediumUserSettingsController.class)));
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
    public void signOut(ActionEvent event) {
        UrlHandler.stopUrlAnalysis();
        Stage stageTheEventSourceNodeBelongs = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stageTheEventSourceNodeBelongs.setScene(new Scene(fxWeaver.loadView(SignInController.class)));
        stageTheEventSourceNodeBelongs.centerOnScreen();
    }
}
