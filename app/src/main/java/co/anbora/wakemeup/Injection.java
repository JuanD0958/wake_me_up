package co.anbora.wakemeup;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;

import co.anbora.wakemeup.background.DisableAlarmBroadCastReceiver;
import co.anbora.wakemeup.background.factory.NotificationFactory;
import co.anbora.wakemeup.background.factory.NotificationFactoryImpl;
import co.anbora.wakemeup.background.service.LocationUpdateService;
import co.anbora.wakemeup.background.shared.preferences.SharedPreferencesManager;
import co.anbora.wakemeup.background.shared.preferences.SharedPreferencesManagerImpl;
import co.anbora.wakemeup.data.Sdk;
import co.anbora.wakemeup.device.alarm.Alarms;
import co.anbora.wakemeup.device.alarm.AlarmsImpl;
import co.anbora.wakemeup.device.location.LocationComponent;
import co.anbora.wakemeup.device.location.LocationSettings;
import co.anbora.wakemeup.device.location.OnLastLocationListener;
import co.anbora.wakemeup.device.notification.Notifications;
import co.anbora.wakemeup.device.notification.NotificationsImpl;
import co.anbora.wakemeup.device.preference.Preferences;
import co.anbora.wakemeup.device.preference.PreferencesImpl;
import co.anbora.wakemeup.device.vibration.Vibrations;
import co.anbora.wakemeup.device.vibration.VibrationsImpl;
import co.anbora.wakemeup.domain.mapper.Mapper;
import co.anbora.wakemeup.domain.model.AlarmGeofence;
import co.anbora.wakemeup.domain.model.HistoryAlarm;
import co.anbora.wakemeup.domain.repository.AlarmGeofenceRepository;
import co.anbora.wakemeup.domain.repository.HistoryAlarmRepository;
import co.anbora.wakemeup.domain.usecase.UseCaseHandler;
import co.anbora.wakemeup.domain.usecase.UseCaseUiThreadPool;
import co.anbora.wakemeup.domain.usecase.alarm.AddAlarm;
import co.anbora.wakemeup.domain.usecase.alarm.CheckAlarmAsActivated;
import co.anbora.wakemeup.domain.usecase.alarm.DeleteAlarm;
import co.anbora.wakemeup.domain.usecase.alarm.GetAlarms;
import co.anbora.wakemeup.domain.usecase.alarm.UpdateStateAlarm;
import co.anbora.wakemeup.executor.MainThreadImpl;
import co.anbora.wakemeup.mapper.AlarmToHistoryMapper;
import co.anbora.wakemeup.ui.model.NotificationViewModel;
import co.anbora.wakemeup.ui.notifiedalarm.NotifiedAlarmActivity;

/**
 * Created by dalgarins on 01/13/18.
 */

public class Injection {

    private static class SingletonHelper {
        private final static Mapper<AlarmGeofence, HistoryAlarm> MAPPER = new AlarmToHistoryMapper();
    }

    private Injection() {}

    public static UseCaseUiThreadPool provideUiThreadPool() {
        return MainThreadImpl.getInstance();
    }

    public static AlarmGeofenceRepository provideRepository() {
        return Sdk.instance();
    }

