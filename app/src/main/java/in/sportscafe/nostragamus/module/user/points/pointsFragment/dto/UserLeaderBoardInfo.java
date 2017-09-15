package in.sportscafe.nostragamus.module.user.points.pointsFragment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

import java.util.List;

import in.sportscafe.nostragamus.module.user.leaderboard.dto.UserLeaderBoard;

/**
 * Created by deepanshi on 9/15/17.
 */

@Parcel
public class UserLeaderBoardInfo {

    @JsonProperty("room_id")
    private int roomId;

    @JsonProperty("contest_id")
    private int contestId;

    @JsonProperty("contest_name")
    private String contestName;

    @JsonProperty("challenge_id")
    private int challengeId;

    @JsonProperty("challenge_name")
    private String challengeName;

    @JsonProperty("users")
    private List<UserLeaderBoard> userLeaderBoardList;

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getContestId() {
        return contestId;
    }

    public void setContestId(int contestId) {
        this.contestId = contestId;
    }

    public String getContestName() {
        return contestName;
    }

    public void setContestName(String contestName) {
        this.contestName = contestName;
    }

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

    public List<UserLeaderBoard> getUserLeaderBoardList() {
        return userLeaderBoardList;
    }

    public void setUserLeaderBoardList(List<UserLeaderBoard> userLeaderBoardList) {
        this.userLeaderBoardList = userLeaderBoardList;
    }
}
