package co.anbora.wakemeup.device.location;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;

import static android.arch.lifecycle.Lifecycle.State.STARTED;

/**
 * Location component
 *
 * Created by dalgarins on 03/04/18.
 */

public class LocationComponent implements OnLastLocationListener
        , OnLocationChangeListener
        , OnObserveState
        , LifecycleObserver {

    private Lifecycle lifecycle;

    private Context context;
    private LocationSettings locationSettings;

    private LocationCallback mLocationCallback;

    /**
     * Contains parameters used by {@link com.google.android.gms.location.FusedLocationProviderApi}.
     */
    private LocationRequest mLocationRequest;

    /**
     * Provides access to the Fused Location Provider API.
     */
    private FusedLocationProviderClient mFusedLocationClient;

    private LocationComponent(Context context,
                              LocationSettings locationSettings,
                              LocationRequest locationRequest,
                              FusedLocationProviderClient mFusedLocationClient,
                              LocationSettingsRequest.Builder builder,
                              SettingsClient client) {

        super();
        this.context = context;
        this.locationSettings = locationSettings;
        this.mLocationRequest = locationRequest;
        this.mFusedLocationClient = mFusedLocationClient;
        this.setUpSettingsGps(builder, client);
    }

    @Override
    public OnLastLocationListener onLastLocation(@NonNull CallbackLocation callback) {

        if (UtilPermission.checkLocationPermission(context)) {
            mFusedLocationClient.getLastLocation()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            callback.onLocationResult(task.getResult());
                        } else {
                            callback.onLocationError();
                        }
                    });
        }
        return this;
    }

    @Override
    public OnLocationChangeListener whenLocationChange() {
        return this;
    }

    @Override
    public OnLocationChangeListener onLocationChanged(@NonNull CallbackLocation callback) {

        this.mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                callback.onLocationResult(locationResult.getLastLocation());
            }
        };
        return this;
    }

    @Override
    public OnObserveState attachState() {
        return this;
    }

    @Override
    public void observe(@NonNull Lifecycle lifecycle) {

        this.lifecycle = lifecycle;
        this.lifecycle.addObserver(this);
    }

    /**
     * Display dialog to enable GPS
     */
    private void setUpSettingsGps(LocationSettingsRequest.Builder builder, SettingsClient client) {

        builder.addLocationRequest(mLocationRequest);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        if (locationSettings != null) {
            task.addOnSuccessListener(locationSettingsResponse ->
                    locationSettings.addOnSuccessListener(locationSettingsResponse));

            task.addOnFailureListener(exception ->
                    locationSettings.addOnFailureListener(exception));
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    void start() {

        if (lifecycle.getCurrentState().isAtLeast(STARTED)
                && mLocationCallback != null
                && UtilPermission.checkLocationPermission(context)) {

            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                    mLocationCallback, Looper.myLooper());
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    void stop() {

        if (mLocationCallback != null
                && UtilPermission.checkLocationPermission(context)) {

            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }

    public static class Builder {

        private LocationSettings locationSettings;
        private LocationRequest locationRequest;

        public Builder locationSettings(LocationSettings locationSettings) {

            this.locationSettings = locationSettings;
            return this;
        }

        public Builder locationRequest(long requestIntervalUpdate,
                                       long requestFastestIntervalUpdate,
                                       int requestPriority) {

            locationRequest = new LocationRequest();
            locationRequest.setInterval(requestIntervalUpdate);
            locationRequest.setFastestInterval(requestFastestIntervalUpdate);
            locationRequest.setPriority(requestPriority);
            return this;
        }

        public LocationComponent build(Context context) {
            return new LocationComponent(context,
                    locationSettings,
                    locationRequest,
                    LocationServices.getFusedLocationProviderClient(context),
                    new LocationSettingsRequest.Builder(),
                    LocationServices.getSettingsClient(context));
        }
    }
}
