package in.sportscafe.nostragamus.module.allchallenges.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.module.feed.dto.Match;
import in.sportscafe.nostragamus.module.feed.dto.TournamentPowerupInfo;
import in.sportscafe.nostragamus.module.tournamentFeed.dto.Tournament;

/**
 * Created by Jeeva on 17/02/17.
 */
@Parcel
public class Challenge {

    @JsonProperty("challenge_id")
    private int challengeId;

    @JsonProperty("challenge_name")
    private String name;

    @JsonProperty("challenge_desc")
    private String description;

    @JsonProperty("challenge_img_url")
    private String image;

    @JsonProperty("challenge_tournaments")
    private List<Tournament> tournaments = new ArrayList<>();

    @JsonProperty("challenge_starttime")
    private String startTime;

    @JsonProperty("challenge_endtime")
    private String endTime;

    @JsonProperty("matches")
    private List<Match> matches = new ArrayList<>();

    @JsonProperty("rank")
    private Integer userRank;

    @JsonProperty("rank_change")
    private Integer userRankChange;

    @JsonProperty("user_challenge_info")
    private ChallengeUserInfo challengeUserInfo;

    @JsonProperty("challenge_info")
    private ChallengeInfo challengeInfo;

    @JsonProperty("challenge_id")
    public int getChallengeId() {
        return challengeId;
    }

    @JsonProperty("challenge_id")
    public void setChallengeId(int challengeId) {
        this.challengeId = challengeId;
    }

    @JsonProperty("challenge_name")
    public String getName() {
        return name;
    }

    @JsonProperty("challenge_name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("challenge_desc")
    public String getDescription() {
        return description;
    }

    @JsonProperty("challenge_desc")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("challenge_tournaments")
    public List<Tournament> getTournaments() {
        return tournaments;
    }

    @JsonProperty("challenge_tournaments")
    public void setTournaments(List<Tournament> tournaments) {
        this.tournaments = tournaments;
    }

    @JsonProperty("challenge_starttime")
    public String getStartTime() {
        return startTime;
    }

    @JsonProperty("challenge_starttime")
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    @JsonProperty("challenge_endtime")
    public String getEndTime() {
        return endTime;
    }

    @JsonProperty("challenge_endtime")
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @JsonProperty("matches")
    public List<Match> getMatches() {
        return matches;
    }

    @JsonProperty("matches")
    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }

    @JsonProperty("challenge_image_url")
    public String getImage() {
        return image;
    }

    @JsonProperty("challenge_image_url")
    public void setImage(String image) {
        this.image = image;
    }

    @JsonProperty("challenge_user_rank")
    public Integer getUserRank() {
        return userRank;
    }

    @JsonProperty("challenge_user_rank")
    public void setUserRank(Integer userRank) {
        this.userRank = userRank;
    }

    @JsonProperty("rank_change")
    public Integer getUserRankChange() {
        return userRankChange;
    }

    @JsonProperty("rank_change")
    public void setUserRankChange(Integer userRankChange) {
        this.userRankChange = userRankChange;
    }


    @JsonProperty("user_challenge_info")
    public ChallengeUserInfo getChallengeUserInfo() {
        return challengeUserInfo;
    }

    @JsonProperty("user_challenge_info")
    public void setChallengeUserInfo(ChallengeUserInfo challengeUserInfo) {
        this.challengeUserInfo = challengeUserInfo;
    }

    @JsonProperty("challenge_info")
    public ChallengeInfo getChallengeInfo() {
        return challengeInfo;
    }

    @JsonProperty("challenge_info")
    public void setChallengeInfo(ChallengeInfo challengeInfo) {
        this.challengeInfo = challengeInfo;
    }

}