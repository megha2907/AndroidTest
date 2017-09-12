package in.sportscafe.nostragamus.module.inPlay.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by sandip on 09/09/17.
 */

public class InPlayMatchesResponse {

    @JsonProperty("contest_id")
    private int contestId;

    @JsonProperty("matches")
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
