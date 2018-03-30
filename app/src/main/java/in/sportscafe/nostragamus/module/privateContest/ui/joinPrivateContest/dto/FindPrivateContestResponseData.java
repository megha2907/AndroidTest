package in.sportscafe.nostragamus.module.privateContest.ui.joinPrivateContest.dto;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by sc on 27/3/18.
 */
@Parcel
public class FindPrivateContestResponseData {

    @SerializedName("challenge_id")
    private int challengeId;

    @SerializedName("challenge_name")
    private String challengeName;

    @SerializedName("challenge_starttime")
    private String challengeStarttime;

    @SerializedName("maxTransferLimit")
    private int maxTransferLimit;

    @SerializedName("contest_data")
    private List<FindPrivateContestResponseContestData> privateContestData = null;

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

    public String getChallengeStarttime() {
        return challengeStarttime;
    }

    public void setChallengeStarttime(String challengeStarttime) {
        this.challengeStarttime = challengeStarttime;
    }

    public int getMaxTransferLimit() {
        return maxTransferLimit;
    }

    public void setMaxTransferLimit(int maxTransferLimit) {
        this.maxTransferLimit = maxTransferLimit;
    }

    public List<FindPrivateContestResponseContestData> getPrivateContestData() {
        return privateContestData;
    }

    public void setPrivateContestData(List<FindPrivateContestResponseContestData> privateContestData) {
        this.privateContestData = privateContestData;
    }
}
