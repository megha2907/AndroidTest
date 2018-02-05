package in.sportscafe.nostragamus.module.contest.dto.bumper;

import org.parceler.Parcel;

/**
 * Created by deepanshi on 2/2/18.
 */
@Parcel
public class BumperPrizesEstimationScreenData {

    private int rewardScreenLauncherParent;
    private String contestName;
    private int configId;
    private int roomId;

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

    public int getConfigId() {
        return configId;
    }

    public void setConfigId(int configId) {
        this.configId = configId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

}