    public static HistoryAlarmRepository provideHistoryRepository() {
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

    public static CheckAlarmAsActivated provideCheckAlarmActivated() {
        return new CheckAlarmAsActivated(provideHistoryRepository(), provideHistoryMapper());
    }

    public static Notifications provideNotificationManager(Context context){
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
            , long interval
            , long fastInterval
            , int priority) {

        return new LocationComponent.Builder()
                .locationRequest(interval, fastInterval, priority)
                .locationSettings(locationSettings)
                .build(context);
    }

    public static NotificationFactory provideNotificationFactory(final Context context, final Resources resources){

        return new NotificationFactoryImpl(context, resources);
    }

    public static NotificationViewModel provideNotificationViewModel(String title, String content) {

        return new NotificationViewModel(title, content);
    }

    public static Intent provideServiceLocationIntent(final Context context) {

        Intent intent =  new Intent(context, LocationUpdateService.class);
        // Extra to help us figure out if we arrived in onStartCommand via the notification or not.
        intent.putExtra(Constants.EXTRA_STARTED_FROM_NOTIFICATION, true);
        return intent;
    }

    /**
     * The PendingIntent that leads to a call to onStartCommand() in this service.
     * @param context
     * @param intent
     * @return
     */
    public static PendingIntent provideServicePendingIntent(final Context context, Intent intent) {

        return PendingIntent.getService(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static Intent provideActivityMainIntent(final Context context) {

        return new Intent(context, MainActivity.class);
    }

    public static Intent provideActivityNotifiedIntent(final Context context) {

        return new Intent(context, NotifiedAlarmActivity.class);
    }

    /**
     * The PendingIntent to launch activity.
     * @param context
     * @param intent
     * @return
     */
    public static PendingIntent provideActivityPendingIntent(final Context context, Intent intent) {

        return PendingIntent.getActivity(context, 0, intent, 0);
    }

    /**
     * Provide notification for service foreground
     * @param title
     * @param content
     * @param context
     * @param resources
     * @return
     */
    public static Notification provideNotificationForegroundService(String title, String content,
                                                                    final Context context, final Resources resources) {

        NotificationFactory factory = provideNotificationFactory(context, resources);

        PendingIntent servicePendingIntent = provideServicePendingIntent(context, provideServiceLocationIntent(context));

        PendingIntent activityPendingIntent = provideActivityPendingIntent(context, provideActivityMainIntent(context));

        return factory.createForegroundServiceNotification(provideNotificationViewModel(title, content),
                servicePendingIntent,
                activityPendingIntent);
    }

    public static TaskStackBuilder provideTaskStackBuilder(final Context context, Intent intent) {

        // Construct a task stack.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        // Add the main Activity to the task stack as the parent.
        stackBuilder.addParentStack(MainActivity.class);
        // Push the content Intent onto the stack.
        stackBuilder.addNextIntent(intent);

        return stackBuilder;
    }

    public static PendingIntent provideNotificationPendingIntent(final Context context) {

        TaskStackBuilder stackBuilder = provideTaskStackBuilder(context, provideActivityNotifiedIntent(context));
        // Get a PendingIntent containing the entire back stack.
        return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
    }


    /**
     * Provide notification when alarm is active
     * @param title
     * @param content
     * @param context
     * @param resources
     * @return
     */
    public static Notification provideNotificationAlarmDetected(String title, String content,
                                                                final Context context,
                                                                final Resources resources){

        NotificationFactory factory = provideNotificationFactory(context, resources);

        return factory.createActiveAlarmNotification(
                provideNotificationViewModel(title, content),
                provideNotificationPendingIntent(context));
    }

    public static Preferences providePreferences(final Context context) {

        return new PreferencesImpl(context);
    }

    public static SharedPreferencesManager provideSharedPreferencesManager(final Context context) {

        return new SharedPreferencesManagerImpl(providePreferences(context));
    }

    public static Mapper<AlarmGeofence, HistoryAlarm> provideHistoryMapper() {
        return SingletonHelper.MAPPER;
    }

    public static Intent provideDisableAlarmIntent(Notification notification) {

        Intent snoozeIntent = new Intent(Constants.ACTION_BROADCAST_ALARM_DISABLE);
        snoozeIntent.putExtra(Constants.DISABLE_ALARM, true);
        snoozeIntent.putExtra(Constants.NOTIFICATION, notification);

        return snoozeIntent;
    }

    public static PendingIntent provideBroadcastPendingIntent(final Context context, Notification notification) {

        return PendingIntent.getBroadcast(context, 0,
                provideDisableAlarmIntent(notification),
                PendingIntent.FLAG_CANCEL_CURRENT);
    }

    public static BroadcastReceiver provideDisableAlarmBroadcast() {
        return new DisableAlarmBroadCastReceiver();
    }

    public static Alarms provideAlarmManager(final Context context) {

        return new AlarmsImpl(context);
    }

}
