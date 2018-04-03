package co.anbora.wakemeup.mapper;

import java.util.Date;
import java.util.UUID;

import co.anbora.wakemeup.data.repository.model.HistoryAlarmModel;
import co.anbora.wakemeup.domain.mapper.Mapper;
import co.anbora.wakemeup.domain.model.AlarmGeofence;
import co.anbora.wakemeup.domain.model.HistoryAlarm;

public class AlarmToHistoryMapper implements Mapper<AlarmGeofence, HistoryAlarm> {

    @Override
    public HistoryAlarm apply(AlarmGeofence alarmGeofence) {
        if (alarmGeofence == null) {
            return null;
        }
        return HistoryAlarmModel.builder()
                .id(UUID.randomUUID().toString())
                .alarmId(alarmGeofence.id())
                .latitude(alarmGeofence.latitude())
                .longitude(alarmGeofence.longitude())
                .createdAt(new Date().getTime())
                .needSync(true)
                .build();
    }
}
