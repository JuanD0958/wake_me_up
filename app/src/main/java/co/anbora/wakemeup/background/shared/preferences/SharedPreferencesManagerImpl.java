package co.anbora.wakemeup.background.shared.preferences;

import co.anbora.wakemeup.Constants;
import co.anbora.wakemeup.device.preference.Preferences;

/**
 * Created by dalgarins on 03/25/18.
 */

public class SharedPreferencesManagerImpl implements SharedPreferencesManager {

    private final Preferences preferences;

    public SharedPreferencesManagerImpl(Preferences preferences) {
        this.preferences = preferences;
    }


    @Override
    public boolean addedGeofence() {
        return this.preferences.getValue(Constants.GEOFENCES_ADDED_KEY);
    }

    @Override
    public void addedGeofence(boolean added) {
        this.preferences.setValue(Constants.GEOFENCES_ADDED_KEY, added);
    }

    @Override
    public boolean requestingLocationUpdates() {
        return this.preferences.getValue(Constants.KEY_REQUESTING_LOCATION_UPDATES);
    }

    @Override
    public void setRequestingLocationUpdates(boolean requestingLocationUpdates) {
        this.preferences.setValue(Constants.KEY_REQUESTING_LOCATION_UPDATES, requestingLocationUpdates);
    }

    @Override
    public boolean activeAlarm() {
        return this.preferences.getValue(Constants.ACTIVE_ALARM);
    }

    @Override
    public void setActiveAlarm(boolean activeAlarm) {
        this.preferences.setValue(Constants.ACTIVE_ALARM, activeAlarm);
    }
}
