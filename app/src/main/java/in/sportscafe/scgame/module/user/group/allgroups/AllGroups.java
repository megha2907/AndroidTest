package in.sportscafe.scgame.module.user.group.allgroups;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by deepanshi on 9/27/16.
 */

public class AllGroups implements Serializable {


    @JsonProperty("group_id")
    private Long groupId;

    @JsonProperty("group_name")
    private String groupName;

    @JsonProperty("group_img_url")
    private String groupPhoto;

    @JsonProperty("count_group_members")
    private Integer countGroupMembers;

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

    @JsonProperty("count_group_members")
    public Integer getCountGroupMembers() {
        return countGroupMembers;
    }

    @JsonProperty("count_group_members")
    public void setCountGroupMembers(Integer countGroupMembers) {
        this.countGroupMembers = countGroupMembers;
    }

    @JsonProperty("group_img_url")
    public String getGroupPhoto() {
        return groupPhoto;
    }

    @JsonProperty("group_img_url")
    public void setGroupPhoto(String groupPhoto) {
        this.groupPhoto = groupPhoto;
    }


}

