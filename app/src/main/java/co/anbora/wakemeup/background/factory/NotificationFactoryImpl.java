package co.anbora.wakemeup.background.factory;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;

import co.anbora.wakemeup.R;
import co.anbora.wakemeup.ui.model.NotificationViewModel;

/**
 * Created by dalgarins on 03/24/18.
 */

public class NotificationFactoryImpl implements NotificationFactory {

    /**
     * The name of the channel for notifications.
     */
    private static final String CHANNEL_ID = "wake_me_up_channel_01";

    private final Context context;
    private final Resources resources;

    public NotificationFactoryImpl(final Context context, final Resources resources) {
        this.context = context;
        this.resources = resources;
    }

    @Override
    public Notification createForegroundServiceNotification(NotificationViewModel notification,
                                                            PendingIntent servicePendingIntent,
                                                            PendingIntent activityPendingIntent) {

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
                builder.addAction(R.drawable.ic_launch, resources.getString(R.string.launch_activity),
                        activityPendingIntent)
                .addAction(R.drawable.ic_cancel, resources.getString(R.string.remove_location_updates),
                        servicePendingIntent)
                .setContentText(notification.getContent())
                .setContentTitle(notification.getTitle())
                        .setContentIntent(activityPendingIntent)
                .setOngoing(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker(notification.getContent())
                .setWhen(System.currentTimeMillis());

        return builder.build();
    }

    @Override
    public Notification createActiveAlarmNotification(NotificationViewModel notification,
                                                      PendingIntent notificationPendingIntent) {

        // Get a notification builder that's compatible with platform versions >= 4
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);

        // Define the notification settings.
        builder.setSmallIcon(R.drawable.geopoint)
                // In a real app, you may want to use a library like Volley
                // to decode the Bitmap.
                .setLargeIcon(BitmapFactory.decodeResource(resources,
                        R.drawable.geopoint))
                .setColor(Color.RED)
                .setAutoCancel(true)
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getContent())
                .setContentIntent(notificationPendingIntent)
                .setPriority(Notification.PRIORITY_HIGH)
                .addAction(R.drawable.ic_launch, resources.getString(R.string.unable_alarm), notificationPendingIntent);

        return builder.build();
    }
}
