package in.sportscafe.nostragamus.module.user.leaderboard.dto;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeeva on 30/6/16.
 */
public class LbSport {

    @SerializedName("sports_id")
    private Integer sportId;

    @SerializedName("weekly")
    private List<LeaderBoard> weeklyList = new ArrayList<>();

    @SerializedName("monthly")
    private List<LeaderBoard> monthlyList = new ArrayList<>();

    @SerializedName("alltime")
    private List<LeaderBoard> allTimeList = new ArrayList<>();

    @SerializedName("sports_id")
    public Integer getSportId() {
        return sportId;
    }

    @SerializedName("sports_id")
    public void setSportId(Integer sportId) {
        this.sportId = sportId;
    }

    @SerializedName("weekly")
    public List<LeaderBoard> getWeeklyList() {
        return weeklyList;
    }

    @SerializedName("weekly")
    public void setWeeklyList(List<LeaderBoard> weeklyList) {
        this.weeklyList = weeklyList;
    }

    @SerializedName("monthly")
    public List<LeaderBoard> getMonthlyList() {
        return monthlyList;
    }

    @SerializedName("monthly")
    public void setMonthlyList(List<LeaderBoard> monthlyList) {
        this.monthlyList = monthlyList;
    }

    @SerializedName("alltime")
    public List<LeaderBoard> getAllTimeList() {
        return allTimeList;
    }

    @SerializedName("alltime")
    public void setAllTimeList(List<LeaderBoard> allTimeList) {
        this.allTimeList = allTimeList;
    }
}