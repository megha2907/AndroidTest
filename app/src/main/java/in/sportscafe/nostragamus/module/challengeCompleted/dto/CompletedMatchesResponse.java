package in.sportscafe.nostragamus.module.challengeCompleted.dto;

import com.google.gson.annotations.SerializedName;

import in.sportscafe.nostragamus.module.inPlay.dto.InPlayMatchesResponseData;

/**
 * Created by deepanshi on 9/27/17.
 */

public class CompletedMatchesResponse {

    @SerializedName("data")
    private CompletedMatchesResponseData data;

    public CompletedMatchesResponseData getData() {
        return data;
    }

    public void setData(CompletedMatchesResponseData data) {
        this.data = data;
    }
}
