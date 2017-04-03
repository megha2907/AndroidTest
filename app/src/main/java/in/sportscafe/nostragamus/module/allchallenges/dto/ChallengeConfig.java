package in.sportscafe.nostragamus.module.allchallenges.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

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

    @JsonProperty("fee")
    private Integer entryFee;

    @JsonProperty("config_label")
    private String configName;

    @JsonProperty("mode")
    private String mode;

    @JsonProperty("config_index")
    private int configIndex;

    @JsonProperty("players_details")
    private ConfigPlayersDetails playersDetails;

    @JsonProperty("prize_money_details")
    private ConfigRewardDetails rewardDetails;

    @JsonProperty("fee")
    public Integer getEntryFee() {
        return entryFee;
    }

    @JsonProperty("fee")
    public void setEntryFee(Integer entryFee) {
        this.entryFee = entryFee;
    }

    @JsonProperty("config_label")
    public String getConfigName() {
        return configName;
    }

    @JsonProperty("config_label")
    public void setConfigName(String configName) {
        this.configName = configName;
    }

    @JsonProperty("mode")
    public String getMode() {
        return mode;
    }

    @JsonProperty("mode")
    public void setMode(String mode) {
        this.mode = mode;
    }

    @JsonProperty("config_index")
    public int getConfigIndex() {
        return configIndex;
    }

    @JsonProperty("config_index")
    public void setConfigIndex(int configIndex) {
        this.configIndex = configIndex;
    }

    @JsonProperty("players_details")
    public ConfigPlayersDetails getPlayersDetails() {
        return playersDetails;
    }

    @JsonProperty("players_details")
    public void setPlayersDetails(ConfigPlayersDetails playersDetails) {
        this.playersDetails = playersDetails;
    }

    @JsonProperty("prize_money_details")
    public ConfigRewardDetails getRewardDetails() {
        return rewardDetails;
    }

    @JsonProperty("prize_money_details")
    public void setRewardDetails(ConfigRewardDetails rewardDetails) {
        this.rewardDetails = rewardDetails;
    }

    @JsonIgnore
    public boolean isFreeEntry() {
        return null == entryFee || entryFee == 0;
    }

    @JsonIgnore
    private int dropDownId;

    @JsonIgnore
    public int getDropDownId() {
        return dropDownId;
    }

    @JsonIgnore
    public void setDropDownId(int dropDownId) {
        this.dropDownId = dropDownId;
    }
}