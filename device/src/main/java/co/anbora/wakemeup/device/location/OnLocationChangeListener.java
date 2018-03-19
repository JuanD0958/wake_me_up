package co.anbora.wakemeup.device.location;

import android.support.annotation.NonNull;

/**
 * Created by dalgarins on 03/05/18.
 */

public interface OnLocationChangeListener {

    OnLocationChangeListener onLocationChanged(@NonNull CallbackLocation callback);

    OnLocationRequest whenRequestLocation();

    OnObserveState attachState();

}
