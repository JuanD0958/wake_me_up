package co.anbora.wakemeup.domain.usecase.history;

import co.anbora.wakemeup.domain.model.HistoryAlarm;
import co.anbora.wakemeup.domain.repository.HistoryAlarmRepository;
import co.anbora.wakemeup.domain.usecase.UseCase;

public class GetLastAlarmActive extends UseCase<GetLastAlarmActive.RequestValues, GetLastAlarmActive.ResponseValues> {

    private final HistoryAlarmRepository repository;

    public GetLastAlarmActive(HistoryAlarmRepository repository) {
        this.repository = repository;
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {

        this.repository.getHistoryAlarm(requestValues.getAlarmId(), new HistoryAlarmRepository.GetHistoryAlarmCallback() {
            @Override
            public void onHistoryAlarmLoaded(HistoryAlarm historyAlarm) {
                getUseCaseCallback().onSuccess(new ResponseValues(historyAlarm));
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }


    public static final class RequestValues implements UseCase.RequestValues {

        private final String alarmId;

        public RequestValues(String alarmId){
            this.alarmId = alarmId;
        }

        public String getAlarmId() {
            return alarmId;
        }
    }

    public static final class ResponseValues implements UseCase.ResponseValue {

        private final HistoryAlarm historyAlarm;

        public ResponseValues(HistoryAlarm alarm) {
            this.historyAlarm = alarm;
        }

        public HistoryAlarm getLastActivatedAlarm() {
            return historyAlarm;
        }
    }

}
