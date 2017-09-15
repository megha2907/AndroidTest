package in.sportscafe.nostragamus.module.allchallenges;

import com.google.gson.annotations.SerializedName;

import in.sportscafe.nostragamus.module.allchallenges.dto.ChallengesDataResponse;

/**
 * Created by deepanshi on 5/11/17.
 */

class CompletedChallengesResponse {

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
