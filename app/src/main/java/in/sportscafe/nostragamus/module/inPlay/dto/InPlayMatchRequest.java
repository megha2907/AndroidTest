package in.sportscafe.nostragamus.module.inPlay.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by sandip on 09/09/17.
 */

public class InPlayMatchRequest {

    @JsonProperty("contest_id")
    private int contestId;

    public int getContestId() {
        return contestId;
    }

    public void setContestId(int contestId) {
        this.contestId = contestId;
    }
}
