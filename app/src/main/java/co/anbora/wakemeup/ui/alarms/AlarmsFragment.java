package co.anbora.wakemeup.ui.alarms;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import co.anbora.wakemeup.Injection;
import co.anbora.wakemeup.R;
import co.anbora.wakemeup.adapter.alarms.AlarmsAdapter;
import co.anbora.wakemeup.broadcast.GeofenceBroadcastReceiver;
import co.anbora.wakemeup.databinding.FragmentAlarmsBinding;
import co.anbora.wakemeup.device.location.CallbackLocation;
import co.anbora.wakemeup.device.location.LocationSettings;
import co.anbora.wakemeup.device.location.OnLastLocationListener;
import co.anbora.wakemeup.domain.model.AlarmGeofence;
import co.anbora.wakemeup.service.Constants;
import co.anbora.wakemeup.service.GeofenceErrorMessages;
import co.anbora.wakemeup.ui.addalarm.AddAlarmActivity;
import co.anbora.wakemeup.util.Utilities;
import ru.alexbykov.nopermission.PermissionHelper;

public class AlarmsFragment extends Fragment implements AlarmsContract.View,
        OnCompleteListener<Void>, OnMapReadyCallback {

    private static final String TAG = AlarmsFragment.class.getSimpleName();
    private FragmentAlarmsBinding binding;
    private AlarmsAdapter adapter;
    private AlarmsContract.Presenter presenter;

    private final int METERS = 20;
    private final int SECONDS = 10 * 1000;
    private static final int REQUEST_CHECK_SETTINGS = 10;

    /**
     * Provides access to the Geofencing API.
     */
    private GeofencingClient mGeofencingClient;

    /**
     * The list of geofences used in this sample.
     */
    private ArrayList<Geofence> mGeofenceList;

    /**
     * Used when requesting to add or remove geofences.
     */
    private PendingIntent mGeofencePendingIntent;
    private SupportMapFragment mMapFragment;

    private OnLastLocationListener locationComponent;
    private LocationSettings locationSettings;

    private PermissionHelper permissionHelper;

    private GoogleMap googleMap;
    private List<Circle> listDrawAlarms;
    private Marker currentMarker;

    public AlarmsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setupUI(inflater, container);
        configureLocationSettings();
        setupUX();
        setupPermissionHelper();
        askLocationPermission();

        return binding.getRoot();
    }

    private void setupUX() {
        // Empty list for storing geofences.

        mGeofenceList = new ArrayList<>();
        listDrawAlarms = new LinkedList<>();

        mGeofencingClient = LocationServices.getGeofencingClient(getActivity());

        mMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);
    }

    private void configureLocationSettings() {
        locationSettings = new LocationSettings() {
            @Override
            public void addOnSuccessListener(LocationSettingsResponse locationSettingsResponse) {

            }

            @Override
            public void addOnFailureListener(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    try {
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(getActivity(),
                                REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                    }
                }
            }
        };
    }

    private void setupUI(LayoutInflater inflater, ViewGroup container) {
        binding = FragmentAlarmsBinding.inflate(inflater, container, false);
        binding.fabAddNewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddAlarm();
            }
        });
        adapter = new AlarmsAdapter(new ArrayList<AlarmGeofence>(), presenter);
        binding.rvList.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvList.setItemAnimator(new DefaultItemAnimator());
        binding.rvList.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.presenter.loadAlarms();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!Utilities.checkPermissions(getActivity())) {
            Utilities.requestPermissions(TAG, getActivity(), R.string.permission_rationale, binding.contentLayout.getRootView());
        } else {
            performPendingGeofenceTask();
        }
    }

    /**
     * Returns true if geofences were added, otherwise false.
     */
    private boolean getGeofencesAdded() {
        return PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean(
                Constants.GEOFENCES_ADDED_KEY, false);
    }

    /**
     * Stores whether geofences were added ore removed in {@link SharedPreferences};
     *
     * @param added Whether geofences were added or removed.
     */
    private void updateGeofencesAdded(boolean added) {
        PreferenceManager.getDefaultSharedPreferences(getActivity())
                .edit()
                .putBoolean(Constants.GEOFENCES_ADDED_KEY, added)
                .apply();
    }

    /**
     * Performs the geofencing task that was pending until location permission was granted.
     */
    private void performPendingGeofenceTask() {
        addGeofences();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void showAlarms(List<AlarmGeofence> alarms) {
        if (alarms != null) {
            this.adapter.setList(alarms);
            populateGeofenceList(alarms);
            addGeofences();
        }
    }

    @Override
    public void drawAllarmsInMap(List<AlarmGeofence> alarms) {
        if (!alarms.isEmpty()) {

            listDrawAlarms.forEach(Circle::remove);
            listDrawAlarms.clear();

            LatLng alarmLocation;
            for (AlarmGeofence alarm : alarms){
                alarmLocation = new LatLng(alarm.latitude(), alarm.longitude());
                listDrawAlarms.add(this.googleMap.addCircle(new CircleOptions().radius(Constants.GEOFENCE_RADIUS_IN_METERS)
                        .center(alarmLocation)));
            }
        }
    }

    /**
     * Gets a PendingIntent to send with the request to add or remove Geofences. Location Services
     * issues the Intent inside this PendingIntent whenever a geofence transition occurs for the
     * current list of geofences.
     *
     * @return A PendingIntent for the IntentService that handles geofence transitions.
     */
    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(getActivity(), GeofenceBroadcastReceiver.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
        // addGeofences() and removeGeofences().
        mGeofencePendingIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return mGeofencePendingIntent;
    }

    @Override
    public void showAddAlarm() {

        Intent intent = new Intent(getActivity(), AddAlarmActivity.class);
        startActivity(intent);
    }

    @Override
    public void showNoAlarms() {

    }

    @Override
    public void showAlarms() {

        this.presenter.loadAlarms();
    }

    /**
     * Adds geofences. This method should be called after the user has granted the location
     * permission.
     */
    @SuppressWarnings("MissingPermission")
    private void addGeofences() {
        if (!Utilities.checkPermissions(getActivity())) {
            showSnackbar(getString(R.string.insufficient_permissions));
            return;
        }

        if (!mGeofenceList.isEmpty()) {
            mGeofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent())
                    .addOnCompleteListener(this);
        }
    }

    /**
     * Shows a {@link Snackbar} using {@code text}.
     *
     * @param text The Snackbar text.
     */
    private void showSnackbar(final String text) {
        View container = getActivity().findViewById(android.R.id.content);
        if (container != null) {
            Snackbar.make(container, text, Snackbar.LENGTH_LONG).show();
        }
    }

    /**
     * Builds and returns a GeofencingRequest. Specifies the list of geofences to be monitored.
     * Also specifies how the geofence notifications are initially triggered.
     */
    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();

        // The INITIAL_TRIGGER_ENTER flag indicates that geofencing service should trigger a
        // GEOFENCE_TRANSITION_ENTER notification when the geofence is added and if the device
        // is already inside that geofence.
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);

        // Add the geofences to be monitored by geofencing service.
        builder.addGeofences(mGeofenceList);

        // Return a GeofencingRequest.
        return builder.build();
    }

    private void populateGeofenceList(List<AlarmGeofence> alarms) {
        mGeofenceList.clear();
        for (AlarmGeofence alarm: alarms) {

            if (alarm.state()) {
                mGeofenceList.add(new Geofence.Builder()
                        // Set the request ID of the geofence. This is a string to identify this
                        // geofence.
                        .setRequestId(alarm.id())

                        // Set the circular region of this geofence.
                        .setCircularRegion(
                                alarm.latitude(),
                                alarm.longitude(),
                                Constants.GEOFENCE_RADIUS_IN_METERS
                        )

                        // Set the expiration duration of the geofence. This geofence gets automatically
                        // removed after this period of time.
                        .setExpirationDuration(Constants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)

                        // Set the transition types of interest. Alerts are only generated for these
                        // transition. We track entry and exit transitions in this sample.
                        .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                                Geofence.GEOFENCE_TRANSITION_EXIT)

                        // Create the geofence.
                        .build());
            }
        }
    }

    @Override
    public void setPresenter(AlarmsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onComplete(@NonNull Task<Void> task) {

        if (task.isSuccessful()) {

            Toast.makeText(getActivity(), getString(R.string.task_ok), Toast.LENGTH_SHORT).show();
        } else {
            // Get the status code for the error and log it using a user-friendly message.
            String errorMessage = GeofenceErrorMessages.getErrorString(getActivity(), task.getException());
            Log.w(TAG, errorMessage);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


        googleMap.getUiSettings().setMapToolbarEnabled(false);
        this.googleMap = googleMap;

        locationComponent = Injection.provideLocationComponent(getContext(),
                locationSettings,
                METERS, SECONDS, LocationRequest.PRIORITY_HIGH_ACCURACY);

        CallbackLocation callback = new CallbackLocation() {
            @Override
            public void onLocationResult(Location location) {
                if (currentMarker != null) {
                    currentMarker.remove();
                }

                LatLng currentLocation = new LatLng(location.getLatitude()
                        , location.getLongitude());

                currentMarker = googleMap.addMarker(new MarkerOptions().position(currentLocation)
                        .title(getString(R.string.current_location)));

                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 17));
            }

            @Override
            public void onLocationError() {

            }
        };

        locationComponent.onLastLocation(callback)
                .whenLocationChange()
                .onLocationChanged(callback).attachState().observe(getLifecycle());
    }

    private void setupPermissionHelper() {
        permissionHelper = new PermissionHelper(this);
    }

    private void askLocationPermission() {
        permissionHelper.check(Manifest.permission.ACCESS_FINE_LOCATION)
                .withDialogBeforeRun(R.string.dialog_before_run_title,
                        R.string.dialog_before_run_message, R.string.dialog_positive_button)
                .setDialogPositiveButtonColor(android.R.color.holo_orange_dark)
                .onSuccess(this::onSuccess)
                .onDenied(this::onDenied)
                .onNeverAskAgain(this::onNeverAskAgain)
                .run();
    }


    private void onSuccess() {
        Log.d(TAG, "LocationSuccess");
    }


    private void onNeverAskAgain() {
        Log.d(TAG, "LocationNeverAskAgain");
        permissionHelper.startApplicationSettingsActivity();
    }

    private void onDenied() {
        Log.d(TAG, "LocationDenied");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
