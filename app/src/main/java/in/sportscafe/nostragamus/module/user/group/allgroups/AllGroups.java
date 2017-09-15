package in.sportscafe.nostragamus.module.user.group.allgroups;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by deepanshi on 9/27/16.
 */
@Parcel
public class AllGroups {

    @SerializedName("group_id")
    private Integer groupId;

    @SerializedName("group_name")
    private String groupName;

    @SerializedName("group_img_url")
    private String groupPhoto;

    @SerializedName("count_group_members")
    private Integer countGroupMembers;

    @SerializedName("no_of_tournaments")
    private Integer tournamentsCount;

    @SerializedName("group_id")
    public Integer getGroupId() {
        return groupId;
    }

    @SerializedName("group_id")
    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    @SerializedName("group_name")
    public String getGroupName() {
        return groupName;
    }

    @SerializedName("group_name")
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @SerializedName("count_group_members")
    public Integer getCountGroupMembers() {
        if (null==countGroupMembers){
            countGroupMembers = 0;
        }
        return countGroupMembers;
    }

    @SerializedName("count_group_members")
    public void setCountGroupMembers(Integer countGroupMembers) {
        this.countGroupMembers = countGroupMembers;
    }

    @SerializedName("group_img_url")
    public String getGroupPhoto() {
        return groupPhoto;
    }

    @SerializedName("group_img_url")
    public void setGroupPhoto(String groupPhoto) {
        this.groupPhoto = groupPhoto;
    }

    @SerializedName("no_of_tournaments")
    public Integer getTournamentsCount() {
        return tournamentsCount;
    }

    @SerializedName("no_of_tournaments")
    public void setTournamentsCount(Integer tournamentsCount) {
        this.tournamentsCount = tournamentsCount;
    }

}
