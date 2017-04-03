package in.sportscafe.nostragamus.module.allchallenges.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by Jeeva on 31/03/17.
 */
public class PoolRewardDetails {

    @JsonProperty("total_reward")
    private String totalReward;

    @JsonProperty("rewards")
    private LinkedHashMap<String, String> rewards = new LinkedHashMap<>();

    @JsonProperty("total_reward")
    public String getTotalReward() {
        return totalReward;
    }

    @JsonProperty("total_reward")
    public void setTotalReward(String totalReward) {
        this.totalReward = totalReward;
    }

    @JsonProperty("rewards")
    public LinkedHashMap<String, String> getRewards() {
        return rewards;
    }

    @JsonProperty("rewards")
    public void setRewards(LinkedHashMap<String, String> rewards) {
        this.rewards = rewards;
    }
}