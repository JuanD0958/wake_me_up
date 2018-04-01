package co.anbora.wakemeup.device.alarm;

import android.app.PendingIntent;

public interface Alarms {

    void create(PendingIntent pendingIntent);

    void cancel(PendingIntent pendingIntent);

}
