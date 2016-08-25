package in.sportscafe.scgame.module.user.myprofile;

import com.jeeva.android.InAppView;

import in.sportscafe.scgame.module.user.myprofile.myposition.dto.LbSummary;

/**
 * Created by Jeeva on 14/6/16.
 */
public interface ProfileView extends InAppView {

    void setName(String name);

    void setProfileImage(String imageUrl);

    void setSportsFollowedCount(int sportsFollowedCount);

    void setGroupsCount(int GroupsCount);

    void setPowerUpsCount(int PowerUpsCount);

    void setPoints(long points);

    void initMyPosition(LbSummary lbSummary);

    void navigateToLogIn();

    void navigateToPowerUpScreen();
}