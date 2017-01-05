package in.sportscafe.nostragamus.module.user.playerprofile;

import com.jeeva.android.InAppView;

import java.util.List;

import in.sportscafe.nostragamus.module.user.myprofile.myposition.dto.LbSummary;
import in.sportscafe.nostragamus.module.user.playerprofile.dto.PlayerInfo;

/**
 * Created by deepanshi on 12/22/16.
 */

public interface PlayerProfileView extends InAppView {

    void setName(String name);

    void setProfileImage(String imageUrl);

    void setSportsFollowedCount(int sportsFollowedCount);

    void setGroupsCount(int GroupsCount);

    void setPoints(long points);

    void setBadgesCount(int badgesCount,List<String> badgeList);

    void initMyPosition(PlayerInfo playerInfo);
}
