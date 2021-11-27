package com.mykhailopavliuk.util;

import com.mykhailopavliuk.controller.SignInController;
import com.mykhailopavliuk.controller.SignUpController;
import com.mykhailopavliuk.controller.admin.overview.LargeAdminOverviewController;
import com.mykhailopavliuk.controller.admin.overview.MediumAdminOverviewController;
import com.mykhailopavliuk.controller.admin.overview.SmallAdminOverviewController;
import com.mykhailopavliuk.controller.admin.settings.LargeAdminSettingsController;
import com.mykhailopavliuk.controller.admin.settings.MediumAdminSettingsController;
import com.mykhailopavliuk.controller.admin.settings.SmallAdminSettingsController;
import com.mykhailopavliuk.controller.admin.users.LargeAdminUsersController;
import com.mykhailopavliuk.controller.admin.users.MediumAdminUsersController;
import com.mykhailopavliuk.controller.admin.users.SmallAdminUsersController;
import com.mykhailopavliuk.controller.user.overview.LargeUserOverviewController;
import com.mykhailopavliuk.controller.user.overview.MediumUserOverviewController;
import com.mykhailopavliuk.controller.user.overview.SmallUserOverviewController;
import com.mykhailopavliuk.controller.user.settings.LargeUserSettingsController;
import com.mykhailopavliuk.controller.user.settings.MediumUserSettingsController;
import com.mykhailopavliuk.controller.user.settings.SmallUserSettingsController;
import com.mykhailopavliuk.controller.user.url.LargeUserUrlController;
import com.mykhailopavliuk.controller.user.url.MediumUserUrlController;
import com.mykhailopavliuk.controller.user.url.SmallUserUrlController;
import com.mykhailopavliuk.controller.user.urls.LargeUserUrlsController;
import com.mykhailopavliuk.controller.user.urls.MediumUserUrlsController;
import com.mykhailopavliuk.controller.user.urls.SmallUserUrlsController;
import com.mykhailopavliuk.service.SettingsService;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;

public class SceneHandler {

    public static void goToOverviewAdminScene(ActionEvent event, SettingsService settingsService, FxWeaver fxWeaver) {
        Stage stageTheEventSourceNodeBelongs = (Stage) ((Node) event.getSource()).getScene().getWindow();

        switch (settingsService.read().getScreenResolution()) {
            case SMALL:
                stageTheEventSourceNodeBelongs.setScene(new Scene(fxWeaver.loadView(SmallAdminOverviewController.class)));
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

    public static void goToSettingsAdminScene(ActionEvent event, SettingsService settingsService, FxWeaver fxWeaver) {
        Stage stageTheEventSourceNodeBelongs = (Stage) ((Node) event.getSource()).getScene().getWindow();

        switch (settingsService.read().getScreenResolution()) {
            case SMALL:
                stageTheEventSourceNodeBelongs.setScene(new Scene(fxWeaver.loadView(SmallAdminSettingsController.class)));
                break;

            case LARGE:
                stageTheEventSourceNodeBelongs.setScene(new Scene(fxWeaver.loadView(LargeAdminSettingsController.class)));
                break;

            default:
                stageTheEventSourceNodeBelongs.setScene(new Scene(fxWeaver.loadView(MediumAdminSettingsController.class)));
                break;
        }

        stageTheEventSourceNodeBelongs.centerOnScreen();

    }

    public static void goToUsersAdminScene(ActionEvent event, SettingsService settingsService, FxWeaver fxWeaver) {
        Stage stageTheEventSourceNodeBelongs = (Stage) ((Node) event.getSource()).getScene().getWindow();

        switch (settingsService.read().getScreenResolution()) {
            case SMALL:
                stageTheEventSourceNodeBelongs.setScene(new Scene(fxWeaver.loadView(SmallAdminUsersController.class)));
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

    public static void goToOverviewUserScene(ActionEvent event, SettingsService settingsService, FxWeaver fxWeaver) {
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

    public static void goToSettingsUserScene(ActionEvent event, SettingsService settingsService, FxWeaver fxWeaver) {
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

    public static void goToUrlsUserScene(Event event, SettingsService settingsService, FxWeaver fxWeaver) {
        Stage stageTheEventSourceNodeBelongs = (Stage) ((Node) event.getSource()).getScene().getWindow();

        switch (settingsService.read().getScreenResolution()) {
            case SMALL:
                stageTheEventSourceNodeBelongs.setScene(new Scene(fxWeaver.loadView(SmallUserUrlsController.class)));
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

    public static void goToUrlUserScene(Event event, SettingsService settingsService, FxWeaver fxWeaver) {
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

    }

    public static void goToSignInScene(ActionEvent event, SettingsService settingsService, FxWeaver fxWeaver) {
        Stage stageTheEventSourceNodeBelongs = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stageTheEventSourceNodeBelongs.setScene(new Scene(fxWeaver.loadView(SignInController.class)));
        stageTheEventSourceNodeBelongs.centerOnScreen();
    }

    public static void goToSignUpScene(ActionEvent event, SettingsService settingsService, FxWeaver fxWeaver) {
        Stage stageTheEventSourceNodeBelongs = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stageTheEventSourceNodeBelongs.setScene(new Scene(fxWeaver.loadView(SignUpController.class)));
        stageTheEventSourceNodeBelongs.centerOnScreen();
    }

}
