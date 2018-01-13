package co.anbora.wakemeup.domain.usecase.alarm;

import co.anbora.wakemeup.domain.model.AlarmGeofence;
import co.anbora.wakemeup.domain.repository.AlarmGeofenceRepository;
import co.anbora.wakemeup.domain.usecase.UseCase;

public class DeleteAlarm extends UseCase<DeleteAlarm.RequestValues, DeleteAlarm.ResponseValues>{

    private final AlarmGeofenceRepository repository;

    public DeleteAlarm(AlarmGeofenceRepository repository) {
        this.repository = repository;
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {

        AlarmGeofence alarm = requestValues.getAlarm();
        this.repository.deleteAlarm(alarm);

        getUseCaseCallback().onSuccess(new ResponseValues());
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

    }

}
