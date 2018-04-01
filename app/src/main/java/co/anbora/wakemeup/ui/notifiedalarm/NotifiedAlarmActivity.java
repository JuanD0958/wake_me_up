package co.anbora.wakemeup.ui.notifiedalarm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import co.anbora.wakemeup.Injection;
import co.anbora.wakemeup.R;

public class NotifiedAlarmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notified_alarm);
        Injection.provideVibrations(getApplicationContext()).cancel();
    }
}
