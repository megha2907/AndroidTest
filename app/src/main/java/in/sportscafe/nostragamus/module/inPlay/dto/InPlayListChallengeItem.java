package in.sportscafe.nostragamus.module.inPlay.dto;

/**
 * Created by sandip on 13/09/17.
 */

public class InPlayListChallengeItem {

    private int challengeId;
    private String challengeName;
    private String challengeDesc;
    private String status;
    private int sportsId;
    private int contestCount = 0;

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

    public int getContestCount() {
        return contestCount;
    }

    public void setContestCount(int contestCount) {
        this.contestCount = contestCount;
    }
}
