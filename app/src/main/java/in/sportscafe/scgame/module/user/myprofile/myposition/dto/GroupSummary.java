package in.sportscafe.scgame.module.user.myprofile.myposition.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeeva on 11/7/16.
 */
public class GroupSummary implements Serializable {

    @JsonProperty("group_id")
    private Long groupId;

    @JsonProperty("group_name")
    private String groupName;

    @JsonProperty("rank")
    private Integer OverallRank;

    @JsonProperty("rank_change")
    private Integer OverallRankChange;

    @JsonProperty("tournaments")
    private List<GroupsTourSummary> tourSummaryList;

    public GroupSummary(Long groupId, String groupName) {
        this.groupId = groupId;
        this.groupName = groupName;
    }

    public GroupSummary() {
    }

    @JsonProperty("groupId")
    public Long getGroupId() {
        return groupId;
    }

    @JsonProperty("groupId")
    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    @JsonProperty("groupName")
    public String getGroupName() {
        return groupName;
    }

    @JsonProperty("groupName")
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @JsonProperty("rank")
    public Integer getOverallRank() {
        return OverallRank;
    }

    @JsonProperty("rank")
    public void setOverallRank(Integer overallRank) {
        OverallRank = overallRank;
    }

    @JsonProperty("rank_change")
    public Integer getOverallRankChange() {
        return OverallRankChange;
    }

    @JsonProperty("rank_change")
    public void setOverallRankChange(Integer overallRankChange) {
        OverallRankChange = overallRankChange;
    }

    @JsonProperty("tournaments")
    public List<GroupsTourSummary> getTourSummaryList() {
        return tourSummaryList;
    }

    @JsonProperty("tournaments")
    public void setTourSummaryList(List<GroupsTourSummary> tourSummaryList) {
        this.tourSummaryList = tourSummaryList;
    }

    @JsonIgnore
    public void addRank(GroupsTourSummary tourSummary) {
        tourSummaryList.add(tourSummary);
    }

}