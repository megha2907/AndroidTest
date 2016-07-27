package in.sportscafe.scgame.module.user.group.groupinfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import in.sportscafe.scgame.ScGame;
import in.sportscafe.scgame.ScGameDataHandler;
import in.sportscafe.scgame.module.common.ApiResponse;
import in.sportscafe.scgame.module.user.myprofile.dto.GroupInfo;
import in.sportscafe.scgame.module.user.sportselection.dto.Sport;
import in.sportscafe.scgame.webservice.MyWebService;
import in.sportscafe.scgame.webservice.ScGameCallBack;
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
        if (ScGame.getInstance().hasNetworkConnection()) {
            callGrpSportUpdateApi(selectedSports);
        } else {
            mGrpSportUpdateModelListener.onNoInternet();
        }
    }

    private void callGrpSportUpdateApi(final List<Integer> selectedSports) {
        GroupSportUpdateRequest groupSportUpdateRequest = new GroupSportUpdateRequest();
        groupSportUpdateRequest.setAdminId(ScGameDataHandler.getInstance().getUserId());
        groupSportUpdateRequest.setGroupId(mGroupId);
        groupSportUpdateRequest.setFollowedSports(selectedSports);

        MyWebService.getInstance().getGrpSportUpdateRequest(groupSportUpdateRequest).enqueue(
                new ScGameCallBack<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
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
        Map<Integer, Sport> allSportsMap = ScGameDataHandler.getInstance().getAllSportsMap();
        List<Sport> followedSports = new ArrayList<>();

        for(Integer sportId : selectedSports) {
            followedSports.add(allSportsMap.get(sportId));
        }

        Map<Long, GroupInfo> grpInfoMap = ScGameDataHandler.getInstance().getGrpInfoMap();
        grpInfoMap.get(mGroupId).setFollowedSports(followedSports);
        ScGameDataHandler.getInstance().setGrpInfoMap(grpInfoMap);

        mGrpSportUpdateModelListener.onSuccessGrpSportUpdate();
    }

    public interface OnGrpSportUpdateModelListener {

        void onSuccessGrpSportUpdate();

        void onFailedGrpSportUpdate(String message);

        void onNoInternet();
    }
}