package co.anbora.wakemeup.domain.usecase.alarm;

import co.anbora.wakemeup.domain.model.AlarmGeofence;
import co.anbora.wakemeup.domain.repository.AlarmGeofenceRepository;
import co.anbora.wakemeup.domain.usecase.UseCase;

public class UpdateStateAlarm extends UseCase<UpdateStateAlarm.RequestValues, UpdateStateAlarm.ResponseValues>{

    private final AlarmGeofenceRepository repository;

    public UpdateStateAlarm(AlarmGeofenceRepository repository) {
        this.repository = repository;
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {

        AlarmGeofence alarm = requestValues.getAlarm();
        Boolean state = requestValues.getState();
        this.repository.updateAlarm(alarm, state);

        getUseCaseCallback().onSuccess(new ResponseValues(alarm));
    }

    public static final class RequestValues implements UseCase.RequestValues {

        private final AlarmGeofence alarm;
        private final Boolean state;

        public RequestValues(AlarmGeofence alarm, Boolean state) {
            this.alarm = alarm;
            this.state = state;
        }

        public AlarmGeofence getAlarm() {
            return alarm;
        }

        public Boolean getState() {
            return state;
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
