package in.sportscafe.scgame.module.user.myprofile.myposition.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeeva on 11/7/16.
 */
public class LbSummary implements Serializable {

    @JsonProperty("tournament_count")
    private Integer tourCount;

    @JsonProperty("total_points")
    private Integer totalPoints;

    @JsonProperty("global")
    private GroupSummary global;

    @JsonProperty("groups")
    private List<GroupSummary> groups = new ArrayList<>();

    @JsonProperty("tournament_count")
    public Integer getTourCount() {
        return tourCount;
    }

    @JsonProperty("tournament_count")
    public void setTourCount(Integer tourCount) {
        this.tourCount = tourCount;
    }

    @JsonProperty("total_points")
    public Integer getTotalPoints() {
        return totalPoints;
    }

    @JsonProperty("total_points")
    public void setTotalPoints(Integer totalPoints) {
        this.totalPoints = totalPoints;
    }

    @JsonProperty("global")
    public GroupSummary getGlobal() {
        return global;
    }

    @JsonProperty("global")
    public void setGlobal(GroupSummary global) {
        this.global = global;
    }

    @JsonProperty("groups")
    public List<GroupSummary> getGroups() {
        return groups;
    }

    @JsonProperty("groups")
    public void setGroups(List<GroupSummary> groups) {
        this.groups = groups;
    }
}