package in.sportscafe.nostragamus.module.inPlay.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sandip on 09/09/17.
 */

public class InPlayMatchRequest {

    @SerializedName("contest_id")
    private int contestId;

    public int getContestId() {
        return contestId;
    }

    public void setContestId(int contestId) {
        this.contestId = contestId;
    }
}
