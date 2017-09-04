package in.sportscafe.nostragamus.module.newChallenges.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;
import in.sportscafe.nostragamus.module.newChallenges.adapter.NewChallengeAdapterItemType;

/**
 * Created by sandip on 29/08/17.
 */

@Parcel
public class NewChallengesResponse {

    @JsonProperty("id")
    private int id;

    @JsonProperty("challenge_name")
    private String challengeName;

    @JsonProperty("challenge_starttime")
    private String challengeStartTime;

    @JsonProperty("challenge_img_url")
    private String challengeImgUrl;

    @JsonProperty("challenge_endtime")
    private String challengeEndTime;

    @JsonProperty("sports_id")
    private int sportsId;

    @JsonProperty("challenge_tournaments")
    private List<Integer> challengeTournaments = null;

    @JsonProperty("challenge_desc")
    private String challengeDesc;

    @JsonProperty("createdAt")
    private String createdAt;

    @JsonProperty("updatedAt")
    private String updatedAt;

    @JsonProperty("matches")
    private List<Match> matches = new ArrayList<>();


    private int challengeAdapterItemType = NewChallengeAdapterItemType.CHALLENGE;

    public int getChallengeAdapterItemType() {
        return challengeAdapterItemType;
    }

    public void setChallengeAdapterItemType(int challengeAdapterItemType) {
        this.challengeAdapterItemType = challengeAdapterItemType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChallengeName() {
        return challengeName;
    }

    public void setChallengeName(String challengeName) {
        this.challengeName = challengeName;
    }

    public String getChallengeStartTime() {
        return challengeStartTime;
    }

    public void setChallengeStartTime(String challengeStartTime) {
        this.challengeStartTime = challengeStartTime;
    }

    public String getChallengeImgUrl() {
        return challengeImgUrl;
    }

    public void setChallengeImgUrl(String challengeImgUrl) {
        this.challengeImgUrl = challengeImgUrl;
    }

    public String getChallengeEndTime() {
        return challengeEndTime;
    }

    public void setChallengeEndTime(String challengeEndTime) {
        this.challengeEndTime = challengeEndTime;
    }

    public int getSportsId() {
        return sportsId;
    }

    public void setSportsId(int sportsId) {
        this.sportsId = sportsId;
    }

    public List<Integer> getChallengeTournaments() {
        return challengeTournaments;
    }

    public void setChallengeTournaments(List<Integer> challengeTournaments) {
        this.challengeTournaments = challengeTournaments;
    }

    public String getChallengeDesc() {
        return challengeDesc;
    }

    public void setChallengeDesc(String challengeDesc) {
        this.challengeDesc = challengeDesc;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }
}
