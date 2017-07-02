package in.sportscafe.nostragamus.module.navigation.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.navigation.settings.about.AboutActivity;
import in.sportscafe.nostragamus.utils.FragmentHelper;

public class SettingsActivity extends NostragamusActivity implements SettingsFragmentListener {

    @Override
    public String getScreenName() {
        return Constants.ScreenNames.SETTINGS;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setShouldAnimateActivity(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings2);

        initialize();
        loadSettingsFragment();
    }

    private void initialize() {
        initToolbar();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.settings_toolbar);
        TextView tvToolbar = (TextView) findViewById(R.id.settings_toolbar_tv);
        tvToolbar.setText("Settings");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationIcon(R.drawable.back_icon_grey);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
    }

    private void loadSettingsFragment() {
        SettingsFragment fragment = new SettingsFragment();
        FragmentHelper.replaceFragment(this, R.id.fragment_container, fragment);
    }

    @Override
    public void onAboutNostragamusClicked() {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSendFeedbackClicked() {


    }

    @Override
    public void onLogoutClicked() {
        Nostragamus.getInstance().logout();
    }
}
