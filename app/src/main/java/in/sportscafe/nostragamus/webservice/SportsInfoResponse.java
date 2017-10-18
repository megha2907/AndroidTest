package in.sportscafe.nostragamus.webservice;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import in.sportscafe.nostragamus.module.prediction.playScreen.dto.PredictionQuestion;

/**
 * Created by deepanshi on 10/18/17.
 */

public class SportsInfoResponse {

    @SerializedName("data")
    private List<SportsInfo> sportsInfoList = null;

    public List<SportsInfo> getSportsInfoList() {
        return sportsInfoList;
    }

    public void setSportsInfoList(List<SportsInfo> sportsInfoList) {
        this.sportsInfoList = sportsInfoList;
    }
}
