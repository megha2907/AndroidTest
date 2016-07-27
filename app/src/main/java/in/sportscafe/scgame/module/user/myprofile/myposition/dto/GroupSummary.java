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

    @JsonProperty("alltime")
    private List<RankSummary> ranks = new ArrayList<>();

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

    @JsonProperty("ranks")
    public List<RankSummary> getRanks() {
        return ranks;
    }

    @JsonProperty("ranks")
    public void setRanks(List<RankSummary> ranks) {
        this.ranks = ranks;
    }

    @JsonIgnore
    public void addRank(RankSummary rankSummary) {
        ranks.add(rankSummary);
    }
}