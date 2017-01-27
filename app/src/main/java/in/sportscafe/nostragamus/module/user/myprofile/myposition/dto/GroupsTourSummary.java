package in.sportscafe.nostragamus.module.user.myprofile.myposition.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by deepanshi on 9/21/16.
 */
public class GroupsTourSummary extends BaseSummary implements Serializable {

    public GroupsTourSummary() {
    }

    public GroupsTourSummary(Integer tournamentId, String tournamentName, String tournamentImageUrl, Integer rank, Integer rankChange, Integer overallRank, Integer overallRankChange, Integer groupId, String groupName,String groupIcon) {
        super(groupName,groupIcon, tournamentId, tournamentName, tournamentImageUrl, rank, rankChange, overallRank, overallRankChange);
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupIcon=groupIcon;
    }

    @JsonProperty("group_id")
    private Integer groupId;

    @JsonProperty("group_name")
    private String groupName;

    @JsonProperty("group_img_url")
    private String groupIcon;

    @JsonProperty("group_id")
    public Integer getGroupId() {
        return groupId;
    }

    @JsonProperty("group_id")
    public void setGroupId(Integer groupId) {
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

    @JsonProperty("group_img_url")
    public String getGroupIcon() {
        return groupIcon;
    }

    @JsonProperty("group_img_url")
    public void setGroupIcon(String groupIcon) {
        this.groupIcon = groupIcon;
    }
}