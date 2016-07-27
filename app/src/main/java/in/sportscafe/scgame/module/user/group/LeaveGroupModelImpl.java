package in.sportscafe.scgame.module.user.group;

import java.util.List;

import in.sportscafe.scgame.Constants;
import in.sportscafe.scgame.ScGame;
import in.sportscafe.scgame.ScGameDataHandler;
import in.sportscafe.scgame.module.common.ApiResponse;
import in.sportscafe.scgame.module.user.group.members.MembersRequest;
import in.sportscafe.scgame.module.user.myprofile.dto.GroupPerson;
import in.sportscafe.scgame.webservice.MyWebService;
import in.sportscafe.scgame.webservice.ScGameCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Jeeva on 14/6/16.
 */
public class LeaveGroupModelImpl {

    private OnLeaveGroupModelListener mLeaveGroupModelListener;

    public LeaveGroupModelImpl(OnLeaveGroupModelListener listener) {
        this.mLeaveGroupModelListener = listener;
    }

    public void leaveGroup(boolean amAdmin, Long groupId) {
        List<GroupPerson> members = ScGameDataHandler.getInstance().getGrpInfoMap().get(groupId).getMembers();
        if(members.size() > 1 && amAdmin) {
            int count = 0;
            for (GroupPerson groupPerson : members) {
                if (groupPerson.isAdmin() && ++count > 1) {
                    break;
                }
            }

            if(count <= 1) {
                mLeaveGroupModelListener.onFailedLeaveGroup(Constants.Alerts.CANNOT_LEAVE_GROUP);
                return;
            }
        }

        if (ScGame.getInstance().hasNetworkConnection()) {
            MembersRequest membersRequest = new MembersRequest();
            membersRequest.setUserId(ScGameDataHandler.getInstance().getUserId());
            membersRequest.setGroupId(groupId);

            callLeaveGroupApi(membersRequest);
        } else {
            mLeaveGroupModelListener.onNoInternet();
        }
    }

    private void callLeaveGroupApi(MembersRequest membersRequest) {
        MyWebService.getInstance().getLeaveGroupRequest(membersRequest).enqueue(
                new ScGameCallBack<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if(response.isSuccessful()) {
                            mLeaveGroupModelListener.onSuccessLeaveGroup();
                        } else {
                            mLeaveGroupModelListener.onFailedLeaveGroup(response.message());
                        }
                    }
                }
        );
    }

    public interface OnLeaveGroupModelListener {

        void onSuccessLeaveGroup();

        void onFailedLeaveGroup(String message);

        void onNoInternet();
    }
}