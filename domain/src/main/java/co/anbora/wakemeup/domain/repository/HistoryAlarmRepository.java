package co.anbora.wakemeup.domain.repository;

import java.util.List;

import co.anbora.wakemeup.domain.model.HistoryAlarm;

public interface HistoryAlarmRepository {

    interface LoadHistoryAlarmsCallback {

        void onHistoryAlarmsLoaded(List<HistoryAlarm> alarms);

        void onDataNotAvailable();
    }

    interface GetHistoryAlarmCallback {

        void onHistoryAlarmLoaded(HistoryAlarm historyAlarm);

        void onDataNotAvailable();
    }

    void getHistoryAlarms(LoadHistoryAlarmsCallback callback);

    void getHistoryAlarm(String alarmId, GetHistoryAlarmCallback callback);

    void saveHistoryAlarm(HistoryAlarm historyAlarm);

}
