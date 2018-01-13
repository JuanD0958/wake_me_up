package co.anbora.wakemeup.domain.usecase.alarm;

import java.util.List;

import co.anbora.wakemeup.domain.model.AlarmGeofence;
import co.anbora.wakemeup.domain.repository.AlarmGeofenceRepository;
import co.anbora.wakemeup.domain.usecase.UseCase;

public class GetAlarms extends UseCase<GetAlarms.RequestValues, GetAlarms.ResponseValues>{

    private final AlarmGeofenceRepository repository;

    public GetAlarms(AlarmGeofenceRepository repository) {
        this.repository = repository;
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {

        this.repository.getAlarms(new AlarmGeofenceRepository.LoadAlarmsCallback() {
            @Override
            public void onAlarmsLoaded(List<AlarmGeofence> alarms) {
                getUseCaseCallback().onSuccess(new ResponseValues(alarms));
            }

            @Override
            public void onDataNotAvailable() {
                getUseCaseCallback().onError();
            }
        });

    }

    public static final class RequestValues implements UseCase.RequestValues {

    }

    public static final class ResponseValues implements UseCase.ResponseValue {

        private final List<AlarmGeofence> alarms;

        public ResponseValues(List<AlarmGeofence> alarms) {
            this.alarms = alarms;
        }

        public List<AlarmGeofence> getAlarms() {
            return alarms;
        }
    }

}
