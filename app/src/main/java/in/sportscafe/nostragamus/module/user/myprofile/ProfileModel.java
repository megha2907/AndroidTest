package in.sportscafe.nostragamus.module.user.myprofile;

import android.support.v4.app.FragmentManager;

import in.sportscafe.nostragamus.module.common.ViewPagerAdapter;
import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;

/**
 * Created by Jeeva on 14/6/16.
 */
public interface ProfileModel {

    void getProfileDetails();

    UserInfo getUserInfo();

    ViewPagerAdapter getAdapter(FragmentManager fm);
}