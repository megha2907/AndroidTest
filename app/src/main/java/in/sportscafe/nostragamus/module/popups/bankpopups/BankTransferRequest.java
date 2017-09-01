package in.sportscafe.nostragamus.module.popups.bankpopups;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;

/**
 * Created by Jeeva on 02/03/17.
 */
public class BankTransferRequest {

    @JsonProperty("challenge_id")
    private int challengeId;

    @JsonProperty("add_powerups")
    private HashMap<String, Integer> powerUps = new HashMap<>();

    @JsonProperty("challenge_id")
    public int getChallengeId() {
        return challengeId;
    }

    @JsonProperty("challenge_id")
    public void setChallengeId(int challengeId) {
        this.challengeId = challengeId;
    }

    @JsonProperty("add_powerups")
    public HashMap<String, Integer> getPowerUps() {
        return powerUps;
    }

    @JsonProperty("add_powerups")
    public void setPowerUps(HashMap<String, Integer> powerUps) {
        this.powerUps = powerUps;
    }
}