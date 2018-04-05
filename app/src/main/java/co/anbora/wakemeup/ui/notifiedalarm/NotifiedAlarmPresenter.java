package co.anbora.wakemeup.ui.notifiedalarm;

import co.anbora.wakemeup.data.repository.model.AlarmAndLastPointModel;
import co.anbora.wakemeup.data.repository.model.AlarmGeofenceModel;
import co.anbora.wakemeup.domain.model.AlarmGeofence;
import co.anbora.wakemeup.domain.usecase.UseCase;
import co.anbora.wakemeup.domain.usecase.UseCaseHandler;
import co.anbora.wakemeup.domain.usecase.alarm.GetAlarmById;
import co.anbora.wakemeup.domain.usecase.history.GetLastAlarmActive;

public class NotifiedAlarmPresenter implements NotifiedAlarmContract.Presenter {

    private UseCaseHandler useCaseHandler;
    private GetAlarmById getAlarmById;
    private GetLastAlarmActive getLastAlarmActive;

    private NotifiedAlarmContract.View view;

    public NotifiedAlarmPresenter(UseCaseHandler useCaseHandler,
                                  NotifiedAlarmContract.View view,
                                  GetAlarmById getAlarmById,
                                  GetLastAlarmActive getLastAlarmActive) {

        this.useCaseHandler = useCaseHandler;
        this.getAlarmById = getAlarmById;
        this.getLastAlarmActive = getLastAlarmActive;

        this.view = view;
        this.view.setPresenter(this);

    }


    @Override
    public void showGeneratedAlarm(String alarmId) {

        getLastAlarmActive(alarmId);
    }

    private void getLastAlarmActive(String alarmId) {

        this.useCaseHandler.execute(getAlarmById,
                new GetAlarmById.RequestValues(alarmId),
                new UseCase.UseCaseCallback<GetAlarmById.ResponseValues>() {
                    @Override
                    public void onSuccess(GetAlarmById.ResponseValues response) {

                        getPositionWhenActiveAlarm(response.getAlarm());
                    }

                    @Override
                    public void onError() {

                    }
                });
    }

    private void getPositionWhenActiveAlarm(AlarmGeofence alarmGeofence) {

        this.useCaseHandler.execute(getLastAlarmActive,
                new GetLastAlarmActive.RequestValues(alarmGeofence.id()),
                new UseCase.UseCaseCallback<GetLastAlarmActive.ResponseValues>() {
            @Override
            public void onSuccess(GetLastAlarmActive.ResponseValues response) {

                view.viewAlarmAndLastPointOnMap(AlarmAndLastPointModel.builder()
                        .alarm(alarmGeofence)
                        .lastPoint(response.getLastActivatedAlarm())
                        .build()
                );
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
