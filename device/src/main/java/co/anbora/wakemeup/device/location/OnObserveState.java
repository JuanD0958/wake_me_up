package co.anbora.wakemeup.device.location;

import android.arch.lifecycle.Lifecycle;
import android.support.annotation.NonNull;

/**
 * Created by dalgarins on 03/09/18.
 */

public interface OnObserveState {

    void observe(@NonNull Lifecycle lifecycle);

}
