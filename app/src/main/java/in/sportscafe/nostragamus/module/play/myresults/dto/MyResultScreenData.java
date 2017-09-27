package in.sportscafe.nostragamus.module.play.myresults.dto;

import org.parceler.Parcel;

/**
 * Created by sandip on 27/09/17.
 */
@Parcel
public class MyResultScreenData {

    private int matchId;
    private int roomId;
    private int challengeId;

    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(int challengeId) {
        this.challengeId = challengeId;
    }
}
