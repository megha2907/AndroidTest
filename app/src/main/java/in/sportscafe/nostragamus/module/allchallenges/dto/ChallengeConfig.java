package in.sportscafe.nostragamus.module.allchallenges.dto;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by Jeeva on 31/03/17.
 */
@Parcel
public class ChallengeConfig {

    public interface DropDownIds {
        int NONE = 0;
        int MEMBER = 1;
        int REWARD = 2;
    }

    @SerializedName("fee")
    private Integer entryFee;

    @SerializedName("name")
    private String configName;

    @SerializedName("mode")
    private String mode;

    @SerializedName("config_index")
    private int configIndex;

    @SerializedName("players_details")
    private ConfigPlayersDetails playersDetails;

    @SerializedName("prize_money_details")
    private ConfigRewardDetails rewardDetails;

    @SerializedName("fee")
    public Integer getEntryFee() {
        return entryFee;
    }

    @SerializedName("fee")
    public void setEntryFee(Integer entryFee) {
        this.entryFee = entryFee;
    }

    @SerializedName("name")
    public String getConfigName() {
        return configName;
    }

    @SerializedName("name")
    public void setConfigName(String configName) {
        this.configName = configName;
    }

    @SerializedName("mode")
    public String getMode() {
        return mode;
    }

    @SerializedName("mode")
    public void setMode(String mode) {
        this.mode = mode;
    }

    @SerializedName("config_index")
    public int getConfigIndex() {
        return configIndex;
    }

    @SerializedName("config_index")
    public void setConfigIndex(int configIndex) {
        this.configIndex = configIndex;
    }

    @SerializedName("players_details")
    public ConfigPlayersDetails getPlayersDetails() {
        return playersDetails;
    }

    @SerializedName("players_details")
    public void setPlayersDetails(ConfigPlayersDetails playersDetails) {
        this.playersDetails = playersDetails;
    }

    @SerializedName("prize_money_details")
    public ConfigRewardDetails getRewardDetails() {
        return rewardDetails;
    }

    @SerializedName("prize_money_details")
    public void setRewardDetails(ConfigRewardDetails rewardDetails) {
        this.rewardDetails = rewardDetails;
    }


    public boolean isFreeEntry() {
        return null == entryFee || entryFee == 0;
    }


    private int dropDownId;


    public int getDropDownId() {
        return dropDownId;
    }


    public void setDropDownId(int dropDownId) {
        this.dropDownId = dropDownId;
    }
}