package co.anbora.wakemeup.ui.addalarm;

import com.google.android.gms.maps.model.LatLng;

import co.anbora.wakemeup.domain.model.AlarmGeofence;
import co.anbora.wakemeup.ui.BasePresenter;
import co.anbora.wakemeup.ui.BaseView;

/**
 * Created by dalgarins on 01/14/18.
 */

public interface AddAlarmContract {

    interface View extends BaseView<Presenter> {

        void showMarkerInMap(Double latitude, Double longitude);

        void close();

        void searchAddress(LatLng latLng);

    }

    interface Presenter extends BasePresenter {

        void setMapMarker(Double latitude, Double longitude);

        void addAlarm(AlarmGeofence alarm);

    }

}
