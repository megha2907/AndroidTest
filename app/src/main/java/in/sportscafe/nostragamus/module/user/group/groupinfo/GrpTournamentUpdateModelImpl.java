package in.sportscafe.nostragamus.module.user.group.groupinfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.common.ApiResponse;
import in.sportscafe.nostragamus.module.TournamentFeed.dto.TournamentFeedInfo;
import in.sportscafe.nostragamus.module.user.myprofile.dto.GroupInfo;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by deepanshi on 9/28/16.
 */

public class GrpTournamentUpdateModelImpl {

    private Long mGroupId;

    private GrpTournamentUpdateModelImpl.OnGrpTournamentUpdateModelListener mGrpTournamentUpdateModelListener;

    public GrpTournamentUpdateModelImpl(Long groupId, GrpTournamentUpdateModelImpl.OnGrpTournamentUpdateModelListener listener) {
        this.mGroupId = groupId;
        this.mGrpTournamentUpdateModelListener = listener;
    }

    public void updateGrpTournaments(List<Integer> selectedTournaments) {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            callGrpTournamentUpdateApi(selectedTournaments);
        } else {
            mGrpTournamentUpdateModelListener.onNoInternet();
        }
    }

    private void callGrpTournamentUpdateApi(final List<Integer> selectedTournaments) {
        GroupTournamentUpdateRequest groupTournamentUpdateRequest = new GroupTournamentUpdateRequest();
        groupTournamentUpdateRequest.setAdminId(NostragamusDataHandler.getInstance().getUserId());
        groupTournamentUpdateRequest.setGroupId(mGroupId);
        groupTournamentUpdateRequest.setFollowedTournaments(selectedTournaments);

        MyWebService.getInstance().getGrpTournamentUpdateRequest(groupTournamentUpdateRequest).enqueue(
                new NostragamusCallBack<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        super.onResponse(call, response);
                        if (response.isSuccessful()) {
                            handleGrpTournamentUpdateResponse(selectedTournaments);
                        } else {
                            mGrpTournamentUpdateModelListener.onFailedGrpTournamentUpdate(response.message());
                        }
                    }
                }
        );
    }

    private void handleGrpTournamentUpdateResponse(List<Integer> selectedTournaments) {
        Map<Integer, TournamentFeedInfo> allTournamentsMap = NostragamusDataHandler.getInstance().getTournamentsMap();
        List<TournamentFeedInfo> followedtournaments = new ArrayList<>();

        for(Integer TournamentId : selectedTournaments) {
            followedtournaments.add(allTournamentsMap.get(TournamentId));
        }

        Map<Long, GroupInfo> grpInfoMap = NostragamusDataHandler.getInstance().getGrpInfoMap();
        grpInfoMap.get(mGroupId).setFollowedTournaments(followedtournaments);
        NostragamusDataHandler.getInstance().setGrpInfoMap(grpInfoMap);

        mGrpTournamentUpdateModelListener.onSuccessGrpTournamentUpdate();
    }

    public interface OnGrpTournamentUpdateModelListener {

        void onSuccessGrpTournamentUpdate();

        void onFailedGrpTournamentUpdate(String message);

        void onNoInternet();
    }
}
