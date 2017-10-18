package in.sportscafe.nostragamus.module.common.dto;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.module.user.myprofile.dto.Tournament;

/**
 * Created by Jeeva on 17/02/17.
 */
@Parcel
public class Challenge {

    @SerializedName("challenge_id")
    private int challengeId;

    @SerializedName("challenge_name")
    private String name;

    @SerializedName("challenge_desc")
    private String description;

    @SerializedName("challenge_img_url")
    private String image;

    @SerializedName("challenge_tournaments")
    private List<Tournament> tournaments = new ArrayList<>();

    @SerializedName("challenge_starttime")
    private String startTime;

    @SerializedName("challenge_endtime")
    private String endTime;

    @SerializedName("count_matches_left")
    private String countMatchesLeft;

    @SerializedName("matches")
    private MatchesCategorized matchesCategorized = new MatchesCategorized();

    @SerializedName("rank")
    private Integer userRank;

    @SerializedName("rank_change")
    private Integer userRankChange;

    @SerializedName("user_challenge_info")
    private ChallengeUserInfo challengeUserInfo;

    @SerializedName("challenge_info")
    private ChallengeInfo challengeInfo;

    @SerializedName("prize_money_topline")
    private String prizeMoneyTopline;

    @SerializedName("challenge_id")
    public int getChallengeId() {
        return challengeId;
    }

    @SerializedName("challenge_id")
    public void setChallengeId(int challengeId) {
        this.challengeId = challengeId;
    }

    @SerializedName("challenge_name")
    public String getName() {
        return name;
    }

    @SerializedName("challenge_name")
    public void setName(String name) {
        this.name = name;
    }

    @SerializedName("challenge_desc")
    public String getDescription() {
        return description;
    }

    @SerializedName("challenge_desc")
    public void setDescription(String description) {
        this.description = description;
    }

    @SerializedName("challenge_tournaments")
    public List<Tournament> getTournaments() {
        return tournaments;
    }

    @SerializedName("challenge_tournaments")
    public void setTournaments(List<Tournament> tournaments) {
        this.tournaments = tournaments;
    }

    @SerializedName("challenge_starttime")
    public String getStartTime() {
        return startTime;
    }

    @SerializedName("challenge_starttime")
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    @SerializedName("challenge_endtime")
    public String getEndTime() {
        return endTime;
    }

    @SerializedName("challenge_endtime")
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @SerializedName("matches")
    public MatchesCategorized getMatchesCategorized() {
        return matchesCategorized;
    }

    @SerializedName("matches")
    public void setMatchesCategorized(MatchesCategorized matchesCategorized) {
        this.matchesCategorized = matchesCategorized;
    }

    @SerializedName("challenge_image_url")
    public String getImage() {
        return image;
    }

    @SerializedName("challenge_image_url")
    public void setImage(String image) {
        this.image = image;
    }

    @SerializedName("challenge_user_rank")
    public Integer getUserRank() {
        return userRank;
    }

    @SerializedName("challenge_user_rank")
    public void setUserRank(Integer userRank) {
        this.userRank = userRank;
    }

    @SerializedName("rank_change")
    public Integer getUserRankChange() {
        return userRankChange;
    }

    @SerializedName("rank_change")
    public void setUserRankChange(Integer userRankChange) {
        this.userRankChange = userRankChange;
    }


    @SerializedName("user_challenge_info")
    public ChallengeUserInfo getChallengeUserInfo() {
        if(null == challengeUserInfo) {
            challengeUserInfo = new ChallengeUserInfo();
        }
        return challengeUserInfo;
    }

    @SerializedName("user_challenge_info")
    public void setChallengeUserInfo(ChallengeUserInfo challengeUserInfo) {
        this.challengeUserInfo = challengeUserInfo;
    }

    @SerializedName("challenge_info")
    public ChallengeInfo getChallengeInfo() {
        return challengeInfo;
    }

    @SerializedName("challenge_info")
    public void setChallengeInfo(ChallengeInfo challengeInfo) {
        this.challengeInfo = challengeInfo;
    }

    @SerializedName("count_matches_left")
    public String getCountMatchesLeft() {
        return countMatchesLeft;
    }

    @SerializedName("count_matches_left")
    public void setCountMatchesLeft(String countMatchesLeft) {
        this.countMatchesLeft = countMatchesLeft;
    }

    @SerializedName("prize_money_topline")
    public String getPrizeMoneyTopline() {
        return prizeMoneyTopline;
    }

    @SerializedName("prize_money_topline")
    public void setPrizeMoneyTopline(String prizeMoneyTopline) {
        this.prizeMoneyTopline = prizeMoneyTopline;
    }

}