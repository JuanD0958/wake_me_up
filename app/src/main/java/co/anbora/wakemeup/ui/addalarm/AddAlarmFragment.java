package co.anbora.wakemeup.ui.addalarm;

import android.support.v4.app.Fragment;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import co.anbora.wakemeup.R;
import co.anbora.wakemeup.data.repository.model.AlarmGeofenceModel;
import co.anbora.wakemeup.databinding.FragmentAddAlarmBinding;
import co.anbora.wakemeup.domain.model.AlarmGeofence;


public class AddAlarmFragment extends Fragment implements AddAlarmContract.View, OnMapReadyCallback {

    private FragmentAddAlarmBinding binding;

    private AddAlarmContract.Presenter presenter;

    private GoogleMap googleMap;
    private Geocoder geocoder;

    private AlarmGeofence alarm;

    private SupportMapFragment mapFragment;

    public AddAlarmFragment() {
        // Required empty public constructor
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

        return binding.getRoot();
    }

    private void setupUX() {
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.add_alarm_map);
        mapFragment.getMapAsync(this);

        binding.okAddAlarm.setOnClickListener(view -> {
            if (alarm != null) {
                presenter.addAlarm(alarm);
            }
        });
    }

    private void setupUI(LayoutInflater inflater, ViewGroup container) {
        binding = FragmentAddAlarmBinding.inflate(inflater, container, false);
    }

    @Override
    public void showMarkerInMap(Double latitude, Double longitude) {


        LatLng latLng = new LatLng(latitude, longitude);
        // Creating a marker
        MarkerOptions markerOptions = new MarkerOptions();

        // Setting the position for the marker
        markerOptions.position(latLng);

        // Setting the title for the marker.
        // This will be displayed on taping the marker
        markerOptions.title(latLng.latitude + " : " + latLng.longitude);

        // Clears the previously touched position
        googleMap.clear();

        // Animating to the touched position
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

        // Placing a marker on the touched position
        googleMap.addMarker(markerOptions);
    }

    @Override
    public void close() {
        getActivity().finish();
    }

    public void searchAddress(LatLng latLng) {

        geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            List<Address> list = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (!list.isEmpty()) {
                String address = getAddress(list.get(0).getAddressLine(0));

                binding.requestContentOriginLayout.textInformationRoute.setText(address);
                createAlarm(latLng, address);
            }
        } catch (IOException e) {
            Log.d("", e.getMessage());
        }
    }

    private void createAlarm(LatLng latLng, String address) {
        alarm = AlarmGeofenceModel.builder()
                .id(UUID.randomUUID().toString())
                .name("Alarma")
                .description(address)
                .latitude(latLng.latitude)
                .longitude(latLng.longitude)
                .createdAt(new Date().getTime())
                .state(true)
                .needsSync(true)
                .build();
    }

    private String getAddress(String geoCodeAddress) {

        String response = "";
        if (geoCodeAddress != null && geoCodeAddress.split(",").length > 0) {
            response = geoCodeAddress.split(",")[0];
        }
        return response;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.googleMap = googleMap;

        LatLng latLng = new LatLng(6.251608, -75.544006);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10);
        this.googleMap.animateCamera(cameraUpdate);

        this.googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                searchAddress(latLng);
                showMarkerInMap(latLng.latitude, latLng.longitude);
            }
        });
    }

    @Override
    public void setPresenter(AddAlarmContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
