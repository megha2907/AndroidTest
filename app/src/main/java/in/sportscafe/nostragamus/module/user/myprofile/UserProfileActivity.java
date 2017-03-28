package in.sportscafe.nostragamus.module.user.myprofile;


import android.os.Bundle;
import android.view.View;

import in.sportscafe.nostragamus.Constants.ScreenNames;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;

/**
 * Created by deepanshi on 12/22/16.
 */
public class UserProfileActivity extends NostragamusActivity {

    @Override
    public String getScreenName() {
        return ScreenNames.USER_PROFILE;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        getSupportFragmentManager().beginTransaction().replace(
                R.id.user_profile_fl_holder,
                ProfileFragment.newInstance(0, true, getIntent().getExtras())
        ).commit();
    }
}