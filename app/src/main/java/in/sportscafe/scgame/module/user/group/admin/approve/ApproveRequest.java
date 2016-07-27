package in.sportscafe.scgame.module.user.group.admin.approve;

import com.fasterxml.jackson.annotation.JsonProperty;

import in.sportscafe.scgame.module.user.group.members.AdminRequest;

/**
 * Created by Jeeva on 2/7/16.
 */
public class ApproveRequest extends AdminRequest {

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