package in.sportscafe.nostragamus.module.user.lblanding;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeeva on 19/01/17.
 */

public class LBLandingSummary {

    @JsonProperty("total_points")
    private Integer totalPoints = 0;

    @JsonProperty("sports")
    private List<LbLanding> sports = new ArrayList<>();

    @JsonProperty("groups")
    private List<LbLanding> groups = new ArrayList<>();

    @JsonProperty("challenges")
    private List<LbLanding> challenges = new ArrayList<>();

    @JsonProperty("total_points")
    public Integer getTotalPoints() {
        return totalPoints;
    }

    @JsonProperty("total_points")
    public void setTotalPoints(Integer totalPoints) {
        this.totalPoints = totalPoints;
    }

    @JsonProperty("sports")
    public List<LbLanding> getSports() {
        return sports;
    }

    @JsonProperty("sports")
    public void setSports(List<LbLanding> sports) {
        this.sports = sports;
    }

    @JsonProperty("groups")
    public List<LbLanding> getGroups() {
        return groups;
    }

    @JsonProperty("groups")
    public void setGroups(List<LbLanding> groups) {
        this.groups = groups;
    }

    @JsonProperty("challenges")
    public List<LbLanding> getChallenges() {
        return challenges;
    }

    @JsonProperty("challenges")
    public void setChallenges(List<LbLanding> challenges) {
        this.challenges = challenges;
    }
}