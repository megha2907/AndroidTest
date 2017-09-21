package in.sportscafe.nostragamus.module.prediction.powerupBank.dto;

import java.util.HashMap;

import in.sportscafe.nostragamus.Constants;

/**
 * Created by sandip on 21/07/17.
 */

public class UserDemandPowerUpDto {

    private int doubler;
    private int noNegative;
    private int audiencePoll;

    public UserDemandPowerUpDto() {
        clearData();
    }

    public int getDoubler() {
        return doubler;
    }

    public void setDoubler(int doubler) {
        this.doubler = doubler;
    }

    public int getNoNegative() {
        return noNegative;
    }

    public void setNoNegative(int noNegative) {
        this.noNegative = noNegative;
    }

    public int getAudiencePoll() {
        return audiencePoll;
    }

    public void setAudiencePoll(int audiencePoll) {
        this.audiencePoll = audiencePoll;
    }

    public void clearData() {
        doubler = 0;
        noNegative = 0;
        audiencePoll = 0;
    }

    public HashMap<String, Integer> getValueMap() {
        HashMap<String, Integer> map = new HashMap<>();

        map.put(Constants.Powerups.XX, getDoubler());
        map.put(Constants.Powerups.NO_NEGATIVE, getNoNegative());
        map.put(Constants.Powerups.AUDIENCE_POLL, getAudiencePoll());

        return map;
    }
}
