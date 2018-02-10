package co.anbora.wakemeup.ui.addalarm;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import co.anbora.wakemeup.Injection;
import co.anbora.wakemeup.R;
import co.anbora.wakemeup.databinding.ActivityAddAlarmBinding;

public class AddAlarmActivity extends AppCompatActivity {

    private ActivityAddAlarmBinding binding;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupUI();
        setAddAlarmUI();
        setupAddAlarmUX();
    }

    private void setAddAlarmUI() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_main, fragment = new AddAlarmFragment()).commit();
    }

    private void setupAddAlarmUX() {
        new AddAlarmPresenter(
                Injection.provideUseCaseHandler(),
                Injection.provideAddAlarm(),
                ((AddAlarmFragment) fragment)
        );
    }

    private void setupUI() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_alarm);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setDisplayShowHomeEnabled(true);
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
