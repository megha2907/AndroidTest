package in.sportscafe.nostragamus.module.navigation.submitquestion.tourlist;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import in.sportscafe.nostragamus.module.feed.dto.Match;

/**
 * Created by Jeeva on 24/03/17.
 */
public class Tour {

    @JsonProperty("id")
    private int id;

    @JsonProperty("tournament_name")
    private String name;

    @JsonProperty("matches")
    private List<Match> matchList;

    @JsonProperty("id")
    public int getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(int id) {
        this.id = id;
    }

    @JsonProperty("tournament_name")
    public String getName() {
        return name;
    }

    @JsonProperty("tournament_name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("matches")
    public List<Match> getMatchList() {
        return matchList;
    }

    @JsonProperty("matches")
    public void setMatchList(List<Match> matchList) {
        this.matchList = matchList;
    }
}