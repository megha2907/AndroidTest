package in.sportscafe.nostragamus.module.allchallenges.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeeva on 17/02/17.
 */
public class AllChallengesResponse {

    /*@JsonProperty("data")
    private List<NewChallengesResponse> challenges = new ArrayList<>();

    @JsonProperty("data")
    public List<NewChallengesResponse> getChallenges() {
        return challenges;
    }

    @JsonProperty("data")
    public void setChallenges(List<NewChallengesResponse> challenges) {
        this.challenges = challenges;
    }*/

    @JsonProperty("data")
    private ChallengesDataResponse response;

    @JsonProperty("data")
    public ChallengesDataResponse getResponse() {
        return response;
    }

    @JsonProperty("data")
    public void setResponse(ChallengesDataResponse response) {
        this.response = response;
    }
}