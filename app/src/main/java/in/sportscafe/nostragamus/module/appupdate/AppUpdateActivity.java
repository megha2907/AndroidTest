package in.sportscafe.nostragamus.module.appupdate;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.common.OnDismissListener;
import in.sportscafe.nostragamus.module.onboard.OnBoardingFragment;

/**
 * Created by deepanshi on 6/2/17.
 */

public class AppUpdateActivity extends NostragamusActivity implements View.OnClickListener, OnDismissListener {

    private static final int DISMISS_SCREEN = 58;

    @Override
    public String getScreenName() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_app);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.update_app_fl_holder, AppUpdateFragment.newInstance()).commit();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update_app_iv_back:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onDismiss(int requestCode, Bundle bundle) {
        switch (requestCode) {
            case DISMISS_SCREEN:
                onBackPressed();
                break;
        }
    }
}
