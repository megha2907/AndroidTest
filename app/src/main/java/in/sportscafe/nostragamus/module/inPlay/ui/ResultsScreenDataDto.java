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

}
