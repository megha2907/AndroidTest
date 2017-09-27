package in.sportscafe.nostragamus.module.contest.dto;

import org.parceler.Parcel;

/**
 * Created by sandip on 27/09/17.
 */
@Parcel
public class ContestScreenData {

    private int challengeId;
    private String challengeName;

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
}
