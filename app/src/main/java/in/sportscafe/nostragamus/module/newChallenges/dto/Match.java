package in.sportscafe.nostragamus.module.newChallenges.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

import java.util.List;

import in.sportscafe.nostragamus.module.feed.dto.Topics;

/**
 * Created by sandip on 30/08/17.
 */

@Parcel
public class Match {

    @JsonProperty("match_id")
    private int matchId;

    @JsonProperty("match_stage")
    private String matchStage;

    @JsonProperty("match_venue")
    private String matchVenue;

    @JsonProperty("match_type")
    private String matchType;

    @JsonProperty("match_parties")
    private List<MatchParty> matchParties = null;

    @JsonProperty("match_topic")
    private Topics topics;

    @JsonProperty("match_starttime")
    private String matchStarttime;

    @JsonProperty("match_endtime")
    private String matchEndtime;

    @JsonProperty("status")
    private String matchStatus;

    @JsonProperty("is_attempted")
    private Integer isAttempted;

    @JsonProperty("sports_id")
    private Integer sportId;

    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public String getMatchStage() {
        return matchStage;
    }

    public void setMatchStage(String matchStage) {
        this.matchStage = matchStage;
    }

    public String getMatchVenue() {
        return matchVenue;
    }

    public void setMatchVenue(String matchVenue) {
        this.matchVenue = matchVenue;
    }

    public String getMatchType() {
        return matchType;
    }

    public void setMatchType(String matchType) {
        this.matchType = matchType;
    }

    public List<MatchParty> getMatchParties() {
        return matchParties;
    }

    public void setMatchParties(List<MatchParty> matchParties) {
        this.matchParties = matchParties;
    }

    public String getMatchStarttime() {
        return matchStarttime;
    }

    public void setMatchStarttime(String matchStarttime) {
        this.matchStarttime = matchStarttime;
    }

    public String getMatchEndtime() {
        return matchEndtime;
    }

    public void setMatchEndtime(String matchEndtime) {
        this.matchEndtime = matchEndtime;
    }

    public Topics getTopics() {
        return topics;
    }

    public void setTopics(Topics topics) {
        this.topics = topics;
    }

    public String getMatchStatus() {
        return matchStatus;
    }

    public void setMatchStatus(String matchStatus) {
        this.matchStatus = matchStatus;
    }

    public Integer getIsAttempted() {
        return isAttempted;
    }

    public void setIsAttempted(Integer isAttempted) {
        this.isAttempted = isAttempted;
    }

    public Integer getSportId() {
        return sportId;
    }

    public void setSportId(Integer sportId) {
        this.sportId = sportId;
    }
}
