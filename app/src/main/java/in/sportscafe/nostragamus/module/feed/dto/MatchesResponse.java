package in.sportscafe.nostragamus.module.feed.dto;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;

/**
 * Created by Jeeva on 30/6/16.
 */
public class MatchesResponse {

    @SerializedName("data")
    private List<Match> matches = new ArrayList<>();


    @SerializedName("data")
    public List<Match> getMatches() {
        return matches;
    }

    @SerializedName("data")
    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }
}