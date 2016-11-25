package in.sportscafe.nostragamus.module.user.myprofile.myposition.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeeva on 11/7/16.
 */
public class LbSummary implements Serializable {


    @JsonProperty("total_points")
    private Integer totalPoints;

    @JsonProperty("groups")
    private List<GroupSummary> groups = new ArrayList<>();

    @JsonProperty("sports")
    private List<SportSummary> sports = new ArrayList<>();

    @JsonProperty("challenges")
    private List<ChallengesSummary> challenges = new ArrayList<>();


    @JsonProperty("total_points")
    public Integer getTotalPoints() {
        return totalPoints;
    }

    @JsonProperty("total_points")
    public void setTotalPoints(Integer totalPoints) {
        this.totalPoints = totalPoints;
    }

    @JsonProperty("groups")
    public List<GroupSummary> getGroups() {
        return groups;
    }

    @JsonProperty("groups")
    public void setGroups(List<GroupSummary> groups) {
        this.groups = groups;
    }

    @JsonProperty("sports")
    public List<SportSummary> getSports() {
        return sports;
    }

    @JsonProperty("sports")
    public void setSports(List<SportSummary> sports) {
        this.sports = sports;
    }

    @JsonProperty("challenges")
    public List<ChallengesSummary> getChallenges() {
        return challenges;
    }

    @JsonProperty("challenges")
    public void setChallenges(List<ChallengesSummary> challenges) {
        this.challenges = challenges;
    }
}