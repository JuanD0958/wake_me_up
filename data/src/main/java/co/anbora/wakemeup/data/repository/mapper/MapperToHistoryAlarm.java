package co.anbora.wakemeup.data.repository.mapper;

import co.anbora.wakemeup.data.repository.local.db.model.HistoryAlarmEntity;
import co.anbora.wakemeup.data.repository.model.HistoryAlarmModel;
import co.anbora.wakemeup.domain.mapper.TwoWaysMapper;
import co.anbora.wakemeup.domain.model.HistoryAlarm;

/**
 * Created by dalgarins.
 */
public class MapperToHistoryAlarm implements TwoWaysMapper<HistoryAlarmEntity, HistoryAlarm> {

    @Override
    public HistoryAlarmEntity inverseMap(HistoryAlarm map) {
        if (map == null) {
            return null;
        }
        HistoryAlarmEntity entity = new HistoryAlarmEntity();
        entity.setInternalId(map.internalId());
        entity.setRemoteId(map.remoteId());
        entity.setHistoryId(map.id());
        entity.setLatitude(map.latitude());
        entity.setLongitude(map.longitude());
        entity.setAlarmId(map.alarmId());
        entity.setCreatedAt(map.createdAt());
        entity.setDeletedAt(map.deletedAt());
        entity.setUpdatedAt(map.updatedAt());
        entity.setNeedSync(map.needSync());
        return entity;
    }

    @Override
    public HistoryAlarm apply(HistoryAlarmEntity historyAlarmEntity) {
        if (historyAlarmEntity == null) {
            return null;
        }
        return HistoryAlarmModel.builder()
                .internalId(historyAlarmEntity.getInternalId())
                .remoteId(historyAlarmEntity.getRemoteId())
                .id(historyAlarmEntity.getHistoryId())
                .latitude(historyAlarmEntity.getLatitude())
                .longitude(historyAlarmEntity.getLongitude())
                .alarmId(historyAlarmEntity.getAlarmId())
                .createdAt(historyAlarmEntity.getCreatedAt())
                .deletedAt(historyAlarmEntity.getDeletedAt())
                .updatedAt(historyAlarmEntity.getUpdatedAt())
                .needSync(historyAlarmEntity.getNeedSync())
                .build();
    }
}
