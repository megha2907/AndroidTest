package in.sportscafe.nostragamus.module.user.leaderboardsummary;

import android.content.Context;

import in.sportscafe.nostragamus.module.user.myprofile.myposition.dto.LbSummary;
import in.sportscafe.nostragamus.module.user.myprofile.myposition.dto.LbSummaryResponse;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by deepanshi on 11/7/16.
 */

public class LeaderBoardSummaryModelImpl implements LeaderBoardSummaryModel {


    private OnLeaderBoardSummaryModelListener onLeaderBoardModelListener;


    private LeaderBoardSummaryModelImpl(LeaderBoardSummaryModelImpl.OnLeaderBoardSummaryModelListener listener) {
        onLeaderBoardModelListener=listener;
    }

    public static LeaderBoardSummaryModel newInstance(LeaderBoardSummaryModelImpl.OnLeaderBoardSummaryModelListener listener) {
        return new LeaderBoardSummaryModelImpl(listener);
    }

    @Override
    public void getLeaderBoardSummary(){
        getLbSummary();
    }

    private void getLbSummary() {
        MyWebService.getInstance().getLeaderBoardSummary().enqueue(
                new NostragamusCallBack<LbSummaryResponse>() {
                    @Override
                    public void onResponse(Call<LbSummaryResponse> call, Response<LbSummaryResponse> response) {
                        if(null == onLeaderBoardModelListener.getContext()) {
                            return;
                        }
                        if(response.isSuccessful()) {
                            onLeaderBoardModelListener.onGetLeaderBoardSummarySuccess(response.body().getLbSummary());
                        } else {
                            onLeaderBoardModelListener.onGetLeaderBoardSummaryFailed(response.message());
                        }
                    }
                }
        );
    }

    public interface OnLeaderBoardSummaryModelListener {

        void onGetLeaderBoardSummarySuccess(LbSummary lbSummary);

        void onGetLeaderBoardSummaryFailed(String message);

        void onNoInternet();

        Context getContext();
    }


}
