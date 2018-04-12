package in.sportscafe.nostragamus.module.notifications;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.HashMap;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.module.contest.dto.PowerUpInfo;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlay;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayContestDto;
import in.sportscafe.nostragamus.module.navigation.powerupbank.earnmorepowerups.PowerUp;
import in.sportscafe.nostragamus.webservice.UserReferralInfo;

/**
 * Created by sandip on 03/10/17.
 */
@Parcel
public class NostraNotificationData {

    @SerializedName("challenge_id")
    private int challengeId;

    @SerializedName("contest_id")
    private int contestId;

    @SerializedName("room_id")
    private int roomId;

    @SerializedName("match_id")
    private int matchId;

    @SerializedName("challenge_name")
    private String challengeName;

    @SerializedName("config_name")
    private String contestName;

    @SerializedName("match_status")
    private String matchStatus;

    @SerializedName("sport_id")
    private int sportId;

    @SerializedName("refer_friend")
    private UserReferralInfo referFriend;

    @SerializedName("challenge_starttime")
    private String challengeStartTime;

    @SerializedName("webview_url")
    private String webViewUrl;

    @SerializedName("webview_heading")
    private String webViewHeading;

    @SerializedName("slide_id")
    private String slideId;

    @SerializedName("announcement_title")
    private String announcementTitle;

    @SerializedName("announcement_desc")
    private String announcementDesc;

    @SerializedName("announcement_time")
    private String announcementTime;

    /* This is used for InApp Notification , where notification redirect to appropriate game screen */
    @SerializedName("inPlayContestDto")
    private InPlayContestDto inPlayContestDto;

    @SerializedName("powerups")
    private HashMap<String, Integer> powerUps = new HashMap<>();

    public int getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(int challengeId) {
        this.challengeId = challengeId;
    }

    public int getContestId() {
        return contestId;
    }

    public void setContestId(int contestId) {
        this.contestId = contestId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public String getChallengeName() {
        return challengeName;
    }

    public void setChallengeName(String challengeName) {
        this.challengeName = challengeName;
    }

    public String getMatchStatus() {
        return matchStatus;
    }

    public void setMatchStatus(String matchStatus) {
        this.matchStatus = matchStatus;
    }

    public UserReferralInfo getReferFriend() {
        return referFriend;
    }

    public void setReferFriend(UserReferralInfo referFriend) {
        this.referFriend = referFriend;
    }

    public int getSportId() {
        return sportId;
    }

    public void setSportId(int sportId) {
        this.sportId = sportId;
    }

    public String getChallengeStartTime() {
        return challengeStartTime;
    }

    public void setChallengeStartTime(String challengeStartTime) {
        this.challengeStartTime = challengeStartTime;
    }

    public String getContestName() {
        return contestName;
    }

    public void setContestName(String contestName) {
        this.contestName = contestName;
    }

    public String getWebViewUrl() {
        return webViewUrl;
    }

    public void setWebViewUrl(String webViewUrl) {
        this.webViewUrl = webViewUrl;
    }

    public String getWebViewHeading() {
        return webViewHeading;
    }

    public void setWebViewHeading(String webViewHeading) {
        this.webViewHeading = webViewHeading;
    }

    public InPlayContestDto getInPlayContestDto() {
        return inPlayContestDto;
    }

    public void setInPlayContestDto(InPlayContestDto inPlayContestDto) {
        this.inPlayContestDto = inPlayContestDto;
    }

    public String getSlideId() {
        return slideId;
    }

    public void setSlideId(String slideId) {
        this.slideId = slideId;
    }

    public String getAnnouncementTitle() {
        return announcementTitle;
    }

    public void setAnnouncementTitle(String announcementTitle) {
        this.announcementTitle = announcementTitle;
    }

    public String getAnnouncementDesc() {
        return announcementDesc;
    }

    public void setAnnouncementDesc(String announcementDesc) {
        this.announcementDesc = announcementDesc;
    }

    public String getAnnouncementTime() {
        return announcementTime;
    }

    public void setAnnouncementTime(String announcementTime) {
        this.announcementTime = announcementTime;
    }

    public HashMap<String, Integer> getPowerUps() {
        return powerUps;
    }

    public void setPowerUps(HashMap<String, Integer> powerUps) {
        this.powerUps = powerUps;
    }

}
