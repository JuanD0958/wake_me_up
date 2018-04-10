package co.anbora.wakemeup.device.preference;

/**
 * Created by dalgarins on 03/25/18.
 */

public interface Preferences {

    boolean getBoolean(String key);

    String getString(String key);

    long getLong(String key);

    void setValue(String key, boolean value);

}
