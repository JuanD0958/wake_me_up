package co.anbora.wakemeup.data.repository.mapper;

import co.anbora.wakemeup.data.repository.local.db.model.AlarmGeofenceEntity;
import co.anbora.wakemeup.data.repository.model.AlarmGeofenceModel;
import co.anbora.wakemeup.domain.mapper.TwoWaysMapper;
import co.anbora.wakemeup.domain.model.AlarmGeofence;

/**
 * Created by dalgarins.
 */
public class MapperToAlarmGeofence implements TwoWaysMapper<AlarmGeofenceEntity, AlarmGeofence> {

    @Override
    public AlarmGeofence apply(AlarmGeofenceEntity alarmGeofenceEntity) {
        if (alarmGeofenceEntity == null) {
            return null;
        }
        return AlarmGeofenceModel.builder()
                .internalId(alarmGeofenceEntity.getId())
                .remoteId(alarmGeofenceEntity.getRemoteId())
                .id(alarmGeofenceEntity.getAlarmId())
                .name(alarmGeofenceEntity.getName())
                .description(alarmGeofenceEntity.getDescription())
                .state(alarmGeofenceEntity.getState())
                .createdAt(alarmGeofenceEntity.getCreatedAt())
                .deletedAt(alarmGeofenceEntity.getDeletedAt())
                .updatedAt(alarmGeofenceEntity.getUpdatedAt())
                .needsSync(alarmGeofenceEntity.getNeedSync())
                .build();
    }

    @Override
    public AlarmGeofenceEntity inverseMap(AlarmGeofence map) {
        if (map == null) {
            return null;
        }
        AlarmGeofenceEntity alarm = new AlarmGeofenceEntity();
        alarm.setId(map.internalId());
        alarm.setRemoteId(map.remoteId());
        alarm.setAlarmId(map.id());
        alarm.setName(map.name());
        alarm.setDescription(map.description());
        alarm.setState(map.state());
        alarm.setCreatedAt(map.createdAt());
        alarm.setDeletedAt(map.deletedAt());
        alarm.setUpdatedAt(map.updatedAt());
        alarm.setNeedSync(map.needsSync());
        return alarm;
    }
}
