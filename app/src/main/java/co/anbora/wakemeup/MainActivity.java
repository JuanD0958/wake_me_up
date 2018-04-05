package co.anbora.wakemeup;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import co.anbora.wakemeup.background.shared.preferences.SharedPreferencesManager;
import co.anbora.wakemeup.databinding.ActivityMainBinding;
import co.anbora.wakemeup.background.service.LocationUpdateService;
import co.anbora.wakemeup.ui.about.AboutFragment;
import co.anbora.wakemeup.ui.alarms.AlarmsFragment;
import co.anbora.wakemeup.ui.alarms.AlarmsPresenter;
import co.anbora.wakemeup.ui.settings.SettingsActivity;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
        , SharedPreferences.OnSharedPreferenceChangeListener {

    private ActivityMainBinding binding;
    private Fragment fragment;

    // A reference to the service used to get location updates.
    private LocationUpdateService mService = null;

    // Tracks the bound state of the service.
    private boolean mBound = false;

    private SharedPreferencesManager sharedPreferencesManager;

    // Monitors the state of the connection to the service.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocationUpdateService.LocalBinder binder = (LocationUpdateService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            mBound = false;
        }
    };

    // The BroadcastReceiver used to listen from broadcasts from the service.
    private MyReceiver myReceiver;

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver,
                new IntentFilter(Constants.ACTION_BROADCAST));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver);
    }

    @Override
    protected void onStop() {
        if (mBound) {
            // Unbind from the service. This signals to the service that this activity is no longer
            // in the foreground, and the service can respond by promoting itself to a foreground
            // service.
            unbindService(mServiceConnection);
            mBound = false;
        }
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupUI();
        setupUX();
        toServiceFragment();
        Injection.provideVibrations(getApplicationContext()).cancel();
        myReceiver = new MyReceiver();

    }

    private void setupUI() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setSupportActionBar(binding.contentLayout.toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, binding.drawerLayout, binding.contentLayout.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void setupUX() {
        binding.navView.setNavigationItemSelectedListener(this);
        sharedPreferencesManager = Injection.provideSharedPreferencesManager(getApplicationContext());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_principal) {
            toServiceFragment();
        } else if (id == R.id.nav_history) {

        } else if (id == R.id.nav_about) {
            toAboutFragment();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void toServiceFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_main, fragment = new AlarmsFragment()).commit();

        setupAlarmsUX();
    }

    public void toAboutFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_main, fragment = new AboutFragment()).commit();
    }

    private void setupAlarmsUX() {
        new AlarmsPresenter(
                Injection.provideUseCaseHandler(),
                ((AlarmsFragment) fragment),
                Injection.provideAddAlarm(),
                Injection.provideDeleteAlarm(),
                Injection.provideGetAlarms(),
                Injection.provideUpdateStateAlarm()
        );
    }

    private void bindLocationServices() {
        // Bind to the service. If the service is in foreground mode, this signals to the service
        // that since this activity is in the foreground, the service can exit foreground mode.
        bindService(new Intent(this, LocationUpdateService.class), mServiceConnection,
                Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

        if (Constants.GEOFENCES_ADDED_KEY.equals(s)) {
            if (sharedPreferencesManager.addedGeofence()){
                if (mService != null) {
                    mService.requestLocation();
                }
            } else {
                if (mService != null) {
                    mService.removeLocation();
                }
            }
        }
    }

    /**
     * Receiver for broadcasts sent by {@link LocationUpdateService}.
     */
    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Location location = intent.getParcelableExtra(Constants.EXTRA_LOCATION);
            if (location != null) {

            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);
        bindLocationServices();
    }
}
