package in.sportscafe.nostragamus.module.user.sportselection.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeeva on 30/6/16.
 */
public class PreferenceRequest {


    @JsonProperty("sports_preferences")
    private List<Integer> sportPreferences = new ArrayList<Integer>();

    public PreferenceRequest() {
    }

    /**
     * @return The sportPreferences
     */
    @JsonProperty("sports_preferences")
    public List<Integer> getSportPreferences() {
        return sportPreferences;
    }

    /**
     * @param sportPreferences The sport_preferences
     */
    @JsonProperty("sports_preferences")
    public void setSportPreferences(List<Integer> sportPreferences) {
        this.sportPreferences = sportPreferences;
    }
}