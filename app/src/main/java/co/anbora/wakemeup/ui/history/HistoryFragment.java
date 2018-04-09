
package co.anbora.wakemeup.ui.history;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import co.anbora.wakemeup.Injection;
import co.anbora.wakemeup.R;
import co.anbora.wakemeup.adapter.alarms.AlarmsAdapter;
import co.anbora.wakemeup.databinding.FragmentHistoryBinding;
import co.anbora.wakemeup.domain.model.AlarmGeofence;
import co.anbora.wakemeup.ui.alarms.AlarmsContract;
import co.anbora.wakemeup.ui.notifiedalarm.NotifiedAlarmActivity;

public class HistoryFragment extends Fragment implements AlarmsContract.View {

    private FragmentHistoryBinding binding;
    private AlarmsAdapter adapter;
    private AlarmsContract.Presenter presenter;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        adapter = new AlarmsAdapter(new ArrayList<AlarmGeofence>(), presenter, false);
        binding.rvList.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvList.setItemAnimator(new DefaultItemAnimator());
        binding.rvList.setAdapter(adapter);

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.presenter.loadAlarms();
    }

    @Override
    public void showAlarms(List<AlarmGeofence> alarms) {
        if (alarms != null) {
            this.adapter.setList(alarms);
        }
    }

    @Override
    public void drawAllarmsInMap(List<AlarmGeofence> alarms) {

    }

    @Override
    public void showAddAlarm() {

    }

    @Override
    public void hideSwipeLayoutLoading() {

    }

    @Override
    public void viewAlarmOnMap(AlarmGeofence alarm) {

        Intent intent = Injection.provideActivityNotifiedIntent(getContext(), alarm.id());
        startActivity(intent);
    }

    @Override
    public void showAlarms() {

        this.presenter.loadAlarms();
    }

    @Override
    public void setPresenter(AlarmsContract.Presenter presenter) {

        this.presenter = presenter;
    }
}
