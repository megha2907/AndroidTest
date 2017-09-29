package in.sportscafe.nostragamus.module.navigation.referfriends.referralcredits;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import in.sportscafe.nostragamus.module.common.dto.RewardBreakUp;
import in.sportscafe.nostragamus.module.common.dto.WinnersRewards;

/**
 * Created by Jeeva on 31/03/17.
 */
@Parcel
public class ConfigRewardDetails {

    @SerializedName("total")
    private String totalReward;

    @SerializedName("user_amount")
    private String amountWonByUser;

    @SerializedName("breakup")
    private List<RewardBreakUp> breakUps = new ArrayList<>();

    @SerializedName("winners")
    private List<WinnersRewards> winnersRewardsList = new ArrayList<>();

    @SerializedName("total")
    public String getTotalReward() {
        return totalReward;
    }

    @SerializedName("total")
    public void setTotalReward(String totalReward) {
        this.totalReward = totalReward;
    }

    @SerializedName("breakup")
    public List<RewardBreakUp> getBreakUps() {
        return breakUps;
    }

    @SerializedName("breakup")
    public void setBreakUps(List<RewardBreakUp> breakUps) {
        this.breakUps = breakUps;
    }

    @SerializedName("winners")
    public List<WinnersRewards> getWinnersRewardsList() {
        return winnersRewardsList;
    }

    @SerializedName("winners")
    public void setWinnersRewardsList(List<WinnersRewards> winnersRewardsList) {
        this.winnersRewardsList = winnersRewardsList;
    }

    public String getAmountWonByUser() {
        return amountWonByUser;
    }

    public void setAmountWonByUser(String amountWonByUser) {
        this.amountWonByUser = amountWonByUser;
    }
}