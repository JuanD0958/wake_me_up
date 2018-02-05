package co.anbora.wakemeup.ui.alarms;

import co.anbora.wakemeup.domain.model.AlarmGeofence;
import co.anbora.wakemeup.domain.usecase.UseCase;
import co.anbora.wakemeup.domain.usecase.UseCaseHandler;
import co.anbora.wakemeup.domain.usecase.alarm.AddAlarm;
import co.anbora.wakemeup.domain.usecase.alarm.DeleteAlarm;
import co.anbora.wakemeup.domain.usecase.alarm.GetAlarms;
import co.anbora.wakemeup.domain.usecase.alarm.UpdateStateAlarm;

/**
 * Created by dalgarins on 01/13/18.
 */

public class AlarmsPresenter implements AlarmsContract.Presenter {

    private final AlarmsContract.View view;
    private final AddAlarm addAlarm;
    private final DeleteAlarm deleteAlarm;
    private final GetAlarms getAlarms;
    private final UpdateStateAlarm updateStateAlarm;

    private final UseCaseHandler useCaseHandler;

    public AlarmsPresenter(UseCaseHandler useCaseHandler,
                           AlarmsContract.View view,
                           AddAlarm addAlarm,
                           DeleteAlarm deleteAlarm,
                           GetAlarms getAlarms,
                           UpdateStateAlarm updateStateAlarm) {

        this.useCaseHandler = useCaseHandler;
        this.view = view;
        this.addAlarm = addAlarm;
        this.deleteAlarm = deleteAlarm;
        this.getAlarms = getAlarms;
        this.updateStateAlarm = updateStateAlarm;

        this.view.setPresenter(this);
    }

    @Override
    public void loadAlarms() {
        this.useCaseHandler.execute(getAlarms,
                new GetAlarms.RequestValues(),
                new UseCase.UseCaseCallback<GetAlarms.ResponseValues>() {
            @Override
            public void onSuccess(GetAlarms.ResponseValues response) {
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
        this.useCaseHandler.execute(deleteAlarm,
                new DeleteAlarm.RequestValues(alarm),
                new UseCase.UseCaseCallback<DeleteAlarm.ResponseValues>() {
                    @Override
                    public void onSuccess(DeleteAlarm.ResponseValues response) {
                        loadAlarms();
                    }

                    @Override
                    public void onError() {

                    }
                }
        );
    }

    @Override
    public void updateStateAlarm(AlarmGeofence alarm, boolean state) {
        this.useCaseHandler.execute(updateStateAlarm,
                new UpdateStateAlarm.RequestValues(alarm, state),
                new UseCase.UseCaseCallback<UpdateStateAlarm.ResponseValues>() {
                    @Override
                    public void onSuccess(UpdateStateAlarm.ResponseValues response) {
                        loadAlarms();
                    }

                    @Override
                    public void onError() {

                    }
                }
        );
    }

    @Override
    public void showAlarm(AlarmGeofence alarm) {

    }

    @Override
    public void start() {
        loadAlarms();
    }
}
