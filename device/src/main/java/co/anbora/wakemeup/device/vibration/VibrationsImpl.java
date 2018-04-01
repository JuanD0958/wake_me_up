package co.anbora.wakemeup.device.vibration;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

/**
 * Created by dalgarins on 02/10/18.
 */

public class VibrationsImpl implements Vibrations {

    private static final int REPEAT = 0;

    private Vibrator vibrator;

    public VibrationsImpl(Context context) {
        this.vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }


    @Override
    public void vibrate(VibrationEffect vibrationEffect) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.vibrator.vibrate(vibrationEffect);
        }
    }

    @Override
    public void vibrate(int milliseconds) {
        this.vibrator.vibrate(milliseconds);
    }

    @Override
    public void vibrate(long[] pattern) {
        this.vibrator.vibrate(pattern, REPEAT);
    }

    @Override
    public void cancel() {
        this.vibrator.cancel();
    }
}
