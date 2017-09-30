package in.sportscafe.nostragamus.module.contest.dto;

import org.parceler.Parcel;

/**
 * Created by sandip on 30/09/17.
 */
@Parcel
public class ContestEntriesScreenData {

    private int contestId;
    private int challengeId;

    public int getContestId() {
        return contestId;
    }

    public void setContestId(int contestId) {
        this.contestId = contestId;
    }

    public int getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(int challengeId) {
        this.challengeId = challengeId;
    }
}
