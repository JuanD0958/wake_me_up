package co.anbora.wakemeup.data.repository.local.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.VisibleForTesting;

import co.anbora.wakemeup.data.repository.local.db.dao.AlarmGeofenceDao;
import co.anbora.wakemeup.data.repository.local.db.dao.HistoryAlarmDao;
import co.anbora.wakemeup.data.repository.local.db.model.AlarmGeofenceEntity;
import co.anbora.wakemeup.data.repository.local.db.model.HistoryAlarmEntity;

/**
 * Created by dalgarins.
 */
@Database(entities = {AlarmGeofenceEntity.class, HistoryAlarmEntity.class}, version = 4, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    static final String DATABASE_NAME = "alarm-geofence-db";

    public abstract AlarmGeofenceDao alarmDao();

    public abstract HistoryAlarmDao historyDao();

    /** The only instance */
    private static AppDatabase sInstance;

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE " + AlarmGeofenceEntity.TABLE_NAME
                    + " ADD COLUMN " + AlarmGeofenceEntity.COLUMN_VISIBLE +
                    " INTEGER");
        }
    };

    /**
     * Gets the singleton instance of AppDatabase.
     *
     * @param context The context.
     * @return The singleton instance of AppDatabase.
     */
    public static synchronized AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            sInstance = Room
                    .databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME)
                    //.addMigrations(MIGRATION_1_2)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return sInstance;
    }

    /**
     * Switches the internal implementation with an empty in-memory database.
     *
     * @param context The context.
     */
    @VisibleForTesting
    public static void switchToInMemory(Context context) {
        sInstance = Room.inMemoryDatabaseBuilder(context.getApplicationContext(),
                AppDatabase.class).build();
    }

}
