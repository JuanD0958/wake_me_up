package co.anbora.wakemeup.domain.usecase.history;

import java.util.List;

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

        this.historyAlarmRepository.getHistoryAlarms(new HistoryAlarmRepository.LoadHistoryAlarmsCallback() {
            @Override
            public void onHistoryAlarmsLoaded(List<HistoryAlarm> alarms) {
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

        private final List<HistoryAlarm> alarms;

        public ResponseValues(List<HistoryAlarm> alarms) {
            this.alarms = alarms;
        }

        public List<HistoryAlarm> getAlarms() {
            return alarms;
        }
    }

}
