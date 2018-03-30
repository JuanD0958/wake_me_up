package co.anbora.wakemeup.data.repository.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

import co.anbora.wakemeup.domain.model.HistoryAlarm;

/**
 * Created by dalgarins.
 */
@AutoValue
public abstract class HistoryAlarmModel implements HistoryAlarm {

    @Nullable
    public abstract Long internalId();

    @Nullable
    public abstract Long remoteId();

    @NonNull
    public abstract String id();

    @NonNull
    public abstract String alarmId();

    @NonNull
    public abstract Long createdAt();

    @Nullable
    public abstract Long deletedAt();

    @Nullable
    public abstract Long updatedAt();

    public abstract Boolean needSync();

    @NonNull
    public static Builder builder() {
        return new AutoValue_HistoryAlarmModel.Builder();
    }

    @NonNull
    public static Builder builder(@NonNull HistoryAlarm historyAlarm) {
        return builder()
                .internalId(historyAlarm.internalId())
                .remoteId(historyAlarm.remoteId())
                .id(historyAlarm.id())
                .alarmId(historyAlarm.alarmId())
                .createdAt(historyAlarm.createdAt())
                .updatedAt(historyAlarm.updatedAt())
                .deletedAt(historyAlarm.deletedAt())
                .needSync(historyAlarm.needSync());
    }

    public boolean isNew() {
        return remoteId() == null;
    }

    public boolean isDeleted() {
        return deletedAt() != null;
    }

    public boolean isStoredLocally() {
        return internalId() != null;
    }

    @AutoValue.Builder
    public abstract static class Builder {

        @NonNull
        public abstract Builder internalId(@NonNull Long internalId);

        @NonNull
        public abstract Builder remoteId(@NonNull Long remoteId);

        @NonNull
        public abstract Builder id(@NonNull String state);

        @NonNull
        public abstract Builder alarmId(@NonNull String alarmId);

        @NonNull
        public abstract Builder createdAt(@NonNull Long createdAt);

        @NonNull
        public abstract Builder updatedAt(@Nullable Long updatedAt);

        @NonNull
        public abstract Builder deletedAt(@Nullable Long deletedAt);

        @NonNull
        public abstract Builder needSync(@Nullable Boolean needsSync);

        @NonNull
        public abstract HistoryAlarmModel build();

    }
}
