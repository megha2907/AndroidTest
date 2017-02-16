package in.sportscafe.nostragamus.module.user.myprofile;

import android.support.v4.app.FragmentManager;

import com.jeeva.android.InAppView;

import in.sportscafe.nostragamus.module.common.ViewPagerAdapter;
import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;

/**
 * Created by Jeeva on 14/6/16.
 */
public interface ProfileView extends InAppView {

    void setName(String name);

    void setProfileImage(String imageUrl);

    void setLevel(String level);

    void setAccuracy(int accuracy);

    void setPoints(Long points);

    void setPredictionCount(Integer predictionCount);

    void updateSportTabTitle(String title);

    void setAdapter(ViewPagerAdapter adapter);

    void navigateToLogIn();

    FragmentManager getChildFragmentManager();
}