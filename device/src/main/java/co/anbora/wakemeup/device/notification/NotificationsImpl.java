package co.anbora.wakemeup.device.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.app.NotificationManager;


public final class NotificationsImpl implements Notifications {

    private final NotificationManager notificationManagerCompat;

    public NotificationsImpl(@NonNull final Context context) {
        this.notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);;
    }

    @Override
    public void showNotification(int notificationId, NotificationChannel notification) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManagerCompat.createNotificationChannel(notification);
        }
    }

    @Override
    public void showNotification(final int notificationId, final Notification notification) {
        notificationManagerCompat.notify(notificationId, notification);
    }

    @Override
    public void updateNotification(final int notificationId, final Notification notification) {
        notificationManagerCompat.notify(notificationId, notification);
    }

    @Override
    public void hideNotification(final int notificationId) {
        notificationManagerCompat.cancel(notificationId);
    }

    @Override
    public void hideNotifications() {
        notificationManagerCompat.cancelAll();
    }
}
