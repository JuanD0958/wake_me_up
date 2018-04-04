package co.anbora.wakemeup.data.repository.model;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;

import co.anbora.wakemeup.domain.model.AlarmAndLastPoint;
import co.anbora.wakemeup.domain.model.AlarmGeofence;
import co.anbora.wakemeup.domain.model.HistoryAlarm;

@AutoValue
public abstract class AlarmAndLastPointModel implements AlarmAndLastPoint {

    @NonNull
    public abstract AlarmGeofence alarm();

    @NonNull
    public abstract HistoryAlarm lastPoint();

    @NonNull
    public static Builder builder() {
        return new AutoValue_AlarmAndLastPointModel.Builder();
    }

    @NonNull
    public static Builder builder(@NonNull AlarmGeofence alarm, HistoryAlarm lastPoint) {
        return builder()
                .alarm(alarm)
                .lastPoint(lastPoint);
    }

    @AutoValue.Builder
    public static abstract class Builder {

        @NonNull
        public abstract Builder alarm(@NonNull AlarmGeofence alarm);

        @NonNull
        public abstract Builder lastPoint(@NonNull HistoryAlarm lastPoint);

        @NonNull
        public abstract AlarmAndLastPointModel build();

    }

}
