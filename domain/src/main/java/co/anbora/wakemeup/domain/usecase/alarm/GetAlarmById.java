package co.anbora.wakemeup.domain.usecase.alarm;

import co.anbora.wakemeup.domain.model.AlarmGeofence;
import co.anbora.wakemeup.domain.repository.AlarmGeofenceRepository;
import co.anbora.wakemeup.domain.usecase.UseCase;

public class GetAlarmById extends UseCase<GetAlarmById.RequestValues, GetAlarmById.ResponseValues>{

    private final AlarmGeofenceRepository alarmRepository;

    public GetAlarmById(AlarmGeofenceRepository alarmRepository) {
        this.alarmRepository = alarmRepository;
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {

        this.alarmRepository.getAlarm(requestValues.getAlarmId(), new AlarmGeofenceRepository.GetAlarmCallback() {
            @Override
            public void onAlarmLoaded(AlarmGeofence task) {
                getUseCaseCallback().onSuccess(new ResponseValues(task));
            }

            @Override
            public void onDataNotAvailable() {

            }
        });

    }

    public static final class RequestValues implements UseCase.RequestValues {

        private final String alarmId;

        public RequestValues(String alarmId) {
            this.alarmId = alarmId;
        }

        public String getAlarmId() {
            return this.alarmId;
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
