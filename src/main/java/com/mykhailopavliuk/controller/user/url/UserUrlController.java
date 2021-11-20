package com.mykhailopavliuk.controller.user.url;

import com.mykhailopavliuk.controller.SignInController;
import com.mykhailopavliuk.controller.user.overview.MediumUserOverviewController;
import com.mykhailopavliuk.controller.user.settings.UserSettingsController;
import com.mykhailopavliuk.controller.user.urls.UserUrlsController;
import com.mykhailopavliuk.model.Url;
import com.mykhailopavliuk.service.SettingsService;
import com.mykhailopavliuk.util.urlHandler.PingStatistics;
import com.mykhailopavliuk.util.urlHandler.Response;
import com.mykhailopavliuk.util.urlHandler.UrlHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Component
@FxmlView("/view/user/url/medium-url.fxml")
public class UserUrlController implements Initializable {

    private final FxWeaver fxWeaver;
    private final SettingsService settingsService;
    private Url urlForAnalysing;
    private List<Response> responses;
    private PingStatistics pingStatistics;

    @FXML
    private Label userEmailLabel;

    @FXML
    private Label pathLabel;

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

    @Autowired
    public UserUrlController(FxWeaver fxWeaver, SettingsService settingsService) {
        this.fxWeaver = fxWeaver;
        this.settingsService = settingsService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        urlForAnalysing = fxWeaver.loadController(UserUrlsController.class).getSentUrlForViewingDetails();
        userEmailLabel.setText(urlForAnalysing.getOwner().getEmail());
        pathLabel.setText(urlForAnalysing.getPath());
        initializeStatistics();
        initializeChart();
        customizeChartHoverEffects();
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
        PieChart.Data slice1;
        PieChart.Data slice2;
        if (!responses.isEmpty()) {
            slice1 = new PieChart.Data(
                    "Correct responses"  ,
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
        Stage stageTheEventSourceNodeBelongs = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stageTheEventSourceNodeBelongs.setScene(new Scene(fxWeaver.loadView(MediumUserOverviewController.class)));
        stageTheEventSourceNodeBelongs.centerOnScreen();
    }

    @FXML
    void goToUrlsPage(MouseEvent event) {
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
    void signOut(ActionEvent event) {
        UrlHandler.stopUrlAnalysis();
        Stage stageTheEventSourceNodeBelongs = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stageTheEventSourceNodeBelongs.setScene(new Scene(fxWeaver.loadView(SignInController.class)));
        stageTheEventSourceNodeBelongs.centerOnScreen();
    }

}
