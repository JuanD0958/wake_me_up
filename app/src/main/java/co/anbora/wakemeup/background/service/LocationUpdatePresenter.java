package co.anbora.wakemeup.background.service;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import co.anbora.wakemeup.Constants;
import co.anbora.wakemeup.domain.model.AlarmGeofence;
import co.anbora.wakemeup.domain.usecase.UseCase;
import co.anbora.wakemeup.domain.usecase.UseCaseHandler;
import co.anbora.wakemeup.domain.usecase.alarm.CheckAlarmAsActivated;
import co.anbora.wakemeup.domain.usecase.alarm.GetAlarms;
import co.anbora.wakemeup.domain.usecase.alarm.UpdateStateAlarm;

/**
 * Created by dalgarins on 03/19/18.
 */

public class LocationUpdatePresenter implements LocationUpdateContract.Presenter {

    private LocationUpdateContract.View view;
    private UseCaseHandler useCaseHandler;
    private GetAlarms getAlarms;
    private CheckAlarmAsActivated checkAlarmAsActivated;
    private UpdateStateAlarm updateStateAlarm;

    public LocationUpdatePresenter(LocationUpdateContract.View view,
                                   UseCaseHandler useCaseHandler,
                                   GetAlarms getAlarms,
                                   CheckAlarmAsActivated checkAlarmAsActivated,
                                   UpdateStateAlarm updateStateAlarm){

        this.view = view;
        this.useCaseHandler = useCaseHandler;
        this.getAlarms = getAlarms;
        this.checkAlarmAsActivated = checkAlarmAsActivated;
        this.updateStateAlarm = updateStateAlarm;

        this.view.setPresenter(this);
    }


    @Override
    public void start() {

    }

    @Override
    public void calculateLocationDistanceWithAlarms(Location mLocation) {

        if (mLocation != null) {
            LatLng latLngCurrent = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
            this.useCaseHandler.execute(this.getAlarms,
                    new GetAlarms.RequestValues(),
                    new UseCase.UseCaseCallback<GetAlarms.ResponseValues>() {
                        @Override
                        public void onSuccess(GetAlarms.ResponseValues response) {
                            AlarmGeofence alarmActivated = validateAlarms(response, latLngCurrent);
                            if (alarmActivated != null) {
                                disableActivatedAlarm(alarmActivated);
                            }
                        }

                        @Override
                        public void onError() {

                        }
                    });
        }
    }

    private AlarmGeofence validateAlarms(GetAlarms.ResponseValues response, LatLng latLngCurrent) {
        LatLng latLngIterator;
        for (AlarmGeofence geofence: response.getAlarms()) {
            if (geofence.state()) {
                latLngIterator = new LatLng(geofence.latitude(), geofence.longitude());
                if (SphericalUtil.computeDistanceBetween(latLngIterator, latLngCurrent) <= Constants.GEOFENCE_RADIUS_IN_METERS) {
                    return geofence;
                }
            }
        }
        return null;
    }

    @Override
    public void saveHistoricalFrom(AlarmGeofence alarmGeofence) {

        if (alarmGeofence != null) {
            this.useCaseHandler.execute(this.checkAlarmAsActivated,
                    new CheckAlarmAsActivated.RequestValues(alarmGeofence),
                    new UseCase.UseCaseCallback<CheckAlarmAsActivated.ResponseValues>() {
                        @Override
                        public void onSuccess(CheckAlarmAsActivated.ResponseValues response) {
                            view.sendNotification(alarmGeofence);
                        }

                        @Override
                        public void onError() {

                        }
                    });
        }
    }

    @Override
    public void disableActivatedAlarm(AlarmGeofence alarmGeofence) {

        if (alarmGeofence != null) {
            this.useCaseHandler.execute(this.updateStateAlarm,
                    new UpdateStateAlarm.RequestValues(alarmGeofence, false),
                    new UseCase.UseCaseCallback<UpdateStateAlarm.ResponseValues>() {
                @Override
                public void onSuccess(UpdateStateAlarm.ResponseValues response) {
                    saveHistoricalFrom(alarmGeofence);
                }

                @Override
                public void onError() {

                }
            });
        }
    }
}
