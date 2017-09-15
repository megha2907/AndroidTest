package in.sportscafe.nostragamus.module.navigation.submitquestion.tourlist;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import in.sportscafe.nostragamus.module.feed.dto.Match;

/**
 * Created by Jeeva on 24/03/17.
 */
public class Tour {

    @SerializedName("id")
    private int id;

    @SerializedName("tournament_name")
    private String name;

    @SerializedName("matches")
    private List<Match> matchList;

    @SerializedName("id")
    public int getId() {
        return id;
    }

    @SerializedName("id")
    public void setId(int id) {
        this.id = id;
    }

    @SerializedName("tournament_name")
    public String getName() {
        return name;
    }

    @SerializedName("tournament_name")
    public void setName(String name) {
        this.name = name;
    }

    @SerializedName("matches")
    public List<Match> getMatchList() {
        return matchList;
    }

    @SerializedName("matches")
    public void setMatchList(List<Match> matchList) {
        this.matchList = matchList;
    }
}