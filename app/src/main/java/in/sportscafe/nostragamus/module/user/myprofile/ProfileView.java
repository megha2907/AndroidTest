package in.sportscafe.nostragamus.module.user.myprofile;

import com.jeeva.android.InAppView;

import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;
import in.sportscafe.nostragamus.module.user.myprofile.myposition.dto.LbSummary;

/**
 * Created by Jeeva on 14/6/16.
 */
public interface ProfileView extends InAppView {

    void setName(String name);

    void setProfileImage(String imageUrl);

    void setSportsFollowedCount(int sportsFollowedCount);

    void setGroupsCount(int GroupsCount);

    void setPowerUpsCount(int PowerUpsCount);

    void setPoints(int points);

    void initMyPosition(UserInfo userInfo);

    void navigateToLogIn();

    void navigateToPowerUpScreen();

    void navigateToBadgeScreen();

    void setBadgesCount(int badgesCount);

    void navigateToJoinGroup();
}