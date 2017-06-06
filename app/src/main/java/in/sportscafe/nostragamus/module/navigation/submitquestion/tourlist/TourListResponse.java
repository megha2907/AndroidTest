package in.sportscafe.nostragamus.module.navigation.submitquestion.tourlist;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeeva on 24/03/17.
 */
public class TourListResponse {

    @JsonProperty("data")
    private List<Tour> tourList = new ArrayList<>();

    @JsonProperty("data")
    public List<Tour> getTourList() {
        return tourList;
    }

    @JsonProperty("data")
    public void setTourList(List<Tour> tourList) {
        this.tourList = tourList;
    }
}