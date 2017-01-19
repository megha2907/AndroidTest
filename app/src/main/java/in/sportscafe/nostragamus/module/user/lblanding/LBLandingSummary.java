package in.sportscafe.nostragamus.module.user.lblanding;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeeva on 19/01/17.
 */

public class LBLandingSummary {

    @JsonProperty("sports")
    private List<LBLanding> sports = new ArrayList<>();

    @JsonProperty("groups")
    private List<LBLanding> groups = new ArrayList<>();

    @JsonProperty("challenges")
    private List<LBLanding> challenges = new ArrayList<>();

    @JsonProperty("sports")
    public List<LBLanding> getSports() {
        return sports;
    }

    @JsonProperty("sports")
    public void setSports(List<LBLanding> sports) {
        this.sports = sports;
    }

    @JsonProperty("groups")
    public List<LBLanding> getGroups() {
        return groups;
    }

    @JsonProperty("groups")
    public void setGroups(List<LBLanding> groups) {
        this.groups = groups;
    }

    @JsonProperty("challenges")
    public List<LBLanding> getChallenges() {
        return challenges;
    }

    @JsonProperty("challenges")
    public void setChallenges(List<LBLanding> challenges) {
        this.challenges = challenges;
    }
}