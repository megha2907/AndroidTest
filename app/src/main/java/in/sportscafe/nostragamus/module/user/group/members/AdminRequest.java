package in.sportscafe.nostragamus.module.user.group.members;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jeeva on 2/7/16.
 */
public class AdminRequest extends MembersRequest {

    @SerializedName("admin_id")
    private String adminId;

    @SerializedName("admin_id")
    public String getAdminId() {
        return adminId;
    }

    @SerializedName("admin_id")
    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }
}