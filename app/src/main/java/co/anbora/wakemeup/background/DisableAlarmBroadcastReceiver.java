package co.anbora.wakemeup.background;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import co.anbora.wakemeup.Constants;
import co.anbora.wakemeup.device.vibration.Vibrations;

public class DisableAlarmBroadcastReceiver extends BroadcastReceiver {

    private Vibrations vibrations;

    public DisableAlarmBroadcastReceiver(Vibrations vibrations) {
        this.vibrations = vibrations;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Boolean disableAlarm = intent.getBooleanExtra(Constants.DISABLE_ALARM, false);
        if (disableAlarm) {
            this.vibrations.cancel();
        }
    }
}
