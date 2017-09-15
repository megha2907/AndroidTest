package in.sportscafe.nostragamus.module.allchallenges.dto;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeeva on 31/03/17.
 */
public class ChallengeConfigsResponse {

    @SerializedName("data")
    private List<ChallengeConfig> configs = new ArrayList<>();

    @SerializedName("data")
    public List<ChallengeConfig> getConfigs() {
        return configs;
    }

    @SerializedName("data")
    public void setConfigs(List<ChallengeConfig> configs) {
        this.configs = configs;
    }
}