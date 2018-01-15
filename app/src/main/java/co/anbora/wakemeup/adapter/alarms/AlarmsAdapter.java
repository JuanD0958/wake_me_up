package co.anbora.wakemeup.adapter.alarms;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.util.List;

import co.anbora.wakemeup.databinding.ItemAlarmBinding;
import co.anbora.wakemeup.domain.model.AlarmGeofence;
import co.anbora.wakemeup.ui.alarms.AlarmsContract;

public class AlarmsAdapter extends RecyclerView.Adapter<AlarmsAdapter.AlarmViewHolder> {

    private List<AlarmGeofence> alarms;
    private final AlarmsContract.Presenter presenter;

    public AlarmsAdapter(List<AlarmGeofence> alarms, AlarmsContract.Presenter presenter) {
        this.alarms = alarms;
        this.presenter = presenter;
    }

    public void setList(List<AlarmGeofence> alarms) {
        this.alarms = alarms;
        notifyDataSetChanged();
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
            binding.swEnable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    AlarmGeofence alarm = alarms.get(getAdapterPosition());
                    presenter.updateStateAlarm(alarm, b);
                }
            });
            binding.ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlarmGeofence alarm = alarms.get(getAdapterPosition());
                    presenter.deleteAlarm(alarm);
                }
            });
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            AlarmGeofence alarm = alarms.get(getAdapterPosition());
            presenter.showAlarm(alarm);
        }
    }
}
