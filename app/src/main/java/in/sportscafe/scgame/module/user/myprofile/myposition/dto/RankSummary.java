package in.sportscafe.scgame.module.user.myprofile.myposition.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by Jeeva on 11/7/16.
 */
public class RankSummary implements Serializable {

    @JsonProperty("sports_id")
    private Integer sportId;

    @JsonProperty("sports_name")
    private String sportName;

    @JsonProperty("rank")
    private Integer rank;

    @JsonProperty("rank_change")
    private Integer rankChange;

    @JsonProperty("user_points")
    private Long userPoints;

    @JsonIgnore
    private Long groupId;

    public RankSummary() {
    }

    public RankSummary(Integer sportId, String sportName) {
        this.sportId = sportId;
        this.sportName = sportName;
    }

    @JsonProperty("sports_id")
    public Integer getSportId() {
        return sportId;
    }

    @JsonProperty("sports_id")
    public void setSportId(Integer sportId) {
        this.sportId = sportId;
    }

    @JsonProperty("sports_name")
    public String getSportName() {
        return sportName;
    }

    @JsonProperty("sports_name")
    public void setSportName(String sportName) {
        this.sportName = sportName;
    }

    @JsonProperty("rank")
    public Integer getRank() {
        return rank;
    }

    @JsonProperty("rank")
    public void setRank(Integer rank) {
        this.rank = rank;
    }

    @JsonProperty("rank_change")
    public Integer getRankChange() {
        return rankChange;
    }

    @JsonProperty("rank_change")
    public void setRankChange(Integer rankChange) {
        this.rankChange = rankChange;
    }

    @JsonProperty("user_points")
    public Long getUserPoints() {
        return userPoints;
    }

    @JsonProperty("user_points")
    public void setUserPoints(Long userPoints) {
        this.userPoints = userPoints;
    }

    @JsonIgnore
    public Long getGroupId() {
        return groupId;
    }

    @JsonIgnore
    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
}