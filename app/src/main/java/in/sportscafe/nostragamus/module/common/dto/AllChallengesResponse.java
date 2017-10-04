package in.sportscafe.nostragamus.module.common.dto;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeeva on 17/02/17.
 */
public class AllChallengesResponse {

    /*@SerializedName("data")
    private List<NewChallengesResponse> challenges = new ArrayList<>();

    @SerializedName("data")
    public List<NewChallengesResponse> getAllQuestions() {
        return challenges;
    }

    @SerializedName("data")
    public void setChallenges(List<NewChallengesResponse> challenges) {
        this.challenges = challenges;
    }*/

    @SerializedName("data")
    private ChallengesDataResponse response;

    @SerializedName("data")
    public ChallengesDataResponse getResponse() {
        return response;
    }

    @SerializedName("data")
    public void setResponse(ChallengesDataResponse response) {
        this.response = response;
    }
}