package in.sportscafe.nostragamus.module.user.group.admin.approve;

import com.fasterxml.jackson.annotation.JsonProperty;

import in.sportscafe.nostragamus.module.user.group.members.AdminRequest;
import in.sportscafe.nostragamus.module.user.group.members.MembersRequest;

/**
 * Created by Jeeva on 2/7/16.
 */
public class ApproveRequest extends MembersRequest {

    @JsonProperty("is_approved")
    private boolean approved;

    @JsonProperty("is_approved")
    public boolean isApproved() {
        return approved;
    }

    @JsonProperty("is_approved")
    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}