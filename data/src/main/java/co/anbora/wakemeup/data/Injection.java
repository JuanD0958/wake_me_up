package co.anbora.wakemeup.data;

import android.content.Context;

import java.util.function.Function;

import co.anbora.wakemeup.data.repository.AlarmGeofenceRepositoryImpl;
import co.anbora.wakemeup.data.repository.HistoryAlarmRepositoryImpl;
import co.anbora.wakemeup.data.repository.RepositoryImpl;
import co.anbora.wakemeup.data.repository.local.db.AppDatabase;
import co.anbora.wakemeup.data.repository.local.db.dao.AlarmGeofenceDao;
import co.anbora.wakemeup.data.repository.local.db.dao.HistoryAlarmDao;
import co.anbora.wakemeup.data.repository.local.db.model.AlarmGeofenceEntity;
import co.anbora.wakemeup.data.repository.local.db.model.HistoryAlarmEntity;
import co.anbora.wakemeup.data.repository.mapper.MapperToAlarmGeofence;
import co.anbora.wakemeup.data.repository.mapper.MapperToHistoryAlarm;
import co.anbora.wakemeup.domain.mapper.TwoWaysMapper;
import co.anbora.wakemeup.domain.model.AlarmGeofence;
import co.anbora.wakemeup.domain.model.HistoryAlarm;
import co.anbora.wakemeup.domain.repository.AlarmGeofenceRepository;
import co.anbora.wakemeup.domain.repository.HistoryAlarmRepository;
import co.anbora.wakemeup.domain.repository.Repository;

class Injection {

    private Injection(){}

    private static class SINGLETON_HELPER {
        public static final TwoWaysMapper<AlarmGeofenceEntity, AlarmGeofence> MAPPER_TO_ALARM_GEOFENCE = new MapperToAlarmGeofence();
        public static final TwoWaysMapper<HistoryAlarmEntity, HistoryAlarm> MAPPER_TO_HISTORY_ALARM = new MapperToHistoryAlarm();
    }

    protected static Repository provideRepository(Context context) {
        return new RepositoryImpl(provideAlarmGeofenceRepository(context), provideHistoryAlarmRepository(context));
    }

    private static AlarmGeofenceRepository provideAlarmGeofenceRepository(Context context) {
        return new AlarmGeofenceRepositoryImpl(provideAlarmGeofenceDao(context),
                                                SINGLETON_HELPER.MAPPER_TO_ALARM_GEOFENCE
                                                );
    }

    private static AlarmGeofenceDao provideAlarmGeofenceDao(Context context) {
        return AppDatabase.getInstance(context).alarmDao();
    }

    private static HistoryAlarmRepository provideHistoryAlarmRepository(Context context) {
        return new HistoryAlarmRepositoryImpl(provideHistoryAlarmDao(context),
                SINGLETON_HELPER.MAPPER_TO_HISTORY_ALARM);
    }

    private static HistoryAlarmDao provideHistoryAlarmDao(Context context) {
        return AppDatabase.getInstance(context).historyDao();
    }

}
