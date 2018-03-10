package co.anbora.wakemeup;

import android.content.Context;

import co.anbora.wakemeup.data.Sdk;
import co.anbora.wakemeup.device.location.LocationComponent;
import co.anbora.wakemeup.device.location.LocationSettings;
import co.anbora.wakemeup.device.location.OnLastLocationListener;
import co.anbora.wakemeup.device.notification.Notifications;
import co.anbora.wakemeup.device.notification.NotificationsImpl;
import co.anbora.wakemeup.device.vibration.Vibrations;
import co.anbora.wakemeup.device.vibration.VibrationsImpl;
import co.anbora.wakemeup.domain.repository.AlarmGeofenceRepository;
import co.anbora.wakemeup.domain.usecase.UseCaseHandler;
import co.anbora.wakemeup.domain.usecase.UseCaseUiThreadPool;
import co.anbora.wakemeup.domain.usecase.alarm.AddAlarm;
import co.anbora.wakemeup.domain.usecase.alarm.DeleteAlarm;
import co.anbora.wakemeup.domain.usecase.alarm.GetAlarms;
import co.anbora.wakemeup.domain.usecase.alarm.UpdateStateAlarm;
import co.anbora.wakemeup.executor.MainThreadImpl;

/**
 * Created by dalgarins on 01/13/18.
 */

public class Injection {

    private Injection() {}

    private static UseCaseUiThreadPool provideUiThreadPool() {
        return MainThreadImpl.getInstance();
    }

    private static AlarmGeofenceRepository provideRepository() {
        return Sdk.instance();
    }

    public static UseCaseHandler provideUseCaseHandler() {
        return UseCaseHandler.getInstance(provideUiThreadPool());
    }

    public static AddAlarm provideAddAlarm() {
        return new AddAlarm(provideRepository());
    }

    public static DeleteAlarm provideDeleteAlarm() {
        return new DeleteAlarm(provideRepository());
    }

    public static GetAlarms provideGetAlarms() {
        return new GetAlarms(provideRepository());
    }

    public static UpdateStateAlarm provideUpdateStateAlarm() {
        return new UpdateStateAlarm(provideRepository());
    }

    public static Notifications provideNotification(Context context){
        return new NotificationsImpl(context);
    }

    public static Vibrations provideVibrations(Context context) {
        return new VibrationsImpl(context);
    }

    public static OnLastLocationListener provideLocationComponent(Context context
            , LocationSettings locationSettings) {

        return new LocationComponent.Builder()
                .locationSettings(locationSettings)
                .build(context);
    }

    public static OnLastLocationListener provideLocationComponent(Context context
            , LocationSettings locationSettings
            , long meters
            , long seconds
            , int priority) {

        return new LocationComponent.Builder()
                .locationRequest(meters, seconds, priority)
                .locationSettings(locationSettings)
                .build(context);
    }
}
