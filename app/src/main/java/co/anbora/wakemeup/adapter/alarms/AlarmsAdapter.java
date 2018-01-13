package co.anbora.wakemeup.adapter.alarms;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.util.List;

import co.anbora.wakemeup.databinding.ItemAlarmBinding;
import co.anbora.wakemeup.domain.model.AlarmGeofence;

public class AlarmsAdapter extends RecyclerView.Adapter<AlarmsAdapter.AlarmViewHolder> {

    private Context context;
    private List<AlarmGeofence> alarms;
    private LayoutInflater inflater;

    public AlarmsAdapter(Context context, List<AlarmGeofence> alarms) {
        this.context = context;
        this.alarms = alarms;
    }

    @Override
    public AlarmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ItemAlarmBinding binding = ItemAlarmBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new AlarmViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(AlarmViewHolder holder, int position) {

        holder.bind(alarms.get(position));
    }

    @Override
    public int getItemCount() {
        if (alarms != null) {
            return alarms.size();
        }
        return 0;
    }


    class AlarmViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ItemAlarmBinding binding;

        public AlarmViewHolder(ItemAlarmBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }


        void bind(@NonNull AlarmGeofence alarm) {
            binding.setAlarm(alarm);
            binding.executePendingBindings();
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
