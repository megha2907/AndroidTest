package in.sportscafe.nostragamus.module.user.group.groupinfo;

import com.google.gson.annotations.SerializedName;

import in.sportscafe.nostragamus.module.user.group.members.AdminRequest;
import in.sportscafe.nostragamus.module.user.group.members.MembersRequest;

/**
 * Created by Jeeva on 13/7/16.
 */
public class GroupNameUpdateRequest extends MembersRequest {

    @SerializedName("group_name")
    private String groupName;

    @SerializedName("group_img_url")
    private String groupPhoto;

    @SerializedName("group_name")
    public String getGroupName() {
        return groupName;
    }

    @SerializedName("group_name")
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @SerializedName("group_img_url")
    public String getGroupPhoto() {
        return groupPhoto;
    }

    @SerializedName("group_img_url")
    public void setGroupPhoto(String groupPhoto) {
        this.groupPhoto = groupPhoto;
    }
}