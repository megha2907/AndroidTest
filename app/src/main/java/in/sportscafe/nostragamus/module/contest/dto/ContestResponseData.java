package in.sportscafe.nostragamus.module.contest.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sandip on 01/09/17.
 */

public class ContestResponseData {

    @SerializedName("challenge_id")
    private int challengeId;

    @SerializedName("challenge_name")
    private String challengeName;

    @SerializedName("challenge_starttime")
    private String challengeStartTime;

    @SerializedName("maxTransferLimit")
    private int maxTransferLimit = -1;

    @SerializedName("contest_data")
    private List<Contest> contests;

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

    public List<Contest> getContests() {
        return contests;
    }

    public void setContests(List<Contest> contests) {
        this.contests = contests;
    }

    public int getMaxTransferLimit() {
        return maxTransferLimit;
    }

    public void setMaxTransferLimit(int maxTransferLimit) {
        this.maxTransferLimit = maxTransferLimit;
    }
}
