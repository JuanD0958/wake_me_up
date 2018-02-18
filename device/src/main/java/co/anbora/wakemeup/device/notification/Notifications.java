package co.anbora.wakemeup.device.notification;

import android.app.Notification;
import android.app.NotificationChannel;

public interface Notifications {

    void showNotification(int notificationId, NotificationChannel notification);

    void showNotification(int notificationId, Notification notification);

    void updateNotification(int notificationId, Notification notification);

    void hideNotification(int notificationId);

    void hideNotifications();
}
