package in.sportscafe.nostragamus.module.contest.dto;

import org.parceler.Parcel;

/**
 * Created by sandip on 27/09/17.
 */
@Parcel
public class ContestScreenData {

    private int challengeId;
    private String challengeName;
    private boolean isHeadLessFlow = false;
    private boolean isPseudoGameFlow = false;
    private int pseudoRoomId;
    private String challengeStartTime;

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

    public boolean isHeadLessFlow() {
        return isHeadLessFlow;
    }

    public void setHeadLessFlow(boolean headLessFlow) {
        isHeadLessFlow = headLessFlow;
    }

    public int getPseudoRoomId() {
        return pseudoRoomId;
    }

    public void setPseudoRoomId(int pseudoRoomId) {
        this.pseudoRoomId = pseudoRoomId;
    }

    public String getChallengeStartTime() {
        return challengeStartTime;
    }

    public void setChallengeStartTime(String challengeStartTime) {
        this.challengeStartTime = challengeStartTime;
    }

    public boolean isPseudoGameFlow() {
        return isPseudoGameFlow;
    }

    public void setPseudoGameFlow(boolean pseudoGameFlow) {
        isPseudoGameFlow = pseudoGameFlow;
    }
}
