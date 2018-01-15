package co.anbora.wakemeup.data.repository;

import co.anbora.wakemeup.domain.model.AlarmGeofence;
import co.anbora.wakemeup.domain.repository.AlarmGeofenceRepository;
import co.anbora.wakemeup.domain.repository.Repository;

public class RepositoryImpl implements Repository {

    private AlarmGeofenceRepository alarmRepository;

    public RepositoryImpl(AlarmGeofenceRepository alarmRepository) {

        this.alarmRepository = alarmRepository;
    }

    @Override
    public void getAlarms(LoadAlarmsCallback callback) {

        this.alarmRepository.getAlarms(callback);
    }

    @Override
    public void saveAlarm(AlarmGeofence alarm) {

        this.alarmRepository.saveAlarm(alarm);
    }

    @Override
    public void deleteAlarm(AlarmGeofence alarm) {

        this.alarmRepository.deleteAlarm(alarm);
    }

    @Override
    public void updateAlarm(AlarmGeofence alarm, Boolean state) {

        this.alarmRepository.updateAlarm(alarm, state);
    }
}
