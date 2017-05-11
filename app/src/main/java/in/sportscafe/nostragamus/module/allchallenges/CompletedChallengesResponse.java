package in.sportscafe.nostragamus.module.allchallenges;

import com.fasterxml.jackson.annotation.JsonProperty;

import in.sportscafe.nostragamus.module.allchallenges.dto.ChallengesDataResponse;

/**
 * Created by deepanshi on 5/11/17.
 */

class CompletedChallengesResponse {

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
