package in.sportscafe.nostragamus.module.contest.dto;

import android.support.v7.app.AppCompatActivity;

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

}
