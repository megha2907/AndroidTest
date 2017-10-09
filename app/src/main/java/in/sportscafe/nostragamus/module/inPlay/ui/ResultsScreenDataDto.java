package in.sportscafe.nostragamus.module.inPlay.ui;

import org.parceler.Parcel;

import in.sportscafe.nostragamus.module.prediction.playScreen.dto.PowerUp;

/**
 * Created by deepanshi on 9/28/17.
 */
@Parcel
public  class ResultsScreenDataDto {

    private int challengeId;
    private int roomId;
    private int matchId;
    private String matchPartyTitle1;
    private String matchPartyTitle2;
    private String subTitle;
    private String challengeName;
    private String challengeStartTime;
    private String matchStatus;
    private boolean isPlayingPseudoGame = false;
    private boolean isHeadLess = false;

    public int getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(int challengeId) {
        this.challengeId = challengeId;
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

    public String getMatchPartyTitle1() {
        return matchPartyTitle1;
    }

    public void setMatchPartyTitle1(String matchPartyTitle1) {
        this.matchPartyTitle1 = matchPartyTitle1;
    }

    public String getMatchPartyTitle2() {
        return matchPartyTitle2;
    }

    public void setMatchPartyTitle2(String matchPartyTitle2) {
        this.matchPartyTitle2 = matchPartyTitle2;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
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

    public boolean isPlayingPseudoGame() {
        return isPlayingPseudoGame;
    }

    public void setPlayingPseudoGame(boolean playingPseudoGame) {
        isPlayingPseudoGame = playingPseudoGame;
    }

    public boolean isHeadLess() {
        return isHeadLess;
    }

    public void setHeadLess(boolean headLess) {
        isHeadLess = headLess;
    }

    public String getChallengeStartTime() {
        return challengeStartTime;
    }

    public void setChallengeStartTime(String challengeStartTime) {
        this.challengeStartTime = challengeStartTime;
    }
}
