package in.sportscafe.nostragamus.module.contest.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sandip on 06/09/17.
 */

public class ContestEntriesResponse {

    @SerializedName("challenge_id")
    private int challengeId;

    @SerializedName("challenge_name")
    private String challengeName;

    @SerializedName("contest_id")
    private int contestId;

    @SerializedName("rooms")
    private List<ContestRoomDto> rooms = null;

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

    public List<ContestRoomDto> getRooms() {
        return rooms;
    }

    public void setRooms(List<ContestRoomDto> rooms) {
        this.rooms = rooms;
    }
}
