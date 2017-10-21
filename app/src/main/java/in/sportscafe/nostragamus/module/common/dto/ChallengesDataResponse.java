package in.sportscafe.nostragamus.module.common.dto;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sandip on 01/05/17.
 */

public class ChallengesDataResponse {

    @SerializedName("In Play")
    private List<Challenge> inPlayChallenges = new ArrayList<>();

    @SerializedName("New")
    private List<Challenge> newChallenges = new ArrayList<>();

    @SerializedName("Completed")
    private List<Challenge> completedChallenges = new ArrayList<>();

    public List<Challenge> getInPlayChallenges() {
        return inPlayChallenges;
    }

    public void setInPlayChallenges(List<Challenge> inPlayChallenges) {
        this.inPlayChallenges = inPlayChallenges;
    }

    public List<Challenge> getNewChallenges() {
        return newChallenges;
    }

    public void setNewChallenges(List<Challenge> newChallenges) {
        this.newChallenges = newChallenges;
    }

    public List<Challenge> getCompletedChallenges() {
        return completedChallenges;
    }

    public void setCompletedChallenges(List<Challenge> completedChallenges) {
        this.completedChallenges = completedChallenges;
    }
}