package in.sportscafe.nostragamus.module.inPlay.ui.headless.dto;

import org.parceler.Parcel;

import in.sportscafe.nostragamus.module.inPlay.dto.InPlayContestDto;
import in.sportscafe.nostragamus.module.prediction.playScreen.dto.PowerUp;

/**
 * Created by sandip on 27/09/17.
 */
@Parcel
public class HeadLessMatchScreenData {

    private int roomId;
    private int challengeId;
    private int matchId;
    private String challengeName;
    private String contestName;
    private String startTime;
    private PowerUp powerUp;
    private boolean isPlayingPseudoGame = false;
    private int matchesLeft;
    private int totalMatches;
    private InPlayContestDto inPlayContestDto;

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

    public String getContestName() {
        return contestName;
    }

    public void setContestName(String contestName) {
        this.contestName = contestName;
    }

    public PowerUp getPowerUp() {
        return powerUp;
    }

    public void setPowerUp(PowerUp powerUp) {
        this.powerUp = powerUp;
    }

    public boolean isPlayingPseudoGame() {
        return isPlayingPseudoGame;
    }

    public void setPlayingPseudoGame(boolean playingPseudoGame) {
        isPlayingPseudoGame = playingPseudoGame;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getMatchesLeft() {
        return matchesLeft;
    }

    public void setMatchesLeft(int matchesLeft) {
        this.matchesLeft = matchesLeft;
    }

    public int getTotalMatches() {
        return totalMatches;
    }

    public void setTotalMatches(int totalMatches) {
        this.totalMatches = totalMatches;
    }

    public InPlayContestDto getInPlayContestDto() {
        return inPlayContestDto;
    }

    public void setInPlayContestDto(InPlayContestDto inPlayContestDto) {
        this.inPlayContestDto = inPlayContestDto;
    }
}