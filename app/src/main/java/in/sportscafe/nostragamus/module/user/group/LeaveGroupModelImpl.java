package in.sportscafe.nostragamus.module.user.group;

import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.Alerts;
import in.sportscafe.nostragamus.Constants.AnalyticsActions;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.common.ApiResponse;
import in.sportscafe.nostragamus.module.user.group.members.MembersRequest;
import in.sportscafe.nostragamus.module.user.myprofile.dto.GroupPerson;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
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

    public void leaveGroup(boolean amAdmin, Integer groupId) {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            MembersRequest membersRequest = new MembersRequest();
            membersRequest.setGroupId(groupId);

            callLeaveGroupApi(membersRequest);
        } else {
            mLeaveGroupModelListener.onNoInternet();
        }
    }

    private void callLeaveGroupApi(MembersRequest membersRequest) {
        MyWebService.getInstance().getLeaveGroupRequest(membersRequest).enqueue(
                new NostragamusCallBack<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        super.onResponse(call, response);
                        if(response.isSuccessful()) {
                            NostragamusAnalytics.getInstance().trackGroups(AnalyticsActions.LEAVE_GROUP, null);
                            mLeaveGroupModelListener.onSuccessLeaveGroup();
                        } else {
                            mLeaveGroupModelListener.onFailedLeaveGroup(Alerts.CANNOT_LEAVE_GROUP);
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