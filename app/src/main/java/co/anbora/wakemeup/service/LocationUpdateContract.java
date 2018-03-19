package co.anbora.wakemeup.service;

import android.location.Location;

import co.anbora.wakemeup.ui.BasePresenter;
import co.anbora.wakemeup.ui.BaseView;

/**
 * Created by dalgarins on 03/19/18.
 */

public interface LocationUpdateContract {

    interface View extends BaseView<LocationUpdateContract.Presenter> {

        void sendNotification(String notificationDetails);

    }

    interface Presenter extends BasePresenter {

        void calculateLocationDistanceWithAlarms(Location mLocation);

    }

}
