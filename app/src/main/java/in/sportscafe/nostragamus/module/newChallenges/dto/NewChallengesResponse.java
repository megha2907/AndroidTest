package in.sportscafe.nostragamus.module.newChallenges.dto;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;
import in.sportscafe.nostragamus.module.newChallenges.adapter.NewChallengeAdapterItemType;

/**
 * Created by sandip on 29/08/17.
 */

@Parcel
public class NewChallengesResponse {

    private int challengeAdapterItemType = NewChallengeAdapterItemType.CHALLENGE;

    @SerializedName("challenge_id")
    private int challengeId;

    @SerializedName("challenge_name")
    private String challengeName;

    @SerializedName("start_time")
    private String challengeStartTime;

    @SerializedName("end_time")
    private String challengeEndTime;

    @SerializedName("sports_id_arr")
    private int[] sportsIdArray;

    @SerializedName("challenge_tournaments")
    private List<String> tournaments = null;

    @SerializedName("tot_matches")
    private int totalMatches;

    @SerializedName("matches_left")
    private int matchesLeft;

    @SerializedName("prize_money")
    private int prizes;

    @SerializedName("is_daily")
    private boolean isDaily;

    public int getChallengeAdapterItemType() {
        return challengeAdapterItemType;
    }

    public void setChallengeAdapterItemType(int challengeAdapterItemType) {
        this.challengeAdapterItemType = challengeAdapterItemType;
    }

    public int getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(int challengeId) {
        this.challengeId = challengeId;
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

    public int[] getSportsIdArray() {
        return sportsIdArray;
    }

    public void setSportsIdArray(int[] sportsIdArray) {
        this.sportsIdArray = sportsIdArray;
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

    public int getPrizes() {
        return prizes;
    }

    public void setPrizes(int prizes) {
        this.prizes = prizes;
    }

    public boolean isDaily() {
        return isDaily;
    }

    public void setDaily(boolean daily) {
        isDaily = daily;
    }
}
