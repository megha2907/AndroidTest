package in.sportscafe.nostragamus.module.prediction.powerupBank.dto;

import org.parceler.Parcel;

/**
 * Created by sc on 2/1/18.
 */
@Parcel
public class PowerupBankTransferScreenData {

    private int challengeId;
    private int roomId;
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

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }
}
