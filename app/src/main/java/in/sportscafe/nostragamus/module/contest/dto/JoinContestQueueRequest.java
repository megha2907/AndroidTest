package in.sportscafe.nostragamus.module.contest.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sandip on 25/09/17.
 */

public class JoinContestQueueRequest {

    @SerializedName("challenge_id")
    private int challengeId;

    @SerializedName("challenge_name")
    private String challengeName;

    @SerializedName("config_id")
    private int contestId;

    /* NOTE: only required in InPlay - headless state joining */
    @SerializedName("pseudo_room_id")
    private int pseudoRoomId;

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

    public int getContestId() {
        return contestId;
    }

    public void setContestId(int contestId) {
        this.contestId = contestId;
    }

    public int getPseudoRoomId() {
        return pseudoRoomId;
    }

    public void setPseudoRoomId(int pseudoRoomId) {
        this.pseudoRoomId = pseudoRoomId;
    }
}
