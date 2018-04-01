package co.anbora.wakemeup.device.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;

public class AlarmsImpl implements Alarms {

    private AlarmManager alarmManager;

    public AlarmsImpl(final Context context) {

        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    @Override
    public void create(PendingIntent pendingIntent) {

        alarmManager.set(AlarmManager.RTC_WAKEUP, 1, pendingIntent);
        //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime(), 60000, pendingIntent);
    }

    @Override
    public void cancel(PendingIntent pendingIntent) {

        alarmManager.cancel(pendingIntent);
    }
}
