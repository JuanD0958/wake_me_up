package co.anbora.wakemeup.domain.usecase.alarm;

import co.anbora.wakemeup.domain.mapper.Mapper;
import co.anbora.wakemeup.domain.model.AlarmGeofence;
import co.anbora.wakemeup.domain.model.HistoryAlarm;
import co.anbora.wakemeup.domain.repository.HistoryAlarmRepository;
import co.anbora.wakemeup.domain.usecase.UseCase;

public class CheckAlarmAsActivated extends UseCase<CheckAlarmAsActivated.RequestValues, CheckAlarmAsActivated.ResponseValues> {

    private final HistoryAlarmRepository historyAlarmRepository;
    private final Mapper<AlarmGeofence, HistoryAlarm> mapper;

    public CheckAlarmAsActivated(HistoryAlarmRepository historyAlarmRepository,
                                 Mapper<AlarmGeofence, HistoryAlarm> mapper) {
        this.historyAlarmRepository = historyAlarmRepository;
        this.mapper = mapper;
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {

        HistoryAlarm history = mapper.apply(requestValues.getAlarm());
        this.historyAlarmRepository.saveHistoryAlarm(history);

        getUseCaseCallback().onSuccess(new ResponseValues(history));

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

        private final HistoryAlarm alarm;

        public ResponseValues(HistoryAlarm alarm) {
            this.alarm = alarm;
        }

        public HistoryAlarm getHistoryAlarm() {
            return alarm;
        }
    }

}
