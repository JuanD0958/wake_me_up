package co.anbora.wakemeup.device.vibration;

import android.os.VibrationEffect;

/**
 * Created by dalgarins on 02/10/18.
 */

public interface Vibrations {

    void vibrate(VibrationEffect vibrationEffect);

    void vibrate(int milliseconds);

    void vibrate(long[] pattern);
}
