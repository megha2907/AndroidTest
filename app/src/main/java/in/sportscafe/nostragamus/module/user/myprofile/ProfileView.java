package in.sportscafe.nostragamus.module.user.myprofile;

import com.jeeva.android.InAppView;

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

    void initMyPosition(int totalMatchesPlayed, int badgesCount, int sportsFollowedCount, int powerupsCount);

    void navigateToLogIn();

    void navigateToPowerUpScreen();

    void setPredictionCount(Integer predictionCount);
}