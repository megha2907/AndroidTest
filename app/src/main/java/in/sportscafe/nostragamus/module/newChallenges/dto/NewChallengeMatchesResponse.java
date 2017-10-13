package in.sportscafe.nostragamus.module.newChallenges.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import in.sportscafe.nostragamus.module.inPlay.dto.InPlayMatch;

/**
 * Created by sandip on 23/09/17.
 */

public class NewChallengeMatchesResponse {

    @SerializedName("contests_joined")
    private int contestJoined;

    @SerializedName("headless_joined")
    private int headLessJoined;

    @SerializedName("data")
    private List<InPlayMatch> matchList;

    public int getContestJoined() {
        return contestJoined;
    }

    public void setContestJoined(int contestJoined) {
        this.contestJoined = contestJoined;
    }

    public int getHeadLessJoined() {
        return headLessJoined;
    }

    public void setHeadLessJoined(int headLessJoined) {
        this.headLessJoined = headLessJoined;
    }

    public List<InPlayMatch> getMatchList() {
        return matchList;
    }

    public void setMatchList(List<InPlayMatch> matchList) {
        this.matchList = matchList;
    }
}
