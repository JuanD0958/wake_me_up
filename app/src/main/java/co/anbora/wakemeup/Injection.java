package co.anbora.wakemeup;

import android.content.Context;

import co.anbora.wakemeup.data.Sdk;
import co.anbora.wakemeup.data.SdkImpl;
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

}
