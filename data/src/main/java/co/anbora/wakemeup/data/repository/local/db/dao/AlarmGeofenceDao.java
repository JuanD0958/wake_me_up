package co.anbora.wakemeup.data.repository.local.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import co.anbora.wakemeup.data.repository.local.db.model.AlarmGeofenceEntity;

/**
 * Created by dalgarins.
 */
@Dao
public interface AlarmGeofenceDao {

    /**
     * Select all alarms geofence.
     *
     * @return A {@link List} of all the alarms in the table.
     */
    @Query("SELECT * FROM " + AlarmGeofenceEntity.TABLE_NAME + " WHERE " + AlarmGeofenceEntity.COLUMN_VISIBLE + " = 1 ")
    List<AlarmGeofenceEntity> selectAll();

    /**
     * Select a alarm by the ID.
     *
     * @param alarmId The row ID.
     * @return A {@link AlarmGeofenceEntity} of the selected alarm.
     */
    @Query("SELECT * FROM " + AlarmGeofenceEntity.TABLE_NAME + " WHERE " + AlarmGeofenceEntity.COLUMN_ALARM_ID + " = :alarmId")
    AlarmGeofenceEntity selectById(String alarmId);

    /**
     * Inserts a alarm into the table.
     *
     * @param alarm A new alarm.
     * @return The row ID of the newly inserted alarm.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(AlarmGeofenceEntity alarm);

    /**
     * Delete a alarm by the ID
     *
     * @return the number of alarm deleted
     */
    @Query("UPDATE " + AlarmGeofenceEntity.TABLE_NAME + " SET " + AlarmGeofenceEntity.COLUMN_VISIBLE + " = 0 " + " WHERE " + AlarmGeofenceEntity.COLUMN_INTERNAL_ID + " = :internalId")
    int delete(long internalId);

}
