package in.sportscafe.nostragamus.module.newChallenges.dto;

import org.parceler.Parcel;

/**
 * Created by sandip on 03/10/17.
 */
@Parcel
public class NewChallengeMatchesScreenData {

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
