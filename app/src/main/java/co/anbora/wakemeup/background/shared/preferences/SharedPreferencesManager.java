package co.anbora.wakemeup.background.shared.preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by dalgarins on 03/25/18.
 */

public interface SharedPreferencesManager {

    /**
     * Returns true if geofences were added, otherwise false.
     */
    boolean addedGeofence();

    /**
     * Stores whether geofences were added ore removed in {@link SharedPreferences};
     *
     * @param added Whether geofences were added or removed.
     */
    void addedGeofence(boolean added);

    /**
     * Returns true if requesting location updates, otherwise returns false.
     */
    boolean requestingLocationUpdates();

    /**
     * Stores the location updates state in SharedPreferences.
     * @param requestingLocationUpdates The location updates state.
     */
    void setRequestingLocationUpdates(boolean requestingLocationUpdates);

}
