package co.anbora.wakemeup.background;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import co.anbora.wakemeup.Constants;
import co.anbora.wakemeup.Injection;

public class DisableAlarmBroadCastReceiver extends BroadcastReceiver {

    public DisableAlarmBroadCastReceiver(){
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Boolean disableAlarm = intent.getBooleanExtra(Constants.DISABLE_ALARM, false);
        Notification notification = intent.getParcelableExtra(Constants.NOTIFICATION);
        if (disableAlarm && notification != null) {
            Injection.provideNotificationManager(context)
                    .showNotification(Constants.NOTIFICATION_ALARM_ACTIVE_ID, notification);
        }
    }
}
