package co.anbora.wakemeup.background.factory;

import android.app.Notification;
import android.app.PendingIntent;

import co.anbora.wakemeup.ui.model.NotificationViewModel;

/**
 * Created by dalgarins on 03/22/18.
 */

public interface NotificationFactory {

    Notification createForegroundServiceNotification(NotificationViewModel notification,
                                                     PendingIntent servicePendingIntent,
                                                     PendingIntent activityPendingIntent);

    Notification createActiveAlarmNotification(NotificationViewModel notification,
                                               PendingIntent notificationPendingIntent);

}
