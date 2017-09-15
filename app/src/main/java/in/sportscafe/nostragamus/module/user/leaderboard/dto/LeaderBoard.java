package in.sportscafe.nostragamus.module.user.leaderboard.dto;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Jeeva on 10/6/16.
 */
@Parcel
public class LeaderBoard {

    @SerializedName("sports_id")
    private Integer sportsId;

    @SerializedName("challenge_id")
    private Integer challengeId;

    @SerializedName("group_id")
    private Integer groupId;

    @SerializedName("sports_name")
    private String sportsName;

    @SerializedName("tournament_id")
    private Integer tournamentId;

    @SerializedName("tournament_name")
    private String tournamentName;

    @SerializedName("group_name")
    private String groupName;

    @SerializedName("tournament_img_url")
    private String tournamentImageUrl;

    @SerializedName("users")
    private List<UserLeaderBoard> userLeaderBoardList;

    /**
     * @return The sportsId
     */
    @SerializedName("sports_id")
    public Integer getSportsId() {
        return sportsId;
    }

    /**
     * @param sportsId The user_id
     */
    @SerializedName("sports_id")
    public void setSportsId(Integer sportsId) {
        this.sportsId = sportsId;
    }

    @SerializedName("tournament_id")
    public Integer getTournamentId() {
        return tournamentId;
    }

    @SerializedName("tournament_id")
    public void setTournamentId(Integer tournamentId) {
        this.tournamentId = tournamentId;
    }

    @SerializedName("tournament_name")
    public String getTournamentName() {
        if (null==tournamentName){
            return groupName;
        }
        return tournamentName;
    }

    @SerializedName("tournament_name")
    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
    }

    @SerializedName("tournament_img_url")
    public String getTournamentImageUrl() {
        return tournamentImageUrl;
    }

    @SerializedName("tournament_img_url")
    public void setTournamentImageUrl(String tournamentImageUrl) {
        this.tournamentImageUrl = tournamentImageUrl;
    }


    @SerializedName("sports_name")
    public String getSportsName() {
        return sportsName;
    }

    @SerializedName("sports_name")
    public void setSportsName(String sportsName) {
        this.sportsName = sportsName;
    }

    @SerializedName("array_agg")
    public List<UserLeaderBoard> getUserLeaderBoardList() {
        return userLeaderBoardList;
    }

    @SerializedName("array_agg")
    public void setUserLeaderBoardList(List<UserLeaderBoard> userLeaderBoardList) {
        this.userLeaderBoardList = userLeaderBoardList;
    }

    @SerializedName("challenge_id")
    public Integer getChallengeId() {
        return challengeId;
    }

    @SerializedName("challenge_id")
    public void setChallengeId(Integer challengeId) {
        this.challengeId = challengeId;
    }

    @SerializedName("group_id")
    public Integer getGroupId() {
        return groupId;
    }

    @SerializedName("group_id")
    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

}