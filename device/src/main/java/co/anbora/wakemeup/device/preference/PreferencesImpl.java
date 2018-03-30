package co.anbora.wakemeup.device.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by dalgarins on 03/25/18.
 */

public class PreferencesImpl implements Preferences {

     private final SharedPreferences sharedPreferences;

     public PreferencesImpl(final Context context) {
         this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
     }

    @Override
    public boolean getValue(String key) {

        return this.sharedPreferences
                .getBoolean(key, false);
    }

    @Override
    public void setValue(String key, boolean value) {

        this.sharedPreferences
                .edit()
                .putBoolean(key, value)
                .apply();
    }
}
