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

    @SerializedName("t_id")
    private String templateId;

    @SerializedName("top_1_win")
    private boolean isPrivateContestTop1Win;

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

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public boolean isPrivateContestTop1Win() {
        return isPrivateContestTop1Win;
    }

    public void setPrivateContestTop1Win(boolean privateContestTop1Win) {
        isPrivateContestTop1Win = privateContestTop1Win;
    }
}
