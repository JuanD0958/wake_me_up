package co.anbora.wakemeup;

import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.content.Context;

import co.anbora.wakemeup.data.Sdk;
import co.anbora.wakemeup.device.location.Callback;
import co.anbora.wakemeup.device.location.LocationComponentListenerImpl;
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

    public static LifecycleObserver provideLocationComponent(Activity context, Lifecycle lifecycle, Callback callback) {
        return new LocationComponentListenerImpl(context, lifecycle, callback);
    }
}
