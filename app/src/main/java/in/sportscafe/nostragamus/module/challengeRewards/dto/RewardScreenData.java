package in.sportscafe.nostragamus.module.challengeRewards.dto;

import org.parceler.Parcel;

/**
 * Created by sc on 2/12/17.
 */
@Parcel
public class RewardScreenData {

    private int roomId = -1;
    private int configId = -1;
    private boolean isPoolContest = true;
    private String contestName;

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getConfigId() {
        return configId;
    }

    public void setConfigId(int configId) {
        this.configId = configId;
    }

    public boolean isPoolContest() {
        return isPoolContest;
    }

    public void setPoolContest(boolean poolContest) {
        isPoolContest = poolContest;
    }

    public String getContestName() {
        return contestName;
    }

    public void setContestName(String contestName) {
        this.contestName = contestName;
    }
}
