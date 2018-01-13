package co.anbora.wakemeup.domain.usecase.alarm;

import co.anbora.wakemeup.domain.model.AlarmGeofence;
import co.anbora.wakemeup.domain.repository.AlarmGeofenceRepository;
import co.anbora.wakemeup.domain.usecase.UseCase;

public class AddAlarm extends UseCase<AddAlarm.RequestValues, AddAlarm.ResponseValues> {

    private final AlarmGeofenceRepository repository;

    public AddAlarm(AlarmGeofenceRepository repository) {
        this.repository = repository;
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {

        AlarmGeofence alarm = requestValues.getAlarm();
        this.repository.saveAlarm(alarm);

        getUseCaseCallback().onSuccess(new ResponseValues(alarm));
    }

    public static final class RequestValues implements UseCase.RequestValues {

        private final AlarmGeofence alarm;

        public RequestValues(AlarmGeofence alarm) {
            this.alarm = alarm;
        }

        public AlarmGeofence getAlarm() {
            return this.alarm;
        }
    }

    public static final class ResponseValues implements UseCase.ResponseValue {

        private final AlarmGeofence alarm;

        public ResponseValues(AlarmGeofence alarm) {
            this.alarm = alarm;
        }

        public AlarmGeofence getAlarm() {
            return alarm;
        }
    }
}
