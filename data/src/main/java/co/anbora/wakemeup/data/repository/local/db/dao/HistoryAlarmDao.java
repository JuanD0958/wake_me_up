package co.anbora.wakemeup.data.repository.local.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import co.anbora.wakemeup.data.repository.local.db.model.HistoryAlarmEntity;

/**
 * Created by dalgarins.
 */
@Dao
public interface HistoryAlarmDao {

    /**
     * Select all history alarms geofence.
     *
     * @return A {@link List} of all the history alarms in the table.
     */
    @Query("SELECT * FROM " + HistoryAlarmEntity.TABLE_NAME + " LIMIT 20 ")
    List<HistoryAlarmEntity> selectAll();

    /**
     * Select a alarm by the ID.
     *
     * @param alarmId The row ID for the table alarm.
     * @return A {@link HistoryAlarmEntity} of the selected history alarm.
     */
    @Query("SELECT * FROM " + HistoryAlarmEntity.TABLE_NAME
            + " WHERE " + HistoryAlarmEntity.COLUMN_ALARM_ID + " = :alarmId "
            + " ORDER BY " + HistoryAlarmEntity.COLUMN_CREATE_AT + " DESC LIMIT 1 ")
    HistoryAlarmEntity selectById(String alarmId);

    /**
     * Inserts a history alarm into the table.
     *
     * @param historyAlarm A new history alarm.
     * @return The row ID of the newly inserted history alarm.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(HistoryAlarmEntity historyAlarm);

}
