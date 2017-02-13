package in.sportscafe.nostragamus.module.user.comparisons;

import in.sportscafe.nostragamus.module.user.playerprofile.dto.PlayerInfo;

/**
 * Created by deepanshi on 2/11/17.
 */

public interface PlayerComparisonView {
    void setName(String userName, String playerName);

    void setProfileImage(String userImageUrl, String playerImageUrl);

    void setSportsFollowedCount(int userSportsFollowedCount, int playerSportsFollowedCount);

    void setLevel(String userLevel, String playerLevel);

    void setPoints(long userPoints, long playerPoints);

    void setAccuracy(int userAccuracy, int playerAccuracy);

    void setPredictionCount(Integer userPredictionCount, Integer playerPredictionCount);

    void initMyPosition(PlayerInfo playerInfo);
}
