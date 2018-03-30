package in.sportscafe.nostragamus.module.privateContest.ui.createContest.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sc on 22/3/18.
 */

public class CreatePrivateContestInfo {

    @SerializedName("fee")
    private int fee;

    @SerializedName("mode")
    private String mode;

    @SerializedName("step")
    private String step;

    @SerializedName("spawn")
    private boolean spawn;

    @SerializedName("flavor")
    private String flavor;

    @SerializedName("margin")
    private double margin;

    @SerializedName("payout")
    private CreatePrivateContestPayout payout;

    @SerializedName("priority")
    private int priority;

    @SerializedName("config_name")
    private String configName;

    @SerializedName("challenge_id")
    private int challengeId;

    @SerializedName("private_code")
    private String privateCode;

    @SerializedName("user_segment")
    private String userSegment;

    @SerializedName("category_name")
    private String categoryName;

    @SerializedName("spawnBeginner")
    private boolean spawnBeginner;

    @SerializedName("max_participants")
    private int maxParticipants;

    @SerializedName("min_participants")
    private int minParticipants;

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public boolean isSpawn() {
        return spawn;
    }

    public void setSpawn(boolean spawn) {
        this.spawn = spawn;
    }

    public String getFlavor() {
        return flavor;
    }

    public void setFlavor(String flavor) {
        this.flavor = flavor;
    }

    public double getMargin() {
        return margin;
    }

    public void setMargin(double margin) {
        this.margin = margin;
    }

    public CreatePrivateContestPayout getPayout() {
        return payout;
    }

    public void setPayout(CreatePrivateContestPayout payout) {
        this.payout = payout;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public int getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(int challengeId) {
        this.challengeId = challengeId;
    }

    public String getPrivateCode() {
        return privateCode;
    }

    public void setPrivateCode(String privateCode) {
        this.privateCode = privateCode;
    }

    public String getUserSegment() {
        return userSegment;
    }

    public void setUserSegment(String userSegment) {
        this.userSegment = userSegment;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public boolean isSpawnBeginner() {
        return spawnBeginner;
    }

    public void setSpawnBeginner(boolean spawnBeginner) {
        this.spawnBeginner = spawnBeginner;
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
}
