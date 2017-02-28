package in.sportscafe.nostragamus.module.user.group;

import in.sportscafe.nostragamus.Constants.Alerts;
import in.sportscafe.nostragamus.Constants.AnalyticsActions;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.common.ApiResponse;
import in.sportscafe.nostragamus.module.user.group.members.AdminRequest;
import in.sportscafe.nostragamus.module.user.group.members.MembersRequest;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Jeeva on 14/6/16.
 */
public class DeleteGroupModelImpl {

    private OnDeleteGroupModelListener mDeleteGroupModelListener;

    public DeleteGroupModelImpl(OnDeleteGroupModelListener listener) {
        this.mDeleteGroupModelListener = listener;
    }

    public void deleteGroup(boolean amAdmin, Integer groupId) {
        if (amAdmin) {
            if (Nostragamus.getInstance().hasNetworkConnection()) {
                MembersRequest membersRequest = new MembersRequest();
                membersRequest.setGroupId(groupId);

                callDeleteGroupApi(membersRequest);
            } else {
                mDeleteGroupModelListener.onNoInternet();
            }
        }
    }

    private void callDeleteGroupApi(MembersRequest membersRequest) {
        MyWebService.getInstance().getDeleteGroupRequest(membersRequest).enqueue(
                new NostragamusCallBack<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        super.onResponse(call, response);
                        if (response.isSuccessful()) {
                            NostragamusAnalytics.getInstance().trackGroups(AnalyticsActions.DELETE_GROUP, null);
                            mDeleteGroupModelListener.onSuccessDeleteGroup();
                        } else {
                            mDeleteGroupModelListener.onFailedDeleteGroup(Alerts.CANNOT_LEAVE_GROUP);
                        }
                    }
                }
        );
    }

    public interface OnDeleteGroupModelListener {

        void onSuccessDeleteGroup();

        void onFailedDeleteGroup(String message);

        void onNoInternet();
    }
}