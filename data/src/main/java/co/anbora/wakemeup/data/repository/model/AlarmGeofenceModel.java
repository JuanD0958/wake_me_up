package co.anbora.wakemeup.data.repository.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

import co.anbora.wakemeup.domain.model.AlarmGeofence;


/**
 * Created by dalgarins.
 */
@AutoValue
public abstract class AlarmGeofenceModel implements AlarmGeofence {

    @Nullable
    public abstract Long internalId();

    @Nullable
    public abstract Long remoteId();

    @Nullable
    public abstract String id();

    @NonNull
    public abstract String name();

    @NonNull
    public abstract String description();

    @NonNull
    public abstract Double latitude();

    @NonNull
    public abstract Double longitude();

    @NonNull
    public abstract Boolean state();

    @NonNull
    public abstract Long createdAt();

    @Nullable
    public abstract Long updatedAt();

    @Nullable
    public abstract Long deletedAt();

    public abstract Boolean needsSync();

    @NonNull
    public static Builder builder() {
        return new AutoValue_AlarmGeofenceModel.Builder();
    }

    @NonNull
    public static Builder builder(@NonNull AlarmGeofence alarm) {
        return builder()
                .internalId(alarm.internalId())
                .remoteId(alarm.remoteId())
                .id(alarm.id())
                .name(alarm.name())
                .description(alarm.description())
                .latitude(alarm.latitude())
                .longitude(alarm.longitude())
                .state(alarm.state())
                .createdAt(alarm.createdAt())
                .updatedAt(alarm.updatedAt())
                .deletedAt(alarm.deletedAt())
                .needsSync(alarm.needsSync());
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
    public static abstract class Builder {

        @NonNull
        public abstract Builder internalId(@NonNull Long internalId);

        @NonNull
        public abstract Builder remoteId(@NonNull Long remoteId);

        @NonNull
        public abstract Builder id(@NonNull String state);

        @NonNull
        public abstract Builder name(@NonNull String state);

        @NonNull
        public abstract Builder description(@NonNull String state);

        @NonNull
        public abstract Builder latitude(@NonNull Double latitude);

        @NonNull
        public abstract Builder longitude(@NonNull Double longitude);

        @NonNull
        public abstract Builder state(@NonNull Boolean state);

        @NonNull
        public abstract Builder createdAt(@NonNull Long createdAt);

        @NonNull
        public abstract Builder updatedAt(@Nullable Long updatedAt);

        @NonNull
        public abstract Builder deletedAt(@Nullable Long deletedAt);

        @NonNull
        public abstract Builder needsSync(@Nullable Boolean needsSync);

        @NonNull
        public abstract AlarmGeofenceModel build();

    }

}
