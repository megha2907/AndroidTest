package in.sportscafe.nostragamus.module.contest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by sandip on 01/09/17.
 */

public class ContestResponse {

    @JsonProperty("data")
    private ContestResponseData data;

    public ContestResponseData getData() {
        return data;
    }

    public void setData(ContestResponseData data) {
        this.data = data;
    }
}
