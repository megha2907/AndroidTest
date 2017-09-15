package in.sportscafe.nostragamus.module.popups.bankpopups;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

/**
 * Created by Jeeva on 02/03/17.
 */
public class BankTransferRequest {

    @SerializedName("challenge_id")
    private int challengeId;

    @SerializedName("add_powerups")
    private HashMap<String, Integer> powerUps = new HashMap<>();

    @SerializedName("challenge_id")
    public int getChallengeId() {
        return challengeId;
    }

    @SerializedName("challenge_id")
    public void setChallengeId(int challengeId) {
        this.challengeId = challengeId;
    }

    @SerializedName("add_powerups")
    public HashMap<String, Integer> getPowerUps() {
        return powerUps;
    }

    @SerializedName("add_powerups")
    public void setPowerUps(HashMap<String, Integer> powerUps) {
        this.powerUps = powerUps;
    }
}