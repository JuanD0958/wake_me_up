package co.anbora.wakemeup.device.location;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * Created by dalgarins on 02/03/18.
 */

public class LocationUpdateListener implements LocationListener {

    private Callback callback;

    public LocationUpdateListener(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void onLocationChanged(Location location) {
        this.callback.execute(location);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
