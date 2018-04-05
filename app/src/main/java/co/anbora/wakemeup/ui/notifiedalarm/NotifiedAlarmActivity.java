package co.anbora.wakemeup.ui.notifiedalarm;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import co.anbora.wakemeup.Constants;
import co.anbora.wakemeup.Injection;
import co.anbora.wakemeup.R;
import co.anbora.wakemeup.domain.model.AlarmAndLastPoint;

public class NotifiedAlarmActivity extends FragmentActivity
        implements NotifiedAlarmContract.View, OnMapReadyCallback {

    private GoogleMap mMap;
    private NotifiedAlarmContract.Presenter presenter;
    private String alarmId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpUI();
        setUpUX();
    }

    private void setUpUX() {
        new NotifiedAlarmPresenter(Injection.provideUseCaseHandler(),
                this,
                Injection.provideGetAlarmById(),
                Injection.provideGetLastAlarmActive()
        );

        Bundle bundle = getIntent().getExtras();
        if (bundle.getString(Constants.ACTIVE_ALARM) != null) {
            alarmId = bundle.getString(Constants.ACTIVE_ALARM);
        }

        Injection.provideVibrations(getApplicationContext()).cancel();
    }

    private void setUpUI() {
        setContentView(R.layout.activity_notified_alarm);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        if (alarmId != null) {
            presenter.showGeneratedAlarm(alarmId);
        }
    }



    @Override
    public void setPresenter(NotifiedAlarmContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void viewAlarmAndLastPointOnMap(AlarmAndLastPoint alarm) {

        addLastPointMark(alarm);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(AddAlarmMark(alarm), 17));
    }

    private LatLng AddAlarmMark(AlarmAndLastPoint alarm) {
        LatLng positionAlarm = new LatLng(alarm.alarm().latitude(), alarm.alarm().longitude());
        mMap.addCircle(new CircleOptions().radius(Constants.GEOFENCE_RADIUS_IN_METERS)
                .center(positionAlarm));
        return positionAlarm;
    }

    private void addLastPointMark(AlarmAndLastPoint alarm) {
        LatLng position = new LatLng(alarm.lastPoint().latitude(), alarm.lastPoint().longitude());
        mMap.addMarker(new MarkerOptions().position(position).title(getResources().getString(R.string.place_active)));
    }
}
