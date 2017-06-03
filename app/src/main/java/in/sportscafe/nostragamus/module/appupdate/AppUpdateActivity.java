package in.sportscafe.nostragamus.module.appupdate;

import android.os.Bundle;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.onboard.OnBoardingFragment;

/**
 * Created by deepanshi on 6/2/17.
 */

public class AppUpdateActivity extends NostragamusActivity {

    @Override
    public String getScreenName() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_app);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.update_app_fl_holder, AppUpdateFragment.newInstance()).commit();

    }

}
