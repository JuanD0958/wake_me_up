package co.anbora.wakemeup.data.repository;

import java.util.List;
import java.util.stream.Collectors;

import co.anbora.wakemeup.data.repository.local.db.dao.AlarmGeofenceDao;
import co.anbora.wakemeup.data.repository.local.db.model.AlarmGeofenceEntity;
import co.anbora.wakemeup.domain.mapper.TwoWaysMapper;
import co.anbora.wakemeup.domain.model.AlarmGeofence;
import co.anbora.wakemeup.domain.repository.AlarmGeofenceRepository;

public class AlarmGeofenceRepositoryImpl implements AlarmGeofenceRepository {

    private AlarmGeofenceDao alarmDao;
    private TwoWaysMapper<AlarmGeofenceEntity, AlarmGeofence> mapperToAlarmGeofence;

    public AlarmGeofenceRepositoryImpl(AlarmGeofenceDao alarmDao,
                                       TwoWaysMapper<AlarmGeofenceEntity, AlarmGeofence> mapperToAlarmGeofence) {
        this.alarmDao = alarmDao;
        this.mapperToAlarmGeofence = mapperToAlarmGeofence;
    }

    @Override
    public void getAlarms(LoadAlarmsCallback callback) {

        List<AlarmGeofence> alarms = alarmDao.selectAll()
                .stream()
                .map(mapperToAlarmGeofence).collect(Collectors.<AlarmGeofence>toList());

        callback.onAlarmsLoaded(alarms);
    }

    @Override
    public void saveAlarm(AlarmGeofence alarm) {
        this.alarmDao.insert(mapperToAlarmGeofence.inverseMap(alarm));
    }

    @Override
    public void deleteAlarm(AlarmGeofence alarm) {
        this.alarmDao.delete(alarm.internalId());
    }

    @Override
    public void updateAlarm(AlarmGeofence alarm, Boolean state) {

        AlarmGeofenceEntity alarmEntity = mapperToAlarmGeofence.inverseMap(alarm);
        alarmEntity.setState(state);
        this.alarmDao.insert(alarmEntity);
    }
}
