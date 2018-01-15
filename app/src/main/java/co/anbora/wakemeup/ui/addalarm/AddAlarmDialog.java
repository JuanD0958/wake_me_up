package co.anbora.wakemeup.ui.addalarm;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
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
import co.anbora.wakemeup.databinding.AddAlarmDialogBinding;
import co.anbora.wakemeup.domain.model.AlarmGeofence;

/**
 * Created by dalgarins on 01/14/18.
 */

public class AddAlarmDialog extends DialogFragment implements AddAlarmContract.View, OnMapReadyCallback {

    private AddAlarmDialogBinding binding;
    private AddAlarmContract.Presenter presenter;
    private GoogleMap googleMap;
    private SupportMapFragment mapFragment;

    private Geocoder geocoder;

    private AlarmGeofence alarm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        binding = AddAlarmDialogBinding.inflate(inflater, container, false);
        mapFragment = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.add_map_alarm);
        mapFragment.getMapAsync(this);

        binding.dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                close();
            }
        });

        binding.okAddAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (alarm != null) {
                    presenter.addAlarm(alarm);
                    dismiss();
                }
            }
        });



        return binding.getRoot();
    }

    @Override
    public void showMarkerInMap(Double latitude, Double longitude) {

    }

    @Override
    public void close() {
        dismiss();
    }

    @Override
    public void searchAddress(LatLng latLng) {

        geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            List<Address> list = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (!list.isEmpty()) {
                binding.txtAddress.setText(list.get(0).getAddressLine(0));
                alarm = AlarmGeofenceModel.builder()
                        .id(UUID.randomUUID().toString())
                        .name("Alarma")
                        .description(list.get(0).getAddressLine(0))
                        .latitude(latLng.latitude)
                        .longitude(latLng.longitude)
                        .createdAt(new Date().getTime())
                        .state(true)
                        .needsSync(true)
                        .build();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setPresenter(AddAlarmContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        this.googleMap = googleMap;

        LatLng latLng = new LatLng(6.251608, -75.544006);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10);
        this.googleMap.animateCamera(cameraUpdate);

        this.googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                searchAddress(latLng);

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
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (mapFragment != null)
            getFragmentManager().beginTransaction().remove(mapFragment).commit();
    }
}
