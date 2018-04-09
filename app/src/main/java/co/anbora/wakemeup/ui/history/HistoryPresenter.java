package co.anbora.wakemeup.ui.history;

import co.anbora.wakemeup.domain.model.AlarmGeofence;
import co.anbora.wakemeup.domain.usecase.UseCase;
import co.anbora.wakemeup.domain.usecase.UseCaseHandler;
import co.anbora.wakemeup.domain.usecase.history.GetHistoryAlarms;
import co.anbora.wakemeup.ui.alarms.AlarmsContract;

public class HistoryPresenter implements HistoryContract.Presenter, AlarmsContract.Presenter {

    private UseCaseHandler useCaseHandler;
    private AlarmsContract.View view;
    private GetHistoryAlarms getHistoryAlarms;

    public HistoryPresenter(UseCaseHandler useCaseHandler,
                            AlarmsContract.View view,
                            GetHistoryAlarms getHistoryAlarms) {

        this.useCaseHandler = useCaseHandler;
        this.view = view;
        this.getHistoryAlarms = getHistoryAlarms;

        this.view.setPresenter(this);
    }

    @Override
    public void start() {
        loadAlarms();
    }

    @Override
    public void loadAlarms() {

        this.useCaseHandler.execute(getHistoryAlarms,
                new GetHistoryAlarms.RequestValues(),
                new UseCase.UseCaseCallback<GetHistoryAlarms.ResponseValues>() {
            @Override
            public void onSuccess(GetHistoryAlarms.ResponseValues response) {
                if (view != null) {
                    view.showAlarms(response.getAlarms());
                }
            }

            @Override
            public void onError() {

            }
        });
    }

    @Override
    public void addNewAlarm() {

    }

    @Override
    public void deleteAlarm(AlarmGeofence alarm) {

    }

    @Override
    public void updateStateAlarm(AlarmGeofence alarm, boolean state) {

    }

    @Override
    public void showAlarm(AlarmGeofence alarm) {
        if (view != null) {
            this.view.viewAlarmOnMap(alarm);
        }
    }
}
