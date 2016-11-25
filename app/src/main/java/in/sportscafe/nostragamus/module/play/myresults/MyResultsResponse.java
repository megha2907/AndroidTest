package in.sportscafe.nostragamus.module.play.myresults;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.module.feed.dto.Match;

/**
 * Created by Jeeva on 5/7/16.
 */
public class MyResultsResponse {

    @JsonProperty("data")
    private List<Match> myResults = new ArrayList<>();

    @JsonProperty("data")
    public List<Match> getMyResults() {
        return myResults;
    }

    @JsonProperty("data")
    public void setMyResults(List<Match> myResults) {
        this.myResults = myResults;
    }
}