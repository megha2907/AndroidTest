package in.sportscafe.scgame.module.common;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.jeeva.android.widgets.customfont.CustomButton;

import in.sportscafe.scgame.R;
import in.sportscafe.scgame.ScGameDataHandler;
import in.sportscafe.scgame.module.analytics.ScGameAnalytics;
import in.sportscafe.scgame.module.getstart.GetStartActivity;

/**
 * Created by deepanshi on 31/8/16.
 */
public class Settings extends AppCompatActivity implements View.OnClickListener {

    private Toolbar mtoolbar;
    private CustomButton mBtnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mBtnLogout=(CustomButton)findViewById(R.id.settings_btn_logout);
        mBtnLogout.setOnClickListener(this);

        initToolBar();

        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        String versionName = pInfo.versionName;

        TextView versioncode=(TextView)findViewById(R.id.version_code_number);
        versioncode.setText(versionName);

    }

    public void initToolBar() {
        mtoolbar = (Toolbar) findViewById(R.id.settings_toolbar);
        mtoolbar.setTitle("Settings");
        setSupportActionBar(mtoolbar);
        mtoolbar.setNavigationIcon(R.drawable.back_icon_grey);
        mtoolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }

        );
    }

    @Override
    public void onClick(View v) {
        onClickLogout();
    }

    private void onClickLogout() {
        ScGameDataHandler.getInstance().clearAll();
        navigateToLogIn();

        ScGameAnalytics.getInstance().trackLogOut();
    }

    private void navigateToLogIn() {
        Intent intent = new Intent(getApplicationContext(), GetStartActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
