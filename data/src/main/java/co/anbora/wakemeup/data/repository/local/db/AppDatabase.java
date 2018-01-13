package co.anbora.wakemeup.data.repository.local.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.VisibleForTesting;

import co.anbora.wakemeup.data.repository.local.db.dao.AlarmGeofenceDao;
import co.anbora.wakemeup.data.repository.local.db.model.AlarmGeofenceEntity;

/**
 * Created by dalgarins.
 */
@Database(entities = {AlarmGeofenceEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    static final String DATABASE_NAME = "alarm-geofence-db";

    public abstract AlarmGeofenceDao alarmDao();

    /** The only instance */
    private static AppDatabase sInstance;

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
