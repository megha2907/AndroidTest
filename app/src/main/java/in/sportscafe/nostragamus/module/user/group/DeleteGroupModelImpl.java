package in.sportscafe.nostragamus.module.user.group;

import java.util.List;

import in.sportscafe.nostragamus.Constants.Alerts;
import in.sportscafe.nostragamus.Constants.AnalyticsActions;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.common.ApiResponse;
import in.sportscafe.nostragamus.module.user.group.members.AdminRequest;
import in.sportscafe.nostragamus.module.user.myprofile.dto.GroupPerson;
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
        List<GroupPerson> members = NostragamusDataHandler.getInstance().getGrpInfoMap().get(groupId).getMembers();
        if (members.size() > 0 && amAdmin) {
            if (Nostragamus.getInstance().hasNetworkConnection()) {
                AdminRequest adminRequest = new AdminRequest();
                adminRequest.setAdminId(NostragamusDataHandler.getInstance().getUserId());
                adminRequest.setGroupId(groupId);

                callDeleteGroupApi(adminRequest);
            } else {
                mDeleteGroupModelListener.onNoInternet();
            }
        }
    }

    private void callDeleteGroupApi(AdminRequest adminRequest) {
        MyWebService.getInstance().getDeleteGroupRequest(adminRequest).enqueue(
                new NostragamusCallBack<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        super.onResponse(call, response);
                        if(response.isSuccessful()) {
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