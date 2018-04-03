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

    interface GetAlarmCallback {

        void onAlarmLoaded(AlarmGeofence task);

        void onDataNotAvailable();
    }

    void getAlarm(String alarmId, GetAlarmCallback callback);

    void getAlarms(LoadAlarmsCallback callback);

    void saveAlarm(AlarmGeofence alarm);

    void deleteAlarm(AlarmGeofence alarm);

    void updateAlarm(AlarmGeofence alarm, Boolean state);

}
