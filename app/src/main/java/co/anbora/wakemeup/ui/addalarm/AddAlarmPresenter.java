package co.anbora.wakemeup.ui.addalarm;

import co.anbora.wakemeup.domain.model.AlarmGeofence;
import co.anbora.wakemeup.domain.usecase.UseCase;
import co.anbora.wakemeup.domain.usecase.UseCaseHandler;
import co.anbora.wakemeup.domain.usecase.alarm.AddAlarm;
import co.anbora.wakemeup.ui.alarms.AlarmsContract;

/**
 * Created by dalgarins on 01/14/18.
 */

public class AddAlarmPresenter implements AddAlarmContract.Presenter {

    private final AddAlarmContract.View view;
    private final AddAlarm addAlarm;

    private final UseCaseHandler useCaseHandler;

    public AddAlarmPresenter(UseCaseHandler useCaseHandler, AddAlarm addAlarm, AddAlarmContract.View view) {

        this.useCaseHandler = useCaseHandler;
        this.addAlarm = addAlarm;
        this.view = view;

        this.view.setPresenter(this);
    }

    @Override
    public void setMapMarker(Double latitude, Double longitude) {

    }

    @Override
    public void addAlarm(AlarmGeofence alarm) {
        this.useCaseHandler.execute(addAlarm, new AddAlarm.RequestValues(alarm),
                new UseCase.UseCaseCallback<AddAlarm.ResponseValues>() {
            @Override
            public void onSuccess(AddAlarm.ResponseValues response) {
                if (view != null) {
                    view.close();
                }
            }

            @Override
            public void onError() {

            }
        });
    }

    @Override
    public void start() {

    }
}
