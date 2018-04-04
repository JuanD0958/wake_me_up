package co.anbora.wakemeup.ui.notifiedalarm;

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

        this.useCaseHandler.execute(getAlarmById,
                new GetAlarmById.RequestValues(alarmId),
                new UseCase.UseCaseCallback<GetAlarmById.ResponseValues>() {
            @Override
            public void onSuccess(GetAlarmById.ResponseValues response) {


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
