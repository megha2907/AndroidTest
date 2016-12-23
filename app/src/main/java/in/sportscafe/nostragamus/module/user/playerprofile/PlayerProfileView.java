package in.sportscafe.nostragamus.module.user.playerprofile;

import com.jeeva.android.InAppView;

/**
 * Created by deepanshi on 12/22/16.
 */

public interface PlayerProfileView extends InAppView {

    void setName(String name);

    void setProfileImage(String imageUrl);

    void setSportsFollowedCount(int sportsFollowedCount);

    void setGroupsCount(int GroupsCount);

    void setPoints(long points);

    void setBadgesCount(int badgesCount);
}
