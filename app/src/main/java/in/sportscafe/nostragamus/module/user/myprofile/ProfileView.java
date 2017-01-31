package in.sportscafe.nostragamus.module.user.myprofile;

import com.jeeva.android.InAppView;

import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;

/**
 * Created by Jeeva on 14/6/16.
 */
public interface ProfileView extends InAppView {

    void setName(String name);

    void setProfileImage(String imageUrl);

    void setSportsFollowedCount(int sportsFollowedCount);

    void setGroupsCount(int GroupsCount);

    void setLevel(String level);

    void setPowerUpsCount(int PowerUpsCount);

    void setAccuracy(int accuracy);

    void setPoints(Long points);

    void initMyPosition(UserInfo userInfo);

    void navigateToLogIn();

    void navigateToPowerUpScreen();

    void navigateToBadgeScreen();

    void setBadgesCount(int badgesCount);

    void navigateToJoinGroup();

    void setPredictionCount(Integer predictionCount);
}