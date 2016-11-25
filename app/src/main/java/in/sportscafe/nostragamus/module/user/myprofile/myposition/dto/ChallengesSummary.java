package in.sportscafe.nostragamus.module.user.myprofile.myposition.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by deepanshi on 10/9/16.
 */
public class ChallengesSummary implements Serializable {

    @JsonProperty("challenge_id")
    private Integer challengeId;

    @JsonProperty("challenge_name")
    private String challengeName;

    @JsonProperty("rank")
    private Integer OverallRank;

    @JsonProperty("rank_change")
    private Integer OverallRankChange;

    @JsonProperty("tournaments")
    private List<ChallengesTourSummary> tourSummaryList;


    public ChallengesSummary(Integer challengeId) {
        this.challengeId = challengeId;
    }

    public ChallengesSummary() {
    }

    @JsonProperty("challenge_id")
    public Integer getChallengeId() {
        return challengeId;
    }

    @JsonProperty("challenge_id")
    public void setChallengeId(Integer challengeId) {
        this.challengeId = challengeId;
    }

    @JsonProperty("challenge_name")
    public String getChallengeName() {
        return challengeName;
    }

    @JsonProperty("challenge_name")
    public void setChallengeName(String challengeName) {
        this.challengeName = challengeName;
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
    public List<ChallengesTourSummary> getTourSummaryList() {
        return tourSummaryList;
    }

    @JsonProperty("tournaments")
    public void setTourSummaryList(List<ChallengesTourSummary> tourSummaryList) {
        this.tourSummaryList = tourSummaryList;
    }


}