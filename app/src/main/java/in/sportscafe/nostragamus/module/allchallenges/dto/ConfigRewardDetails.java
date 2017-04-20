package in.sportscafe.nostragamus.module.allchallenges.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Jeeva on 31/03/17.
 */
@Parcel
public class ConfigRewardDetails {

    @JsonProperty("total")
    private String totalReward;

    @JsonProperty("breakup")
    private List<RewardBreakUp> breakUps = new ArrayList<>();

//    @JsonProperty("winners")
//    private List<WinnersRewards> winnersRewardsList = new ArrayList<>();

    @JsonProperty("total")
    public String getTotalReward() {
        return totalReward;
    }

    @JsonProperty("total")
    public void setTotalReward(String totalReward) {
        this.totalReward = totalReward;
    }

    @JsonProperty("breakup")
    public List<RewardBreakUp> getBreakUps() {
        return breakUps;
    }

    @JsonProperty("breakup")
    public void setBreakUps(List<RewardBreakUp> breakUps) {
        this.breakUps = breakUps;
    }

//    public List<WinnersRewards> getWinnersRewardsList() {
//        return winnersRewardsList;
//    }
//
//    public void setWinnersRewardsList(List<WinnersRewards> winnersRewardsList) {
//        this.winnersRewardsList = winnersRewardsList;
//    }
}