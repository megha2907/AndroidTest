package in.sportscafe.nostragamus.module.newChallenges.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import in.sportscafe.nostragamus.module.inPlay.dto.InPlayMatch;

/**
 * Created by sandip on 23/09/17.
 */

public class NewChallengeMatchesResponse {

    @SerializedName("data")
    private List<InPlayMatch> matchList;

    public List<InPlayMatch> getMatchList() {
        return matchList;
    }

    public void setMatchList(List<InPlayMatch> matchList) {
        this.matchList = matchList;
    }
}
