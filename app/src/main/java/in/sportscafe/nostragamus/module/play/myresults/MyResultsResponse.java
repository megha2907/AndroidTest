package in.sportscafe.nostragamus.module.play.myresults;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.module.feed.dto.Match;

/**
 * Created by Jeeva on 5/7/16.
 */
public class MyResultsResponse {

    @SerializedName("data")
    private Match myResults;

    @SerializedName("data")
    public Match getMyResults() {
        return myResults;
    }

    @SerializedName("data")
    public void setMyResults(Match myResults) {
        this.myResults = myResults;
    }

}