package co.anbora.wakemeup.background.service;

import android.location.Location;

import co.anbora.wakemeup.domain.model.AlarmGeofence;
import co.anbora.wakemeup.ui.BasePresenter;
import co.anbora.wakemeup.ui.BaseView;

/**
 * Created by dalgarins on 03/19/18.
 */

public interface LocationUpdateContract {

    interface View extends BaseView<LocationUpdateContract.Presenter> {

        void sendNotification(AlarmGeofence notification);

    }

    interface Presenter extends BasePresenter {

        void calculateLocationDistanceWithAlarms(Location mLocation);

        void saveHistoricalFrom(AlarmGeofence alarmGeofence);

        void disableActivatedAlarm(AlarmGeofence alarmGeofence, Location mLocation);

    }

}
