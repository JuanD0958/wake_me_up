package co.anbora.wakemeup.service;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import co.anbora.wakemeup.domain.model.AlarmGeofence;
import co.anbora.wakemeup.domain.usecase.UseCase;
import co.anbora.wakemeup.domain.usecase.UseCaseHandler;
import co.anbora.wakemeup.domain.usecase.alarm.GetAlarms;

/**
 * Created by dalgarins on 03/19/18.
 */

public class LocationUpdatePresenter implements LocationUpdateContract.Presenter {

    private LocationUpdateContract.View view;
    private UseCaseHandler useCaseHandler;
    private GetAlarms getAlarms;

    public LocationUpdatePresenter(LocationUpdateContract.View view,
                                   UseCaseHandler useCaseHandler,
                                   GetAlarms getAlarms){

        this.view = view;
        this.useCaseHandler = useCaseHandler;
        this.getAlarms = getAlarms;

        this.view.setPresenter(this);
    }


    @Override
    public void start() {

    }

    @Override
    public void calculateLocationDistanceWithAlarms(Location mLocation) {

        LatLng latLngCurrent = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
        this.useCaseHandler.execute(this.getAlarms,
                new GetAlarms.RequestValues(),
                new UseCase.UseCaseCallback<GetAlarms.ResponseValues>() {
            @Override
            public void onSuccess(GetAlarms.ResponseValues response) {
                LatLng latLngIterator;
                for (AlarmGeofence geofence: response.getAlarms()) {
                    latLngIterator = new LatLng(geofence.latitude(), geofence.longitude());
                    if (SphericalUtil.computeDistanceBetween(latLngIterator, latLngCurrent) <= Constants.GEOFENCE_RADIUS_IN_METERS) {
                        view.sendNotification(geofence.description());
                        return;
                    }
                }
            }

            @Override
            public void onError() {

            }
        });
    }
}
