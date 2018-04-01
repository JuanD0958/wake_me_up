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

    /**
     * Returns true if a alarm was active, otherwise false.
     */
    boolean activeAlarm();

    /**
     * Store the active alarm in SharedPreferences
     * @param activeAlarm the alarm active
     */
    void setActiveAlarm(boolean activeAlarm);

}
