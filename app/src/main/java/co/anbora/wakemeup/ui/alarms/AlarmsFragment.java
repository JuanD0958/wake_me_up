package co.anbora.wakemeup.ui.alarms;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import co.anbora.wakemeup.adapter.alarms.AlarmsAdapter;
import co.anbora.wakemeup.databinding.FragmentAlarmsBinding;
import co.anbora.wakemeup.domain.model.AlarmGeofence;

public class AlarmsFragment extends Fragment {

    private FragmentAlarmsBinding binding;
    private AlarmsAdapter adapter;

    public AlarmsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentAlarmsBinding.inflate(inflater, container, false);
        adapter = new AlarmsAdapter(getActivity(), alarms());
        binding.rvList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvList.setItemAnimator(new DefaultItemAnimator());
        binding.rvList.setAdapter(adapter);
        return binding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public List<AlarmGeofence> alarms(){
        ArrayList<AlarmGeofence> list = new ArrayList<>();
        list.add(new AlarmGeofence() {
            @Override
            public Long internalId() {
                return 1L;
            }

            @Override
            public Long remoteId() {
                return 2L;
            }

            @Override
            public String id() {
                return "12345";
            }

            @Override
            public String name() {
                return "poc";
            }

            @Override
            public String description() {
                return "poc";
            }

            @Override
            public Boolean state() {
                return true;
            }

            @Override
            public Long createdAt() {
                return 1L;
            }

            @Override
            public Long updatedAt() {
                return 1L;
            }

            @Override
            public Long deletedAt() {
                return 1L;
            }

            @Override
            public Boolean needsSync() {
                return false;
            }
        });
        list.add(new AlarmGeofence() {
            @Override
            public Long internalId() {
                return 1L;
            }

            @Override
            public Long remoteId() {
                return 2L;
            }

            @Override
            public String id() {
                return "12345";
            }

            @Override
            public String name() {
                return "poc";
            }

            @Override
            public String description() {
                return "poc";
            }

            @Override
            public Boolean state() {
                return true;
            }

            @Override
            public Long createdAt() {
                return 1L;
            }

            @Override
            public Long updatedAt() {
                return 1L;
            }

            @Override
            public Long deletedAt() {
                return 1L;
            }

            @Override
            public Boolean needsSync() {
                return false;
            }
        });
        return list;
    }
}
