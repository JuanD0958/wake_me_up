package co.anbora.wakemeup.domain.usecase.alarm;

import co.anbora.wakemeup.domain.model.AlarmGeofence;
import co.anbora.wakemeup.domain.repository.AlarmGeofenceRepository;
import co.anbora.wakemeup.domain.usecase.UseCase;

public class DisableAlarm extends UseCase<DisableAlarm.RequestValues, DisableAlarm.ResponseValues>{

    private final AlarmGeofenceRepository repository;

    public DisableAlarm(AlarmGeofenceRepository repository) {
        this.repository = repository;
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {

        AlarmGeofence alarm = requestValues.getAlarm();
        this.repository.disableAlarm(alarm);

        getUseCaseCallback().onSuccess(new ResponseValues(alarm));
    }

    public static final class RequestValues implements UseCase.RequestValues {

        private final AlarmGeofence alarm;

        public RequestValues(AlarmGeofence alarm) {
            this.alarm = alarm;
        }

        public AlarmGeofence getAlarm() {
            return alarm;
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
