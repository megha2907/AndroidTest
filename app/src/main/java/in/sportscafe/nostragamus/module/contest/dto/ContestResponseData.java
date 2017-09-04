package in.sportscafe.nostragamus.module.contest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by sandip on 01/09/17.
 */

public class ContestResponseData {

    @JsonProperty("challenge_id")
    private int challengeId;

    @JsonProperty("challenge_name")
    private String challengeName;

    @JsonProperty("challenge_starttime")
    private String challengeStartTime;

    @JsonProperty("contest_data")
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

}
