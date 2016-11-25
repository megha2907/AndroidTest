package in.sportscafe.nostragamus.module.user.group.members;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Jeeva on 2/7/16.
 */
public class AdminRequest extends MembersRequest {

    @JsonProperty("admin_id")
    private String adminId;

    @JsonProperty("admin_id")
    public String getAdminId() {
        return adminId;
    }

    @JsonProperty("admin_id")
    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }
}