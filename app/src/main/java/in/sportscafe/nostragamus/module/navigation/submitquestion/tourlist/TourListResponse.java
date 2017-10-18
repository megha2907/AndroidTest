package in.sportscafe.nostragamus.module.navigation.submitquestion.tourlist;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeeva on 24/03/17.
 */
public class TourListResponse {

    @SerializedName("data")
    private List<Tour> tourList = new ArrayList<>();

    @SerializedName("data")
    public List<Tour> getTourList() {
        return tourList;
    }

    @SerializedName("data")
    public void setTourList(List<Tour> tourList) {
        this.tourList = tourList;
    }
}