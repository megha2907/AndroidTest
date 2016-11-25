package in.sportscafe.nostragamus.module.user.sportselection.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeeva on 30/6/16.
 */
public class PreferenceRequest {

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("sports_preferences")
    private List<Integer> sportPreferences = new ArrayList<Integer>();

    public PreferenceRequest(String userId) {
        this.userId = userId;
    }

    /**
     * @return The userId
     */
    @JsonProperty("user_id")
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId The user_id
     */
    @JsonProperty("user_id")
    public void setUserId(String userId) {
        this.userId = userId;
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