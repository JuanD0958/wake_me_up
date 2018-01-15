package co.anbora.wakemeup;

import android.app.Application;

import co.anbora.wakemeup.data.Sdk;

/**
 * Created by dalgarins on 01/13/18.
 */

public class WakeMeUpApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Sdk.init(this);
    }
}
