package com.mykhailopavliuk.util;

import com.github.plushaze.traynotification.animations.Animations;
import com.github.plushaze.traynotification.notification.Notification;
import com.github.plushaze.traynotification.notification.TrayNotification;
import javafx.scene.paint.Paint;
import javafx.util.Duration;

public final class TrayHandler {
    private static final TrayNotification trayNotification = new TrayNotification();

    private TrayHandler() {
    }

    public static void notify(
            String title,
            String message,
            Notification notification,
            Animations animation,
            Paint paint,
            Duration duration) {

        trayNotification.setTitle(title);
        trayNotification.setMessage(message);
        trayNotification.setNotification(notification);
        trayNotification.setAnimation(animation);
        trayNotification.setRectangleFill(paint);
        trayNotification.showAndDismiss(duration);
    }

}
