package in.sportscafe.scgame.module.user.group.groupinfo;

import java.util.Map;

import in.sportscafe.scgame.ScGame;
import in.sportscafe.scgame.ScGameDataHandler;
import in.sportscafe.scgame.module.common.ApiResponse;
import in.sportscafe.scgame.module.user.myprofile.dto.GroupInfo;
import in.sportscafe.scgame.webservice.MyWebService;
import in.sportscafe.scgame.webservice.ScGameCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Jeeva on 14/6/16.
 */
public class GrpNameUpdateModelImpl {

    private Long mGroupId;

    private OnGrpNameUpdateModelListener mGrpNameUpdateModelListener;

    public GrpNameUpdateModelImpl(Long groupId, OnGrpNameUpdateModelListener listener) {
        this.mGroupId = groupId;
        this.mGrpNameUpdateModelListener = listener;
    }

    public void updateGrpName(String name,String photo) {
        if (ScGame.getInstance().hasNetworkConnection()) {
            callGrpNameUpdateApi(name,photo);
        } else {
            mGrpNameUpdateModelListener.onNoInternet();
        }
    }

    private void callGrpNameUpdateApi(final String name,String photo) {
        GroupNameUpdateRequest groupNameUpdateRequest = new GroupNameUpdateRequest();
        groupNameUpdateRequest.setAdminId(ScGameDataHandler.getInstance().getUserId());
        groupNameUpdateRequest.setGroupId(mGroupId);
        groupNameUpdateRequest.setGroupPhoto(photo);
        groupNameUpdateRequest.setGroupName(name);

        MyWebService.getInstance().getGrpNameUpdateRequest(groupNameUpdateRequest).enqueue(
                new ScGameCallBack<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
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
        Map<Long, GroupInfo> grpInfoMap = ScGameDataHandler.getInstance().getGrpInfoMap();
        grpInfoMap.get(mGroupId).setName(groupName);
        ScGameDataHandler.getInstance().setGrpInfoMap(grpInfoMap);

        mGrpNameUpdateModelListener.onSuccessGrpNameUpdate();
    }

    public interface OnGrpNameUpdateModelListener {

        void onSuccessGrpNameUpdate();

        void onFailedGrpNameUpdate(String message);

        void onNoInternet();
    }
}