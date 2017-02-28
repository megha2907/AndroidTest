package in.sportscafe.nostragamus.module.user.group.groupinfo;

import com.jeeva.android.Log;

import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.common.ApiResponse;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Jeeva on 14/6/16.
 */
public class GrpNameUpdateModelImpl {

    private Integer mGroupId;

    private OnGrpNameUpdateModelListener mGrpNameUpdateModelListener;

    public GrpNameUpdateModelImpl(Integer groupId, OnGrpNameUpdateModelListener listener) {
        this.mGroupId = groupId;
        this.mGrpNameUpdateModelListener = listener;
    }

    public void updateGrpName(String name, String photo) {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            callGrpNameUpdateApi(name, photo);
        } else {
            mGrpNameUpdateModelListener.onNoInternet();
        }
    }

    private void callGrpNameUpdateApi(final String name, String photo) {
        GroupNameUpdateRequest groupNameUpdateRequest = new GroupNameUpdateRequest();
        groupNameUpdateRequest.setGroupId(mGroupId);
        groupNameUpdateRequest.setGroupPhoto(photo);
        groupNameUpdateRequest.setGroupName(name);

        MyWebService.getInstance().getGrpNameUpdateRequest(groupNameUpdateRequest).enqueue(
                new NostragamusCallBack<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        super.onResponse(call, response);
                        if (response.isSuccessful()) {
                            handleGrpNameUpdateResponse(name);
                        } else {
                            mGrpNameUpdateModelListener.onFailedGrpNameUpdate(response.message());
                        }
                    }
                }
        );
    }

    private void handleGrpNameUpdateResponse(String groupName) {
        mGrpNameUpdateModelListener.onSuccessGrpNameUpdate();
    }

    public interface OnGrpNameUpdateModelListener {

        void onSuccessGrpNameUpdate();

        void onFailedGrpNameUpdate(String message);

        void onNoInternet();
    }
}