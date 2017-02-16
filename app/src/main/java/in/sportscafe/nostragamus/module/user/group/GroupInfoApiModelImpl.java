package in.sportscafe.nostragamus.module.user.group;

import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.user.myprofile.dto.GroupInfo;
import in.sportscafe.nostragamus.webservice.GroupSummaryResponse;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Jeeva on 14/6/16.
 */
public class GroupInfoApiModelImpl {

    private OnGroupInfoApiModelListener mGroupInfoApiModelListener;

    public GroupInfoApiModelImpl(OnGroupInfoApiModelListener listener) {
        this.mGroupInfoApiModelListener = listener;
    }

    public static GroupInfoApiModelImpl newInstance(OnGroupInfoApiModelListener listener) {
        return new GroupInfoApiModelImpl(listener);
    }

    public void getGroupInfo(int groupId) {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            callGroupInfoApiApi(groupId);
        } else {
            mGroupInfoApiModelListener.onNoInternet();
        }
    }

    private void callGroupInfoApiApi(int groupId) {
        MyWebService.getInstance().getGroupSummaryRequest(groupId).enqueue(
                new NostragamusCallBack<GroupSummaryResponse>() {
                    @Override
                    public void onResponse(Call<GroupSummaryResponse> call, Response<GroupSummaryResponse> response) {
                        super.onResponse(call, response);
                        if (response.isSuccessful()) {
                            mGroupInfoApiModelListener.onSuccessGroupInfoApi(response.body().getGroupInfo());
                        } else {
                            mGroupInfoApiModelListener.onFailedGroupInfoApi(response.message());
                        }
                    }
                }
        );
    }

    public interface OnGroupInfoApiModelListener {

        void onSuccessGroupInfoApi(GroupInfo groupInfo);

        void onFailedGroupInfoApi(String message);

        void onNoInternet();
    }
}