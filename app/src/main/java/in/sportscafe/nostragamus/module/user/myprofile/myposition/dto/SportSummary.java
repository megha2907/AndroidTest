package in.sportscafe.nostragamus.module.user.myprofile.myposition.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by deepanshi on 10/9/16.
 */
public class SportSummary implements Serializable {

    @JsonProperty("sports_id")
    private Integer sportsId;

    @JsonProperty("sports_name")
    private String sportsName;

    @JsonProperty("rank")
    private Integer OverallRank;

    @JsonProperty("rank_change")
    private Integer OverallRankChange;

    @JsonProperty("tournaments")
    private List<TourSummary> tourSummaryList;

    public SportSummary(Integer sportsId, String sportsName) {
        this.sportsId = sportsId;
        this.sportsName = sportsName;
    }

    public SportSummary() {
    }

    @JsonProperty("sports_id")
    public Integer getSportsId() {
        return sportsId;
    }

    @JsonProperty("sports_id")
    public void setSportsId(Integer sportsId) {
        this.sportsId = sportsId;
    }

    @JsonProperty("sports_name")
    public String getSportsName() {
        return sportsName;
    }

    @JsonProperty("sports_name")
    public void setSportsName(String sportsName) {
        this.sportsName = sportsName;
    }

    @JsonProperty("rank")
    public Integer getOverallRank() {
        return OverallRank;
    }

    @JsonProperty("rank")
    public void setOverallRank(Integer overallRank) {
        OverallRank = overallRank;
    }

    @JsonProperty("rank_change")
    public Integer getOverallRankChange() {
        return OverallRankChange;
    }

    @JsonProperty("rank_change")
    public void setOverallRankChange(Integer overallRankChange) {
        OverallRankChange = overallRankChange;
    }

    @JsonProperty("tournaments")
    public List<TourSummary> getTourSummaryList() {
        return tourSummaryList;
    }

    @JsonProperty("tournaments")
    public void setTourSummaryList(List<TourSummary> tourSummaryList) {
        this.tourSummaryList = tourSummaryList;
    }

}