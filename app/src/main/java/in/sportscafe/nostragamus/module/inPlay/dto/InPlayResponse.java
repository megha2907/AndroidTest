package in.sportscafe.nostragamus.module.inPlay.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

import java.util.List;

import in.sportscafe.nostragamus.module.contest.dto.Contest;
import in.sportscafe.nostragamus.module.inPlay.adapter.InPlayAdapterItemType;

/**
 * Created by deepanshi on 9/6/17.
 */

@Parcel
public class InPlayResponse {

    @JsonProperty("challenge_id")
    private int challengeId;

    @JsonProperty("challenge_name")
    private String challengeName;

    @JsonProperty("challenge_desc")
    private String challengeDesc;

    @JsonProperty("status")
    private String status="ongoing";

    @JsonProperty("sports_id")
    private int sportsId;

    @JsonProperty("is_daily_challenge")
    private boolean isDailyChallenge;

    @JsonProperty("contests")
    private List<InPlayContestDto> contestList;

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

    public String getChallengeDesc() {
        return challengeDesc;
    }

    public void setChallengeDesc(String challengeDesc) {
        this.challengeDesc = challengeDesc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getSportsId() {
        return sportsId;
    }

    public void setSportsId(int sportsId) {
        this.sportsId = sportsId;
    }

    public boolean isDailyChallenge() {
        return isDailyChallenge;
    }

    public void setDailyChallenge(boolean dailyChallenge) {
        isDailyChallenge = dailyChallenge;
    }

    public List<InPlayContestDto> getContestList() {
        return contestList;
    }

    public void setContestList(List<InPlayContestDto> contestList) {
        this.contestList = contestList;
    }
}
