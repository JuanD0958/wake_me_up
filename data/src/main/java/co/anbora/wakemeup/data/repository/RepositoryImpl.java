package co.anbora.wakemeup.data.repository;

import co.anbora.wakemeup.domain.model.AlarmGeofence;
import co.anbora.wakemeup.domain.model.HistoryAlarm;
import co.anbora.wakemeup.domain.repository.AlarmGeofenceRepository;
import co.anbora.wakemeup.domain.repository.HistoryAlarmRepository;
import co.anbora.wakemeup.domain.repository.Repository;

public class RepositoryImpl implements Repository {

    private AlarmGeofenceRepository alarmRepository;
    private HistoryAlarmRepository historyAlarmRepository;

    public RepositoryImpl(AlarmGeofenceRepository alarmRepository, HistoryAlarmRepository historyAlarmRepository) {

        this.alarmRepository = alarmRepository;
        this.historyAlarmRepository = historyAlarmRepository;
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

    @Override
    public void getHistoryAlarms(LoadHistoryAlarmsCallback callback) {

        this.historyAlarmRepository.getHistoryAlarms(callback);
    }

    @Override
    public void getHistoryAlarm(Long internalId, GetHistoryAlarmCallback callback) {

        this.historyAlarmRepository.getHistoryAlarm(internalId, callback);
    }

    @Override
    public void saveHistoryAlarm(HistoryAlarm historyAlarm) {

        this.historyAlarmRepository.saveHistoryAlarm(historyAlarm);
    }
}
