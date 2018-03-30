package co.anbora.wakemeup.data.repository.local.db.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.provider.BaseColumns;

import co.anbora.wakemeup.domain.model.HistoryAlarm;

/**
 * Created by dalgarins.
 */
@Entity(tableName = HistoryAlarmEntity.TABLE_NAME, indices = {
        @Index(value = HistoryAlarmEntity.COLUMN_HISTORY_ID, unique = true),
        @Index(value = HistoryAlarmEntity.COLUMN_ALARM_ID)
}, foreignKeys = @ForeignKey(entity = AlarmGeofenceEntity.class,
        parentColumns = AlarmGeofenceEntity.COLUMN_ALARM_ID,
        childColumns = HistoryAlarmEntity.COLUMN_ALARM_ID,
        onDelete = ForeignKey.CASCADE
))
public class HistoryAlarmEntity {

    public static final String TABLE_NAME = "history_alarms";
    public static final String COLUMN_INTERNAL_ID = BaseColumns._ID;
    public static final String COLUMN_REMOTE_ID = "id_remote";
    public static final String COLUMN_HISTORY_ID = "id_history";
    public static final String COLUMN_ALARM_ID = "id_alarm";
    public static final String COLUMN_CREATE_AT = "create_at";
    public static final String COLUMN_DELETE_AT = "delete_at";
    public static final String COLUMN_UPDATE_AT = "update_at";
    public static final String COLUMN_NEED_SYNC = "need_sync";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = COLUMN_INTERNAL_ID)
    private Long internalId;

    @ColumnInfo(name = COLUMN_REMOTE_ID)
    private Long remoteId;

    @ColumnInfo(name = COLUMN_HISTORY_ID)
    private String historyId;

    @ColumnInfo(name = COLUMN_ALARM_ID)
    private String alarmId;

    @ColumnInfo(name = COLUMN_CREATE_AT)
    private Long createdAt;

    @ColumnInfo(name = COLUMN_DELETE_AT)
    private Long deletedAt;

    @ColumnInfo(name = COLUMN_UPDATE_AT)
    private Long updatedAt;

    @ColumnInfo(name = COLUMN_NEED_SYNC)
    private Boolean needSync;

    public Long getInternalId() {
        return internalId;
    }

    public void setInternalId(Long id) {
        this.internalId = id;
    }

    public Long getRemoteId() {
        return remoteId;
    }

    public void setRemoteId(Long remoteId) {
        this.remoteId = remoteId;
    }

    public String getHistoryId() {
        return historyId;
    }

    public void setHistoryId(String historyId) {
        this.historyId = historyId;
    }

    public String getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(String alarmId) {
        this.alarmId = alarmId;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Long deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getNeedSync() {
        return needSync;
    }

    public void setNeedSync(Boolean needSync) {
        this.needSync = needSync;
    }
}
