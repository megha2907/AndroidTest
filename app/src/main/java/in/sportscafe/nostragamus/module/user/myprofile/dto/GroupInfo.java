package in.sportscafe.nostragamus.module.user.myprofile.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeeva on 10/6/16.
 */
@Parcel
public class GroupInfo {

    @SerializedName("group_id")
    private Integer id;

    @SerializedName("group_name")
    private String name;

    @SerializedName("group_img_url")
    private String photo;

    @SerializedName("group_created_by")
    private Integer createdBy;

    @SerializedName("group_created_at")
    private String createdAt;

    @SerializedName("group_active")
    private boolean active;

    @SerializedName("group_deleted")
    private boolean deleted;

    @SerializedName("group_deleted_at")
    private String deletedAt;

    @SerializedName("group_deactivated_at")
    private String deactivatedAt;

    @SerializedName("group_members")
    private List<GroupPerson> members = new ArrayList<>();

    @SerializedName("group_tournaments_exp")
    private List<TournamentFeedInfo> followedTournaments = new ArrayList<>();

    @SerializedName("group_code")
    private String groupCode;

    @SerializedName("wallet_init")
    private Integer walletInitialAmount;

    @SerializedName("download_link")
    private String appDownloadLink;

    public GroupInfo() {
    }

    public GroupInfo(Integer id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The id
     */
    @SerializedName("group_id")
    public Integer getId() {
        return id;
    }

    /**
     *
     * @param id
     * The group_id
     */
    @SerializedName("group_id")
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The name
     */
    @SerializedName("group_name")
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The group_name
     */
    @SerializedName("group_name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The createdBy
     */
    @SerializedName("group_created_by")
    public Integer getCreatedBy() {
        return createdBy;
    }

    /**
     *
     * @param createdBy
     * The group_created_by
     */
    @SerializedName("group_created_by")
    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    /**
     *
     * @return
     * The createdAt
     */
    @SerializedName("group_created_at")
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     *
     * @param createdAt
     * The group_created_at
     */
    @SerializedName("group_created_at")
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     *
     * @return
     * The active
     */
    @SerializedName("group_active")
    public boolean getActive() {
        return active;
    }

    /**
     *
     * @param active
     * The group_active
     */
    @SerializedName("group_active")
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     *
     * @return
     * The deleted
     */
    @SerializedName("group_deleted")
    public boolean getDeleted() {
        return deleted;
    }

    /**
     *
     * @param deleted
     * The group_deleted
     */
    @SerializedName("group_deleted")
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    /**
     *
     * @return
     * The deletedAt
     */
    @SerializedName("group_deleted_at")
    public String getDeletedAt() {
        return deletedAt;
    }

    /**
     *
     * @param deletedAt
     * The group_deleted_at
     */
    @SerializedName("group_deleted_at")
    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }

    /**
     *
     * @return
     * The deactivatedAt
     */
    @SerializedName("group_deactivated_at")
    public String getDeactivatedAt() {
        return deactivatedAt;
    }

    /**
     *
     * @param deactivatedAt
     * The group_deactivated_at
     */
    @SerializedName("group_deactivated_at")
    public void setDeactivatedAt(String deactivatedAt) {
        this.deactivatedAt = deactivatedAt;
    }

    @SerializedName("group_members")
    public List<GroupPerson> getMembers() {
        return members;
    }

    @SerializedName("group_members")
    public void setMembers(List<GroupPerson> members) {
        this.members = members;
    }

    @SerializedName("group_tournaments_exp")
    public List<TournamentFeedInfo> getFollowedTournaments() {
        return followedTournaments;
    }

    @SerializedName("group_tournaments_exp")
    public void setFollowedTournaments(List<TournamentFeedInfo> followedTournaments) {
        this.followedTournaments = followedTournaments;
    }

    @SerializedName("group_code")
    public String getGroupCode() {
        return groupCode;
    }

    @SerializedName("group_code")
    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    @SerializedName("group_img_url")
    public String getPhoto() {
        return photo;
    }


    public void setPhoto(String photo) {
        this.photo = photo;
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

    @SerializedName("wallet_init")
    public Integer getWalletInitialAmount() {
        return walletInitialAmount;
    }

    @SerializedName("wallet_init")
    public void setWalletInitialAmount(Integer walletInitialAmount) {
        this.walletInitialAmount = walletInitialAmount;
    }

    @SerializedName("download_link")
    public String getAppDownloadLink() {
        return appDownloadLink;
    }

    @SerializedName("download_link")
    public void setAppDownloadLink(String appDownloadLink) {
        this.appDownloadLink = appDownloadLink;
    }

}