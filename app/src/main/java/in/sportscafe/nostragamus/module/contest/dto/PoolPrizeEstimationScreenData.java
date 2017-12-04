package in.sportscafe.nostragamus.module.contest.dto;

import org.parceler.Parcel;

/**
 * Created by sc on 4/12/17.
 */
@Parcel
public class PoolPrizeEstimationScreenData {

    private int rewardScreenLauncherParent;
    private String contestName;

    public int getRewardScreenLauncherParent() {
        return rewardScreenLauncherParent;
    }

    public void setRewardScreenLauncherParent(int rewardScreenLauncherParent) {
        this.rewardScreenLauncherParent = rewardScreenLauncherParent;
    }

    public String getContestName() {
        return contestName;
    }

    public void setContestName(String contestName) {
        this.contestName = contestName;
    }
}
