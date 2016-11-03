package in.sportscafe.scgame.module.user.myprofile.myposition.dto;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by deepanshi on 10/13/16.
 */

public class ChallengesTourSummary extends BaseSummary implements Serializable {

    public ChallengesTourSummary() {
    }

    public ChallengesTourSummary(Integer tournamentId, String tournamentName, String tournamentImageUrl, Integer rank, Integer rankChange, Integer overallRank, Integer overallRankChange, Integer challengeId, String challengeName,String challengePhoto) {
        super(challengePhoto,challengeName, tournamentId, tournamentName, tournamentImageUrl, rank, rankChange, overallRank, overallRankChange);
        this.challengeId = challengeId;
        this.challengeName = challengeName;
        this.challengePhoto = challengePhoto;
    }

    @JsonProperty("challenge_id")
    private Integer challengeId;

    @JsonProperty("challenge_name")
    private String challengeName;

    @JsonIgnore
    private String challengePhoto;

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

    @JsonIgnore
    public String getChallengePhoto() {
        return challengePhoto;
    }

    @JsonIgnore
    public void setChallengePhoto(String challengePhoto) {
        this.challengePhoto = challengePhoto;
    }

}