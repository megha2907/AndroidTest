package in.sportscafe.nostragamus.module.challengeCompleted.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by deepanshi on 9/27/17.
 */

public class CompletedListChallengeItem {

    private int challengeId;
    private String challengeName;
    private String challengeDesc;
    private String status;
    private int[] sportIdArray;
    private int contestCount = 0;
    private String challengeStartTime;
    private String challengeEndTime;

    private List<String> challengeTournaments = new ArrayList<>();

    public int getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(int challengeId) {
        this.challengeId = challengeId;
    }

    public String getChallengeName() {
        return challengeName;
    }

    public void setChallengeName(String challengeName) {
        this.challengeName = challengeName;
    }

    public String getChallengeDesc() {
        return challengeDesc;
    }

    public void setChallengeDesc(String challengeDesc) {
        this.challengeDesc = challengeDesc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int[] getSportIdArray() {
        return sportIdArray;
    }

    public void setSportIdArray(int[] sportIdArray) {
        this.sportIdArray = sportIdArray;
    }

    public int getContestCount() {
        return contestCount;
    }

    public void setContestCount(int contestCount) {
        this.contestCount = contestCount;
    }

    public List<String> getChallengeTournaments() {
        return challengeTournaments;
    }

    public void setChallengeTournaments(List<String> challengeTournaments) {
        this.challengeTournaments = challengeTournaments;
    }

    public String getChallengeStartTime() {
        return challengeStartTime;
    }

    public void setChallengeStartTime(String challengeStartTime) {
        this.challengeStartTime = challengeStartTime;
    }

    public String getChallengeEndTime() {
        return challengeEndTime;
    }

    public void setChallengeEndTime(String challengeEndTime) {
        this.challengeEndTime = challengeEndTime;
    }

}
