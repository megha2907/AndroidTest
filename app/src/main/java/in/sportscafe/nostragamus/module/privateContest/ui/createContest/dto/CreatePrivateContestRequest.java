package in.sportscafe.nostragamus.module.privateContest.ui.createContest.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sc on 21/3/18.
 */

public class CreatePrivateContestRequest {

    @SerializedName("challenge_id")
    private int challengeId;

    @SerializedName("fee")
    private double fee;

    @SerializedName("config_name")
    private String configName;

    @SerializedName("max_participants")
    private int maxParticipants;

    @SerializedName("min_participants")
    private int minParticipants;

    @SerializedName("step")
    private String step;

    public int getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(int challengeId) {
        this.challengeId = challengeId;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public int getMinParticipants() {
        return minParticipants;
    }

    public void setMinParticipants(int minParticipants) {
        this.minParticipants = minParticipants;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }
}
