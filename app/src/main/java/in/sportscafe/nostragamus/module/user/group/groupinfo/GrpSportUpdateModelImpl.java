package in.sportscafe.nostragamus.module.user.group.groupinfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.common.ApiResponse;
import in.sportscafe.nostragamus.module.user.myprofile.dto.GroupInfo;
import in.sportscafe.nostragamus.module.user.sportselection.dto.Sport;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Jeeva on 14/6/16.
 */
public class GrpSportUpdateModelImpl {

    private Long mGroupId;

    private OnGrpSportUpdateModelListener mGrpSportUpdateModelListener;

    public GrpSportUpdateModelImpl(Long groupId, OnGrpSportUpdateModelListener listener) {
        this.mGroupId = groupId;
        this.mGrpSportUpdateModelListener = listener;
    }

    public void updateGrpSports(List<Integer> selectedSports) {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            callGrpSportUpdateApi(selectedSports);
        } else {
            mGrpSportUpdateModelListener.onNoInternet();
        }
    }

    private void callGrpSportUpdateApi(final List<Integer> selectedSports) {
        GroupSportUpdateRequest groupSportUpdateRequest = new GroupSportUpdateRequest();
        groupSportUpdateRequest.setAdminId(NostragamusDataHandler.getInstance().getUserId());
        groupSportUpdateRequest.setGroupId(mGroupId);
        groupSportUpdateRequest.setFollowedSports(selectedSports);

        MyWebService.getInstance().getGrpSportUpdateRequest(groupSportUpdateRequest).enqueue(
                new NostragamusCallBack<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        super.onResponse(call, response);
                        if (response.isSuccessful()) {
                            handleGrpSportUpdateResponse(selectedSports);
                        } else {
                            mGrpSportUpdateModelListener.onFailedGrpSportUpdate(response.message());
                        }
                    }
                }
        );
    }

    private void handleGrpSportUpdateResponse(List<Integer> selectedSports) {
        Map<Integer, Sport> allSportsMap = NostragamusDataHandler.getInstance().getAllSportsMap();
        List<Sport> followedSports = new ArrayList<>();

        for(Integer sportId : selectedSports) {
            followedSports.add(allSportsMap.get(sportId));
        }

        Map<Long, GroupInfo> grpInfoMap = NostragamusDataHandler.getInstance().getGrpInfoMap();
       // grpInfoMap.get(mGroupId).setFollowedTournaments(followedSports);
        NostragamusDataHandler.getInstance().setGrpInfoMap(grpInfoMap);

        mGrpSportUpdateModelListener.onSuccessGrpSportUpdate();
    }

    public interface OnGrpSportUpdateModelListener {

        void onSuccessGrpSportUpdate();

        void onFailedGrpSportUpdate(String message);

        void onNoInternet();
    }
}