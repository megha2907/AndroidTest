package in.sportscafe.nostragamus.module.contest.dto;

import org.parceler.Parcel;

/**
 * Created by sandip on 25/09/17.
 */
@Parcel
public class JoinContestData {

    private int challengeId;
    private int contestId;
    private int joiContestDialogLaunchMode;
    private double entryFee;
    private String challengeName;
    private boolean shouldSendPseudoRoomId = false;
    private int pseudoRoomId;
    private String contestType;
    private String contestName;
    private int privateContestEntries;

    private boolean shouldScrollContestsInPlay = true;

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

    public String getChallengeName() {
        return challengeName;
    }

    public void setChallengeName(String challengeName) {
        this.challengeName = challengeName;
    }

    public int getJoiContestDialogLaunchMode() {
        return joiContestDialogLaunchMode;
    }

    public void setJoiContestDialogLaunchMode(int joiContestDialogLaunchMode) {
        this.joiContestDialogLaunchMode = joiContestDialogLaunchMode;
    }

    public double getEntryFee() {
        return entryFee;
    }

    public void setEntryFee(double entryFee) {
        this.entryFee = entryFee;
    }

    public boolean isShouldSendPseudoRoomId() {
        return shouldSendPseudoRoomId;
    }

    public void setShouldSendPseudoRoomId(boolean shouldSendPseudoRoomId) {
        this.shouldSendPseudoRoomId = shouldSendPseudoRoomId;
    }

    public int getPseudoRoomId() {
        return pseudoRoomId;
    }

    public void setPseudoRoomId(int pseudoRoomId) {
        this.pseudoRoomId = pseudoRoomId;
    }

    public boolean isShouldScrollContestsInPlay() {
        return shouldScrollContestsInPlay;
    }

    public void setShouldScrollContestsInPlay(boolean shouldScrollContestsInPlay) {
        this.shouldScrollContestsInPlay = shouldScrollContestsInPlay;
    }

    public String getContestType() {
        return contestType;
    }

    public void setContestType(String contestType) {
        this.contestType = contestType;
    }

    public String getContestName() {
        return contestName;
    }

    public void setContestName(String contestName) {
        this.contestName = contestName;
    }

    public int getPrivateContestEntries() {
        return privateContestEntries;
    }

    public void setPrivateContestEntries(int privateContestEntries) {
        this.privateContestEntries = privateContestEntries;
    }
}
