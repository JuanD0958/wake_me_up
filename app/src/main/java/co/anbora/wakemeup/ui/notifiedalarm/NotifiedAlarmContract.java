package co.anbora.wakemeup.ui.notifiedalarm;

import co.anbora.wakemeup.domain.model.AlarmAndLastPoint;
import co.anbora.wakemeup.ui.BasePresenter;
import co.anbora.wakemeup.ui.BaseView;

public interface NotifiedAlarmContract {

    interface View extends BaseView<Presenter> {

        void viewAlarmAndLastPointOnMap(AlarmAndLastPoint alarm);

    }

    interface Presenter extends BasePresenter {

        void showGeneratedAlarm(String alarmId);

    }

}
