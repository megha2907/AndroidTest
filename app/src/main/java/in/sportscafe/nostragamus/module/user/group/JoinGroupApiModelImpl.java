package in.sportscafe.nostragamus.module.user.group;

import java.util.HashMap;
import java.util.Map;

import in.sportscafe.nostragamus.Constants.AnalyticsActions;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.user.group.joingroup.dto.JoinGroupResponse;
import in.sportscafe.nostragamus.module.user.myprofile.dto.GroupInfo;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Jeeva on 14/6/16.
 */
public class JoinGroupApiModelImpl {

    private OnJoinGroupApiModelListener mJoinGroupApiModelListener;

    public JoinGroupApiModelImpl(OnJoinGroupApiModelListener listener) {
        this.mJoinGroupApiModelListener = listener;
    }

    public static JoinGroupApiModelImpl newInstance(OnJoinGroupApiModelListener listener) {
        return new JoinGroupApiModelImpl(listener);
    }

    public void joinGroup(String groupCode, boolean fromReferral) {
        if(isValidGroupCode(groupCode)) {
            if (Nostragamus.getInstance().hasNetworkConnection()) {
                callJoinGroupApi(groupCode, fromReferral);
            } else {
                mJoinGroupApiModelListener.onNoInternet();
            }
        } else {
            mJoinGroupApiModelListener.onInvalidGroupCode();
        }
    }

    public boolean isValidGroupCode(String groupCode) {
        return groupCode.length() == 5;
    }

    private void callJoinGroupApi(String groupCode, final boolean fromReferral) {
        MyWebService.getInstance().getJoinGroupRequest(groupCode).enqueue(
                new NostragamusCallBack<JoinGroupResponse>() {
                    @Override
                    public void onResponse(Call<JoinGroupResponse> call, Response<JoinGroupResponse> response) {
                        super.onResponse(call, response);
                        if (response.isSuccessful()) {
                            NostragamusAnalytics.getInstance().trackGroups(AnalyticsActions.JOIN_GROUP, null);

                            if (fromReferral) {
                                // Removing group code since user joined in that group
                                NostragamusDataHandler.getInstance().setInstallGroupCode(null);
                            }

                            getGroupInfo(response.body().getJoinGroup().getGroupId());
                        } else {
                            mJoinGroupApiModelListener.onFailedJoinGroupApi(response.message());
                        }
                    }
                }
        );

        Map<String, String> values = new HashMap<>();
        values.put("GroupCode", groupCode);
        values.put("UserID", NostragamusDataHandler.getInstance().getUserId());

        NostragamusAnalytics.getInstance().trackOtherEvents("JOIN GROUP-ONCLICK", values);
    }

    private void getGroupInfo(int groupId) {
        GroupInfoApiModelImpl.newInstance(new GroupInfoApiModelImpl.OnGroupInfoApiModelListener() {
            @Override
            public void onSuccessGroupInfoApi(GroupInfo groupInfo) {
                mJoinGroupApiModelListener.onSuccessJoinGroupApi(groupInfo);
            }

            @Override
            public void onFailedGroupInfoApi(String message) {
                mJoinGroupApiModelListener.onFailedJoinGroupApi(message);
            }

            @Override
            public void onNoInternet() {
                mJoinGroupApiModelListener.onNoInternet();
            }
        }).getGroupInfo(groupId);
    }

    public interface OnJoinGroupApiModelListener {

        void onSuccessJoinGroupApi(GroupInfo groupInfo);

        void onFailedJoinGroupApi(String message);

        void onNoInternet();

        void onInvalidGroupCode();
    }
}