package co.anbora.wakemeup.ui.alarms;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
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

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import co.anbora.wakemeup.R;
import co.anbora.wakemeup.adapter.alarms.AlarmsAdapter;
import co.anbora.wakemeup.databinding.FragmentAlarmsBinding;
import co.anbora.wakemeup.device.location.Callback;
import co.anbora.wakemeup.device.location.LocationComponentListenerImpl;
import co.anbora.wakemeup.domain.model.AlarmGeofence;
import co.anbora.wakemeup.service.Constants;
import co.anbora.wakemeup.service.GeofenceErrorMessages;
import co.anbora.wakemeup.service.GeofenceTransitionsIntentService;
import co.anbora.wakemeup.ui.addalarm.AddAlarmActivity;
import co.anbora.wakemeup.util.Utilities;
import ru.alexbykov.nopermission.PermissionHelper;

public class AlarmsFragment extends Fragment implements AlarmsContract.View,
        OnCompleteListener<Void>, OnMapReadyCallback {

    private static final String TAG = AlarmsFragment.class.getSimpleName();
    private FragmentAlarmsBinding binding;
    private AlarmsAdapter adapter;
    private AlarmsContract.Presenter presenter;

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

    private LocationComponentListenerImpl observerLocation;

    private PermissionHelper permissionHelper;

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
        setupUX();
        setupPermissionHelper();
        askLocationPermission();

        return binding.getRoot();
    }

    private void setupUX() {
        // Empty list for storing geofences.
        mGeofenceList = new ArrayList<>();

        mGeofencingClient = LocationServices.getGeofencingClient(getActivity());

        mMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);
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
        }
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
        Intent intent = new Intent(getActivity(), GeofenceTransitionsIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
        // addGeofences() and removeGeofences().
        return PendingIntent.getService(getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
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
    public void onMapReady(final GoogleMap googleMap) {

        googleMap.getUiSettings().setMapToolbarEnabled(false);

        observerLocation = new LocationComponentListenerImpl(getActivity(), getLifecycle(), new Callback() {
            @Override
            public void execute(Location location) {
                googleMap.clear();
                LatLng currentLocation = new LatLng(location.getLatitude()
                        , location.getLongitude());
                googleMap.addMarker(new MarkerOptions().position(currentLocation)
                        .title("Mi ubicacion"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 17));
            }
        });

        getLifecycle().addObserver(observerLocation);
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
