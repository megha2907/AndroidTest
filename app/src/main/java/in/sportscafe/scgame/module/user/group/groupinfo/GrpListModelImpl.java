package in.sportscafe.scgame.module.user.group.groupinfo;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.scgame.ScGame;
import in.sportscafe.scgame.ScGameDataHandler;
import in.sportscafe.scgame.module.user.myprofile.dto.GroupInfo;
import in.sportscafe.scgame.module.user.myprofile.dto.GroupsDetailResponse;
import in.sportscafe.scgame.webservice.MyWebService;
import in.sportscafe.scgame.webservice.ScGameCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Jeeva on 14/6/16.
 */
public class GrpListModelImpl {

    private OnGrpListModelListener mGrpListModelListener;

    public GrpListModelImpl(OnGrpListModelListener listener) {
        this.mGrpListModelListener = listener;
    }

    public void getGrpList() {
        if(ScGame.getInstance().hasNetworkConnection()) {
            callGrpInfoApi();
        } else {
            if(ScGameDataHandler.getInstance().getGrpInfoMap().isEmpty()) {
                mGrpListModelListener.onNoInternet();
            } else {
                mGrpListModelListener.onSuccessGrpList();
            }
        }
    }

    private void callGrpInfoApi() {
        MyWebService.getInstance().getGrpInfoRequest().enqueue(
                new ScGameCallBack<GroupsDetailResponse>() {
            @Override
            public void onResponse(Call<GroupsDetailResponse> call, Response<GroupsDetailResponse> response) {
                if (response.isSuccessful()) {
                    List<GroupInfo> grpInfoList = response.body().getGroupsDetail();

                    handleGrpDetailResponse(grpInfoList);
                } else {
                    mGrpListModelListener.onFailedGrpList(response.message());
                }
            }
        });
    }

    private void handleGrpDetailResponse(List<GroupInfo> grpInfoList) {
        if(null == grpInfoList) {
            grpInfoList = new ArrayList<>();
        }
        ScGameDataHandler.getInstance().setGrpInfoList(grpInfoList);

        mGrpListModelListener.onSuccessGrpList();
    }

    public interface OnGrpListModelListener {

        void onSuccessGrpList();

        void onFailedGrpList(String message);

        void onNoInternet();
    }
}