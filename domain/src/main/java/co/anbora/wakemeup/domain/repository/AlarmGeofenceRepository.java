package co.anbora.wakemeup.domain.repository;

import java.util.List;

import co.anbora.wakemeup.domain.model.AlarmGeofence;

/**
 * Created by dalgarins.
 */
public interface AlarmGeofenceRepository {

    interface LoadAlarmsCallback {

        void onAlarmsLoaded(List<AlarmGeofence> alarms);

        void onDataNotAvailable();
    }

    void getAlarms(LoadAlarmsCallback callback);

    void saveAlarm(AlarmGeofence alarm);

    void deleteAlarm(AlarmGeofence alarm);

    void updateAlarm(AlarmGeofence alarm, Boolean state);

}
