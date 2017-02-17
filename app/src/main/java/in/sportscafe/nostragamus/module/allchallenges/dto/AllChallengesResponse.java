package in.sportscafe.nostragamus.module.allchallenges.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeeva on 17/02/17.
 */
public class AllChallengesResponse {

    @JsonProperty("data")
    private List<Challenge> challenges = new ArrayList<>();

    @JsonProperty("data")
    public List<Challenge> getChallenges() {
        return challenges;
    }

    @JsonProperty("data")
    public void setChallenges(List<Challenge> challenges) {
        this.challenges = challenges;
    }
}