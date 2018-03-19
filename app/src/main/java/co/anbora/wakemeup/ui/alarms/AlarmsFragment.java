package co.anbora.wakemeup.ui.alarms;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import co.anbora.wakemeup.Injection;
import co.anbora.wakemeup.R;
import co.anbora.wakemeup.Utils;
import co.anbora.wakemeup.adapter.alarms.AlarmsAdapter;
import co.anbora.wakemeup.databinding.FragmentAlarmsBinding;
import co.anbora.wakemeup.device.location.CallbackLocation;
import co.anbora.wakemeup.device.location.LocationSettings;
import co.anbora.wakemeup.device.location.OnLastLocationListener;
import co.anbora.wakemeup.domain.model.AlarmGeofence;
import co.anbora.wakemeup.service.Constants;
import co.anbora.wakemeup.ui.addalarm.AddAlarmActivity;
import co.anbora.wakemeup.util.Utilities;
import ru.alexbykov.nopermission.PermissionHelper;

public class AlarmsFragment extends Fragment implements AlarmsContract.View,
        OnMapReadyCallback {

    private static final String TAG = AlarmsFragment.class.getSimpleName();

    private FragmentAlarmsBinding binding;
    private AlarmsAdapter adapter;
    private AlarmsContract.Presenter presenter;

    public static final int MILLISECONDS = 1000;
    private final int INTERVAL_TIME = 10 * MILLISECONDS;
    private final int FAST_INTERVAL = INTERVAL_TIME /2 ;
    private static final int REQUEST_CHECK_SETTINGS = 10;

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
        setupSwipeLayout();

        return binding.getRoot();
    }

    private void setupUX() {
        // Empty list for storing geofences.

        listDrawAlarms = new LinkedList<>();

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
        binding.fabAddNewPost.setOnClickListener(view ->
            showAddAlarm()
        );
        adapter = new AlarmsAdapter(new ArrayList<AlarmGeofence>(), presenter);
        binding.rvList.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvList.setItemAnimator(new DefaultItemAnimator());
        binding.rvList.setAdapter(adapter);

        binding.switchUnable.setOnCheckedChangeListener((compoundButton, b) -> processGeofencing(b));
    }

    private void setupSwipeLayout() {
        binding.srlList.setOnRefreshListener(() -> {
            presenter.loadAlarms();
        });
    }

    private void processGeofencing(boolean status) {
        if (!Utilities.checkPermissions(getActivity())) {
            Utilities.requestPermissions(TAG, getActivity(), R.string.permission_rationale, binding.contentLayout.getRootView());
        } else {
            Utils.updateGeofencesAdded(getActivity(), status);
        }
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
        setButtonsEnabledState();
    }

    @Override
    public void showAlarms(List<AlarmGeofence> alarms) {
        if (alarms != null) {
            this.adapter.setList(alarms);
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

    @Override
    public void showAddAlarm() {

        Intent intent = new Intent(getActivity(), AddAlarmActivity.class);
        startActivity(intent);
    }

    @Override
    public void hideSwipeLayoutLoading() {

        binding.srlList.setRefreshing(false);
    }

    @Override
    public void showNoAlarms() {

    }

    @Override
    public void showAlarms() {

        this.presenter.loadAlarms();
    }

    /**
     * Ensures that only one button is enabled at any time. The Add Geofences button is enabled
     * if the user hasn't yet added geofences. The Remove Geofences button is enabled if the
     * user has added geofences.
     */
    private void setButtonsEnabledState() {
        activateSwitchAlarm(Utils.getGeofencesAdded(getActivity()));
    }

    private void activateSwitchAlarm(boolean enable) {
        binding.switchUnable.setChecked(enable);
        binding.switchUnable.setPressed(enable);
        binding.switchUnable.clearFocus();
    }

    @Override
    public void setPresenter(AlarmsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


        googleMap.getUiSettings().setMapToolbarEnabled(false);
        this.googleMap = googleMap;

        locationComponent = Injection.provideLocationComponent(getContext(),
                locationSettings,
                INTERVAL_TIME, FAST_INTERVAL, LocationRequest.PRIORITY_HIGH_ACCURACY);

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
