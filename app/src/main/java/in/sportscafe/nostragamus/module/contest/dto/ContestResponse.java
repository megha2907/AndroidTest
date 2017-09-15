package in.sportscafe.nostragamus.module.contest.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sandip on 01/09/17.
 */

public class ContestResponse {

    @SerializedName("data")
    private ContestResponseData data;

    public ContestResponseData getData() {
        return data;
    }

    public void setData(ContestResponseData data) {
        this.data = data;
    }
}
