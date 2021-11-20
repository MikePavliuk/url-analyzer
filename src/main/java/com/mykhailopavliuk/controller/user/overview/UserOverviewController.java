package com.mykhailopavliuk.controller.user.overview;

import com.mykhailopavliuk.controller.SignInController;
import com.mykhailopavliuk.controller.user.settings.UserSettingsController;
import com.mykhailopavliuk.controller.user.urls.UserUrlsController;
import com.mykhailopavliuk.model.Url;
import com.mykhailopavliuk.model.User;
import com.mykhailopavliuk.service.UserService;
import com.mykhailopavliuk.util.urlHandler.PingStatistics;
import com.mykhailopavliuk.util.urlHandler.Response;
import com.mykhailopavliuk.util.urlHandler.UrlHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;

import java.net.URL;
import java.util.*;

public abstract class UserOverviewController implements Initializable {
    private User user;
    private final FxWeaver fxWeaver;
    private Map<Long, PingStatistics> pingStatisticsMap;
    private PingStatistics globalPingStatistics;

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
    private AreaChart<String, Number> responseChart;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;


    public UserOverviewController(FxWeaver fxWeaver) {
        this.fxWeaver = fxWeaver;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        user = fxWeaver.loadController(SignInController.class).getSignedInUser();
        userEmailLabel.setText(user.getEmail());
        pingStatisticsMap = new HashMap<>();
        initializeStatistics();
        initializeChart();
    }

    private void initializeStatistics() {
        List<Response> globalResponses = new ArrayList<>();
        for (Url url : user.getUrls()) {
            List<Response> responses = UrlHandler.getAllResponsesByUrlId(url.getId());
            if (!responses.isEmpty()) {
                pingStatisticsMap.put(url.getId(), new PingStatistics(responses));
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
        xAxis.setLabel("Url id");
        yAxis.setLabel("Response ms");
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
        stageTheEventSourceNodeBelongs.setScene(new Scene(fxWeaver.loadView(UserUrlsController.class)));
        stageTheEventSourceNodeBelongs.centerOnScreen();
    }

    @FXML
    void goToSettingsPage(ActionEvent event) {
        Stage stageTheEventSourceNodeBelongs = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stageTheEventSourceNodeBelongs.setScene(new Scene(fxWeaver.loadView(UserSettingsController.class)));
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
