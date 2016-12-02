package in.sportscafe.nostragamus.module.user.group.groupinfo;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.user.myprofile.dto.GroupInfo;
import in.sportscafe.nostragamus.module.user.myprofile.dto.GroupsDetailResponse;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
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
        if(Nostragamus.getInstance().hasNetworkConnection()) {
            callGrpInfoApi();
        } else {
            if(NostragamusDataHandler.getInstance().getGrpInfoMap().isEmpty()) {
                mGrpListModelListener.onNoInternet();
            } else {
                mGrpListModelListener.onSuccessGrpList();
            }
        }
    }

    private void callGrpInfoApi() {
        MyWebService.getInstance().getGrpInfoRequest().enqueue(
                new NostragamusCallBack<GroupsDetailResponse>() {
            @Override
            public void onResponse(Call<GroupsDetailResponse> call, Response<GroupsDetailResponse> response) {
                super.onResponse(call, response);
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
        NostragamusDataHandler.getInstance().setGrpInfoList(grpInfoList);

        mGrpListModelListener.onSuccessGrpList();
    }

    public interface OnGrpListModelListener {

        void onSuccessGrpList();

        void onFailedGrpList(String message);

        void onNoInternet();
    }
}