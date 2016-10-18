package in.sportscafe.scgame.module.user.myprofile.myposition.dto;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jeeva.android.Log;

import java.io.Serializable;
import java.util.List;

/**
 * Created by deepanshi on 9/21/16.
 */
public class GroupsTourSummary extends BaseSummary implements Serializable {

    public GroupsTourSummary() {
    }

    public GroupsTourSummary(Integer tournamentId, String tournamentName, String tournamentImageUrl, Integer rank, Integer rankChange, Integer overallRank, Integer overallRankChange, Long groupId, String groupName) {
        super(groupName, tournamentId, tournamentName, tournamentImageUrl, rank, rankChange, overallRank, overallRankChange);
        this.groupId = groupId;
        this.groupName = groupName;
    }

    @JsonProperty("group_id")
    private Long groupId;

    @JsonProperty("group_name")
    private String groupName;

    @JsonProperty("group_id")
    public Long getGroupId() {
        return groupId;
    }

    @JsonProperty("group_id")
    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    @JsonProperty("group_name")
    public String getGroupName() {
        return groupName;
    }

    @JsonProperty("group_name")
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}