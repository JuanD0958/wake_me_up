package co.anbora.wakemeup.domain.usecase.history;

import java.util.List;

import co.anbora.wakemeup.domain.model.AlarmGeofence;
import co.anbora.wakemeup.domain.model.HistoryAlarm;
import co.anbora.wakemeup.domain.repository.HistoryAlarmRepository;
import co.anbora.wakemeup.domain.usecase.UseCase;

public class GetHistoryAlarms extends UseCase <GetHistoryAlarms.RequestValues, GetHistoryAlarms.ResponseValues>{

    private final HistoryAlarmRepository historyAlarmRepository;

    public GetHistoryAlarms(HistoryAlarmRepository historyAlarmRepository) {
        this.historyAlarmRepository = historyAlarmRepository;
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {

        this.historyAlarmRepository.getHistoryLastPointsAlarms(new HistoryAlarmRepository.LoadLastPointAlarmsCallback() {
            @Override
            public void onHistoryLastPointsLoaded(List<AlarmGeofence> alarms) {
                getUseCaseCallback().onSuccess(new ResponseValues(alarms));
            }

            @Override
            public void onDataNotAvailable() {

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
