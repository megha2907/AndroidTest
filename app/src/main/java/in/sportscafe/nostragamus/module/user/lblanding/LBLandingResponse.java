package in.sportscafe.nostragamus.module.user.lblanding;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeeva on 11/7/16.
 */
public class LBLandingResponse {

    @SerializedName("data")
    private List<LBLandingSummary> summary= new ArrayList<>();

    @SerializedName("data")
    public List<LBLandingSummary> getSummary() {
        return summary;
    }

    @SerializedName("data")
    public void setSummary(List<LBLandingSummary> summary) {
        this.summary = summary;
    }

}