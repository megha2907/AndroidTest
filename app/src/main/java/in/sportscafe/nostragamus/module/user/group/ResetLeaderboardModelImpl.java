package in.sportscafe.nostragamus.module.user.group;

import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.Alerts;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.common.ApiResponse;
import in.sportscafe.nostragamus.module.user.group.members.MembersRequest;
import in.sportscafe.nostragamus.module.user.myprofile.dto.GroupPerson;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Jeeva on 14/6/16.
 */
public class ResetLeaderboardModelImpl {

    private OnResetLeaderboardModelListener mResetLeaderboardModelListener;

    public ResetLeaderboardModelImpl(OnResetLeaderboardModelListener listener) {
        this.mResetLeaderboardModelListener = listener;
    }

    public void resetLeaderboard(Integer groupId) {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            callResetLeaderboardApi(groupId);
        } else {
            mResetLeaderboardModelListener.onNoInternet();
        }
    }

    private void callResetLeaderboardApi(Integer groupId) {
        MyWebService.getInstance().getResetLeaderboardRequest(groupId).enqueue(
                new NostragamusCallBack<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        super.onResponse(call, response);
                        if (response.isSuccessful()) {
                            mResetLeaderboardModelListener.onSuccessResetLeaderboard();
                        } else {
                            mResetLeaderboardModelListener.onFailedResetLeaderboard(Alerts.CANNOT_RESET_LB);
                        }
                    }
                }
        );
    }

    public interface OnResetLeaderboardModelListener {

        void onSuccessResetLeaderboard();

        void onFailedResetLeaderboard(String message);

        void onNoInternet();
    }
}