package in.sportscafe.nostragamus.module.inPlay.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sandip on 09/09/17.
 */

public class InPlayMatchesResponse {

    @SerializedName("data")
    private InPlayMatchesResponseData data;

    public InPlayMatchesResponseData getData() {
        return data;
    }

    public void setData(InPlayMatchesResponseData data) {
        this.data = data;
    }
}
