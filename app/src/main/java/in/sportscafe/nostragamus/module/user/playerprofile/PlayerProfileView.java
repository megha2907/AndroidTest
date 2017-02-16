package in.sportscafe.nostragamus.module.user.playerprofile;

import android.os.Bundle;

import com.jeeva.android.InAppView;

import java.util.List;

import in.sportscafe.nostragamus.module.user.badges.Badge;
import in.sportscafe.nostragamus.module.user.playerprofile.dto.PlayerInfo;

/**
 * Created by deepanshi on 12/22/16.
 */

public interface PlayerProfileView extends InAppView {

    void setName(String name);

    void setProfileImage(String imageUrl);

    void setSportsFollowedCount(int sportsFollowedCount);

    void setGroupsCount(int GroupsCount);

    void setLevel(String level);

    void setPoints(long points);

    void setAccuracy(int accuracy);

    void setBadgesCount(int badgesCount, List<Badge> badgeList);

    void initMyPosition(PlayerInfo playerInfo);

    void setPredictionCount(Integer predictionCount);

    void navigateToPlayerComparison(Bundle playerInfoBundle);
}
