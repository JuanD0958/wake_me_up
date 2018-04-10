package co.anbora.wakemeup.background.shared.preferences;

import android.content.res.Resources;

import co.anbora.wakemeup.Constants;
import co.anbora.wakemeup.R;
import co.anbora.wakemeup.device.preference.Preferences;

/**
 * Created by dalgarins on 03/25/18.
 */

public class SharedPreferencesManagerImpl implements SharedPreferencesManager {

    private final Preferences preferences;
    private final Resources resources;

    public SharedPreferencesManagerImpl(Preferences preferences, Resources resources) {
        this.preferences = preferences;
        this.resources = resources;
    }

    @Override
    public boolean addedGeofence() {
        return this.preferences.getBoolean(Constants.GEOFENCES_ADDED_KEY);
    }

    @Override
    public void addedGeofence(boolean added) {
        this.preferences.setValue(Constants.GEOFENCES_ADDED_KEY, added);
    }

    @Override
    public boolean requestingLocationUpdates() {
        return this.preferences.getBoolean(Constants.KEY_REQUESTING_LOCATION_UPDATES);
    }

    @Override
    public void setRequestingLocationUpdates(boolean requestingLocationUpdates) {
        this.preferences.setValue(Constants.KEY_REQUESTING_LOCATION_UPDATES, requestingLocationUpdates);
    }

    @Override
    public boolean activeAlarm() {
        return this.preferences.getBoolean(Constants.ACTIVE_ALARM);
    }

    @Override
    public void setActiveAlarm(boolean activeAlarm) {
        this.preferences.setValue(Constants.ACTIVE_ALARM, activeAlarm);
    }

    @Override
    public boolean notifyAlarm() {
        return this.preferences.getBoolean(this.resources.getString(R.string.pref_notification_key));
    }

    @Override
    public boolean vibrateAlarm() {
        return this.preferences.getBoolean(this.resources.getString(R.string.pref_notification_vibrate_key));
    }

    @Override
    public String nameUser() {
        return this.preferences.getString(this.resources.getString(R.string.pref_general_name_key));
    }

    @Override
    public String emailUser() {
        return this.preferences.getString(this.resources.getString(R.string.pref_general_email_key));
    }

    @Override
    public long metersAlarmRadio() {
        return this.preferences.getLong(this.resources.getString(R.string.pref_alarm_area_key));
    }

    @Override
    public long timeUpdateGps() {
        return this.preferences.getLong(this.resources.getString(R.string.pref_alarm_time_key));
    }
}
