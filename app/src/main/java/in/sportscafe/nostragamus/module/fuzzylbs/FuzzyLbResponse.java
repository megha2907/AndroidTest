package in.sportscafe.nostragamus.module.fuzzylbs;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.module.user.lblanding.LbLanding;

/**
 * Created by Jeeva on 10/01/17.
 */

public class FuzzyLbResponse {

    @SerializedName("data")
    private List<LbLanding> lbs = new ArrayList<>();

    @SerializedName("data")
    public List<LbLanding> getLbs() {
        return lbs;
    }

    @SerializedName("data")
    public void setLbs(List<LbLanding> lbs) {
        this.lbs = lbs;
    }
}