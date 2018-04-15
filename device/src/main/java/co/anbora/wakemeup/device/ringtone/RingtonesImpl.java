package co.anbora.wakemeup.device.ringtone;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

import java.io.IOException;

public class RingtonesImpl implements Ringtones {

    private Ringtone ringtone;

    public RingtonesImpl(final Context context, final Uri alarmUri) {
        ringtone = RingtoneManager.getRingtone(context, alarmUri);
    }

    @Override
    public void play() {

        ringtone.play();
    }

    @Override
    public void cancel() {

        ringtone.stop();
    }
}
