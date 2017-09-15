package in.sportscafe.nostragamus.module.newChallenges.dto;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;
import in.sportscafe.nostragamus.module.newChallenges.adapter.NewChallengeAdapterItemType;

/**
 * Created by sandip on 29/08/17.
 */

@Parcel
public class NewChallengesResponse {

    private int challengeAdapterItemType = NewChallengeAdapterItemType.CHALLENGE;

    @SerializedName("id")
    private int id;

    @SerializedName("challenge_name")
    private String challengeName;

    @SerializedName("challenge_starttime")
    private String challengeStartTime;

    @SerializedName("challenge_endtime")
    private String challengeEndTime;

    @SerializedName("sports_id")
    private int sportsId;

    @SerializedName("tournaments")
    private List<String> tournaments = null;

    @SerializedName("total_matches")
    private int totalMatches;

    @SerializedName("matches_left")
    private int matchesLeft;

    @SerializedName("matches")
    private List<Match> matches = new ArrayList<>();

    @SerializedName("prizes")
    private double prizes;

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

    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }

    public List<String> getTournaments() {
        return tournaments;
    }

    public void setTournaments(List<String> tournaments) {
        this.tournaments = tournaments;
    }

    public int getTotalMatches() {
        return totalMatches;
    }

    public void setTotalMatches(int totalMatches) {
        this.totalMatches = totalMatches;
    }

    public int getMatchesLeft() {
        return matchesLeft;
    }

    public void setMatchesLeft(int matchesLeft) {
        this.matchesLeft = matchesLeft;
    }

    public double getPrizes() {
        return prizes;
    }

    public void setPrizes(double prizes) {
        this.prizes = prizes;
    }
}
