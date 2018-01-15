package co.anbora.wakemeup.ui;

import co.anbora.wakemeup.domain.model.AlarmGeofence;

/**
 * Created by dalgarins on 01/13/18.
 */

public interface MainContract {

    interface View {

        void showAlarmInMap(AlarmGeofence alarm);

    }

}
