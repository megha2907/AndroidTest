package in.sportscafe.scgame.module.user.myprofile.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import in.sportscafe.scgame.module.user.sportselection.dto.Sport;

/**
 * Created by Jeeva on 10/6/16.
 */
public class GroupInfo implements Serializable {

    @JsonProperty("group_id")
    private Long id;

    @JsonProperty("group_name")
    private String name;

    @JsonProperty("group_created_by")
    private Integer createdBy;

    @JsonProperty("group_created_at")
    private String createdAt;

    @JsonProperty("group_active")
    private boolean active;

    @JsonProperty("group_deleted")
    private boolean deleted;

    @JsonProperty("group_deleted_at")
    private String deletedAt;

    @JsonProperty("group_deactivated_at")
    private String deactivatedAt;

    @JsonProperty("group_members")
    private List<GroupPerson> members = new ArrayList<>();

    @JsonProperty("followed_sports")
    private List<Sport> followedSports = new ArrayList<>();

    @JsonProperty("group_code")
    private String groupCode;

    public GroupInfo() {
    }

    public GroupInfo(Long id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The id
     */
    @JsonProperty("group_id")
    public Long getId() {
        return id;
    }

    /**
     *
     * @param id
     * The group_id
     */
    @JsonProperty("group_id")
    public void setId(Long id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The name
     */
    @JsonProperty("group_name")
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The group_name
     */
    @JsonProperty("group_name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The createdBy
     */
    @JsonProperty("group_created_by")
    public Integer getCreatedBy() {
        return createdBy;
    }

    /**
     *
     * @param createdBy
     * The group_created_by
     */
    @JsonProperty("group_created_by")
    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    /**
     *
     * @return
     * The createdAt
     */
    @JsonProperty("group_created_at")
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     *
     * @param createdAt
     * The group_created_at
     */
    @JsonProperty("group_created_at")
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     *
     * @return
     * The active
     */
    @JsonProperty("group_active")
    public boolean getActive() {
        return active;
    }

    /**
     *
     * @param active
     * The group_active
     */
    @JsonProperty("group_active")
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     *
     * @return
     * The deleted
     */
    @JsonProperty("group_deleted")
    public boolean getDeleted() {
        return deleted;
    }

    /**
     *
     * @param deleted
     * The group_deleted
     */
    @JsonProperty("group_deleted")
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    /**
     *
     * @return
     * The deletedAt
     */
    @JsonProperty("group_deleted_at")
    public String getDeletedAt() {
        return deletedAt;
    }

    /**
     *
     * @param deletedAt
     * The group_deleted_at
     */
    @JsonProperty("group_deleted_at")
    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }

    /**
     *
     * @return
     * The deactivatedAt
     */
    @JsonProperty("group_deactivated_at")
    public String getDeactivatedAt() {
        return deactivatedAt;
    }

    /**
     *
     * @param deactivatedAt
     * The group_deactivated_at
     */
    @JsonProperty("group_deactivated_at")
    public void setDeactivatedAt(String deactivatedAt) {
        this.deactivatedAt = deactivatedAt;
    }

    @JsonProperty("group_members")
    public List<GroupPerson> getMembers() {
        return members;
    }

    @JsonProperty("group_members")
    public void setMembers(List<GroupPerson> members) {
        this.members = members;
    }

    @JsonProperty("followed_sports")
    public List<Sport> getFollowedSports() {
        return followedSports;
    }

    @JsonProperty("followed_sports")
    public void setFollowedSports(List<Sport> followedSports) {
        this.followedSports = followedSports;
    }

    @JsonProperty("group_code")
    public String getGroupCode() {
        return groupCode;
    }

    @JsonProperty("group_code")
    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    @Override
    public boolean equals(Object o) {
        GroupInfo groupInfo = (GroupInfo) o;
        return id == groupInfo.getId();
    }

    @Override
    public String toString() {
        return name;
    }
}