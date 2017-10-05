package in.sportscafe.nostragamus.module.newChallenges.dto;

import org.parceler.Parcel;

/**
 * Created by sandip on 03/10/17.
 */
@Parcel
public class NewChallengeMatchesScreenData {

    private int challengeId;
    private String challengeName;
    private String startTime;
    private int matchesLeft;
    private int totalMatches;

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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getMatchesLeft() {
        return matchesLeft;
    }

    public void setMatchesLeft(int matchesLeft) {
        this.matchesLeft = matchesLeft;
    }

    public int getTotalMatches() {
        return totalMatches;
    }

    public void setTotalMatches(int totalMatches) {
        this.totalMatches = totalMatches;
    }
}
