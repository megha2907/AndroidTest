package in.sportscafe.nostragamus.module.user.leaderboard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Jeeva on 10/6/16.
 */
@Parcel
public class LeaderBoard {

    @JsonProperty("sports_id")
    private Integer sportsId;

    @JsonProperty("challenge_id")
    private Integer challengeId;

    @JsonProperty("group_id")
    private Long groupId;

    @JsonProperty("sports_name")
    private String sportsName;

    @JsonProperty("tournament_id")
    private Integer tournamentId;

    @JsonProperty("tournament_name")
    private String tournamentName;

    @JsonProperty("tournament_img_url")
    private String tournamentImageUrl;

    @JsonProperty("users")
    private List<UserLeaderBoard> userLeaderBoardList;


    /**
     * @return The sportsId
     */
    @JsonProperty("sports_id")
    public Integer getSportsId() {
        return sportsId;
    }

    /**
     * @param sportsId The user_id
     */
    @JsonProperty("sports_id")
    public void setSportsId(Integer sportsId) {
        this.sportsId = sportsId;
    }

    @JsonProperty("tournament_id")
    public Integer getTournamentId() {
        return tournamentId;
    }

    @JsonProperty("tournament_id")
    public void setTournamentId(Integer tournamentId) {
        this.tournamentId = tournamentId;
    }

    @JsonProperty("tournament_name")
    public String getTournamentName() {
        return tournamentName;
    }

    @JsonProperty("tournament_name")
    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
    }

    @JsonProperty("tournament_img_url")
    public String getTournamentImageUrl() {
        return tournamentImageUrl;
    }

    @JsonProperty("tournament_img_url")
    public void setTournamentImageUrl(String tournamentImageUrl) {
        this.tournamentImageUrl = tournamentImageUrl;
    }


    @JsonProperty("sports_name")
    public String getSportsName() {
        return sportsName;
    }

    @JsonProperty("sports_name")
    public void setSportsName(String sportsName) {
        this.sportsName = sportsName;
    }

    @JsonProperty("array_agg")
    public List<UserLeaderBoard> getUserLeaderBoardList() {
        return userLeaderBoardList;
    }

    @JsonProperty("array_agg")
    public void setUserLeaderBoardList(List<UserLeaderBoard> userLeaderBoardList) {
        this.userLeaderBoardList = userLeaderBoardList;
    }

    @JsonProperty("challenge_id")
    public Integer getChallengeId() {
        return challengeId;
    }

    @JsonProperty("challenge_id")
    public void setChallengeId(Integer challengeId) {
        this.challengeId = challengeId;
    }

    @JsonProperty("group_id")
    public Long getGroupId() {
        return groupId;
    }

    @JsonProperty("group_id")
    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

}