package co.anbora.wakemeup.device.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

/**
 * Created by dalgarins on 03/10/18.
 */

public class UtilPermission {

    private UtilPermission(){
    }

    /**
     * Check location permission
     * @return
     */
    public static boolean checkLocationPermission(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

}
