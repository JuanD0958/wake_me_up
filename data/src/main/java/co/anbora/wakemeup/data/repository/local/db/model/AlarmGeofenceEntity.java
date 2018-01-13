package co.anbora.wakemeup.data.repository.local.db.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.provider.BaseColumns;

/**
 * Created by dalgarins.
 */
@Entity(tableName = AlarmGeofenceEntity.TABLE_NAME, indices = {
        @Index(value = AlarmGeofenceEntity.COLUMN_ALARM_ID, unique = true)
})
public class AlarmGeofenceEntity {

    public static final String TABLE_NAME = "alarms";
    public static final String COLUMN_INTERNAL_ID = BaseColumns._ID;
    public static final String COLUMN_REMOTE_ID = "id_remote";
    public static final String COLUMN_ALARM_ID = "id_alarm";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_STATE = "state";
    public static final String COLUMN_CREATE_AT = "create_at";
    public static final String COLUMN_DELETE_AT = "delete_at";
    public static final String COLUMN_UPDATE_AT = "update_at";
    public static final String COLUMN_NEED_SYNC = "need_sync";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = COLUMN_INTERNAL_ID)
    private Long id;

    @ColumnInfo(name = COLUMN_REMOTE_ID)
    private Long remoteId;

    @ColumnInfo(name = COLUMN_ALARM_ID)
    private String alarmId;

    @ColumnInfo(name = COLUMN_NAME)
    private String name;

    @ColumnInfo(name = COLUMN_DESCRIPTION)
    private String description;

    @ColumnInfo(name = COLUMN_STATE)
    private Boolean state;

    @ColumnInfo(name = COLUMN_CREATE_AT)
    private Long createdAt;

    @ColumnInfo(name = COLUMN_DELETE_AT)
    private Long deletedAt;

    @ColumnInfo(name = COLUMN_UPDATE_AT)
    private Long updatedAt;

    @ColumnInfo(name = COLUMN_NEED_SYNC)
    private Boolean needSync;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRemoteId() {
        return remoteId;
    }

    public void setRemoteId(Long remoteId) {
        this.remoteId = remoteId;
    }

    public String getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(String alarmId) {
        this.alarmId = alarmId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
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
