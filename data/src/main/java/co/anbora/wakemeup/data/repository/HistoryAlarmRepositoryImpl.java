package co.anbora.wakemeup.data.repository;

import java.util.List;
import java.util.stream.Collectors;

import co.anbora.wakemeup.data.repository.local.db.dao.HistoryAlarmDao;
import co.anbora.wakemeup.data.repository.local.db.model.HistoryAlarmEntity;
import co.anbora.wakemeup.domain.mapper.TwoWaysMapper;
import co.anbora.wakemeup.domain.model.HistoryAlarm;
import co.anbora.wakemeup.domain.repository.HistoryAlarmRepository;

public class HistoryAlarmRepositoryImpl implements HistoryAlarmRepository {

    private HistoryAlarmDao historyAlarmDao;
    private TwoWaysMapper<HistoryAlarmEntity, HistoryAlarm> mapper;

    public HistoryAlarmRepositoryImpl(HistoryAlarmDao historyAlarmDao,
                                      TwoWaysMapper<HistoryAlarmEntity, HistoryAlarm> mapper) {
        this.historyAlarmDao = historyAlarmDao;
        this.mapper = mapper;
    }

    @Override
    public void getHistoryAlarms(LoadHistoryAlarmsCallback callback) {

        List<HistoryAlarm> historyAlarms = this.historyAlarmDao
                .selectAll()
                .stream()
                .map(mapper).collect(Collectors.<HistoryAlarm>toList());

        callback.onHistoryAlarmsLoaded(historyAlarms);
    }

    @Override
    public void getHistoryAlarm(String alarmId, GetHistoryAlarmCallback callback) {

        callback.onHistoryAlarmLoaded(mapper.apply(this.historyAlarmDao
                .selectById(alarmId)));
    }

    @Override
    public void saveHistoryAlarm(HistoryAlarm historyAlarm) {
        this.historyAlarmDao.insert(mapper.inverseMap(historyAlarm));
    }
}
