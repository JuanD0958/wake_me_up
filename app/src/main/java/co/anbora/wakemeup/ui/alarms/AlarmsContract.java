package co.anbora.wakemeup.ui.alarms;

import java.util.List;

import co.anbora.wakemeup.domain.model.AlarmGeofence;
import co.anbora.wakemeup.ui.BasePresenter;
import co.anbora.wakemeup.ui.BaseView;

/**
 * Created by dalgarins on 01/13/18.
 */

public interface AlarmsContract {

    interface View extends BaseView<Presenter> {

        void showAlarms(List<AlarmGeofence> alarms);

        void drawAllarmsInMap(List<AlarmGeofence> alarms);

        void showAddAlarm();

        void showNoAlarms();

        void showAlarms();
    }

    interface Presenter extends BasePresenter {

        void loadAlarms();

        void addNewAlarm();

        void deleteAlarm(AlarmGeofence alarm);

        void updateStateAlarm(AlarmGeofence alarm, boolean state);

        void showAlarm(AlarmGeofence alarm);

    }

}
