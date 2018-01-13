package co.anbora.wakemeup.data;

import android.content.Context;

import java.util.function.Function;

import co.anbora.wakemeup.data.repository.AlarmGeofenceRepositoryImpl;
import co.anbora.wakemeup.data.repository.RepositoryImpl;
import co.anbora.wakemeup.data.repository.local.db.AppDatabase;
import co.anbora.wakemeup.data.repository.local.db.dao.AlarmGeofenceDao;
import co.anbora.wakemeup.data.repository.local.db.model.AlarmGeofenceEntity;
import co.anbora.wakemeup.data.repository.mapper.MapperToAlarmGeofence;
import co.anbora.wakemeup.domain.mapper.TwoWaysMapper;
import co.anbora.wakemeup.domain.model.AlarmGeofence;
import co.anbora.wakemeup.domain.repository.AlarmGeofenceRepository;
import co.anbora.wakemeup.domain.repository.Repository;

public class Injection {

    private Injection(){}

    private static class SINGLETON_HELPER {
        public static final TwoWaysMapper<AlarmGeofenceEntity, AlarmGeofence> MAPPER_TO_ALARM_GEOFENCE = new MapperToAlarmGeofence();
    }

    public static Repository provideRepository(Context context) {
        return new RepositoryImpl(provideAlarmGeofenceRepository(context));
    }

    private static AlarmGeofenceRepository provideAlarmGeofenceRepository(Context context) {
        return new AlarmGeofenceRepositoryImpl(provideAlarmGeofenceDao(context),
                                                SINGLETON_HELPER.MAPPER_TO_ALARM_GEOFENCE
                                                );
    }

    private static AlarmGeofenceDao provideAlarmGeofenceDao(Context context) {
        return AppDatabase.getInstance(context).alarmDao();
    }

}
