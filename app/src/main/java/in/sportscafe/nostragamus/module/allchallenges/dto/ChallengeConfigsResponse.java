package in.sportscafe.nostragamus.module.allchallenges.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeeva on 31/03/17.
 */
public class ChallengeConfigsResponse {

    @JsonProperty("data")
    private List<ChallengeConfig> configs = new ArrayList<>();

    @JsonProperty("data")
    public List<ChallengeConfig> getConfigs() {
        return configs;
    }

    @JsonProperty("data")
    public void setConfigs(List<ChallengeConfig> configs) {
        this.configs = configs;
    }
}