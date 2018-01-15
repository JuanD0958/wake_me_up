package co.anbora.wakemeup.data;

import android.content.Context;
import android.support.annotation.NonNull;

import co.anbora.wakemeup.domain.model.AlarmGeofence;
import co.anbora.wakemeup.domain.repository.Repository;

public class SdkImpl implements Repository {

    private Repository repository;

    protected SdkImpl(@NonNull Context context){
        this.repository = Injection.provideRepository(context);
    }

    @Override
    public void getAlarms(LoadAlarmsCallback callback) {
        this.repository.getAlarms(callback);
    }

    @Override
    public void saveAlarm(AlarmGeofence alarm) {
        this.repository.saveAlarm(alarm);
    }

    @Override
    public void deleteAlarm(AlarmGeofence alarm) {
        this.repository.deleteAlarm(alarm);
    }

    @Override
    public void updateAlarm(AlarmGeofence alarm, Boolean state) {
        this.repository.updateAlarm(alarm, state);
    }
}
