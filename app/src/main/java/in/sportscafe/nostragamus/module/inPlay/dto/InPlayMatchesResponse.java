package in.sportscafe.nostragamus.module.inPlay.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sandip on 09/09/17.
 */

public class InPlayMatchesResponse {

    @SerializedName("contest_id")
    private int contestId;

    @SerializedName("data")
    private List<InPlayMatch> inPlayMatchList;

    public int getContestId() {
        return contestId;
    }

    public void setContestId(int contestId) {
        this.contestId = contestId;
    }

    public List<InPlayMatch> getInPlayMatchList() {
        return inPlayMatchList;
    }

    public void setInPlayMatchList(List<InPlayMatch> inPlayMatchList) {
        this.inPlayMatchList = inPlayMatchList;
    }
}
