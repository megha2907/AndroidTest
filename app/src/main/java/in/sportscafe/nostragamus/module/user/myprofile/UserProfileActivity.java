package in.sportscafe.nostragamus.module.user.myprofile;


import android.os.Bundle;
import android.view.View;

import in.sportscafe.nostragamus.Constants.ScreenNames;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.utils.FragmentHelper;

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
        setShouldAnimateActivity(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        loadProfileFragment();
    }

    private void loadProfileFragment() {
        Bundle args = null;
        if (getIntent() != null) {
            args = getIntent().getExtras();
        }

        ProfileFragment profileFragment = ProfileFragment.newInstance(0, true, args);
        FragmentHelper.replaceFragment(this, R.id.user_profile_fl_holder, profileFragment);
    }
}