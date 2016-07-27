package in.sportscafe.scgame.module.user.leaderboard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeeva on 30/6/16.
 */
public class LbSport {

    @JsonProperty("sports_id")
    private Integer sportId;

    @JsonProperty("weekly")
    private List<LeaderBoard> weeklyList = new ArrayList<>();

    @JsonProperty("monthly")
    private List<LeaderBoard> monthlyList = new ArrayList<>();

    @JsonProperty("alltime")
    private List<LeaderBoard> allTimeList = new ArrayList<>();

    @JsonProperty("sports_id")
    public Integer getSportId() {
        return sportId;
    }

    @JsonProperty("sports_id")
    public void setSportId(Integer sportId) {
        this.sportId = sportId;
    }

    @JsonProperty("weekly")
    public List<LeaderBoard> getWeeklyList() {
        return weeklyList;
    }

    @JsonProperty("weekly")
    public void setWeeklyList(List<LeaderBoard> weeklyList) {
        this.weeklyList = weeklyList;
    }

    @JsonProperty("monthly")
    public List<LeaderBoard> getMonthlyList() {
        return monthlyList;
    }

    @JsonProperty("monthly")
    public void setMonthlyList(List<LeaderBoard> monthlyList) {
        this.monthlyList = monthlyList;
    }

    @JsonProperty("alltime")
    public List<LeaderBoard> getAllTimeList() {
        return allTimeList;
    }

    @JsonProperty("alltime")
    public void setAllTimeList(List<LeaderBoard> allTimeList) {
        this.allTimeList = allTimeList;
    }
}