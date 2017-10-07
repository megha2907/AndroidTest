package in.sportscafe.nostragamus.module.challengeRewards;

import android.content.Context;
import android.support.annotation.Nullable;

import com.jeeva.android.Log;

import java.util.List;

import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.challengeRewards.dto.Rewards;
import in.sportscafe.nostragamus.module.challengeRewards.dto.RewardsRequest;
import in.sportscafe.nostragamus.module.challengeRewards.dto.RewardsResponse;
import in.sportscafe.nostragamus.webservice.ApiCallBack;
import in.sportscafe.nostragamus.webservice.MyWebService;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by deepanshi on 9/6/17.
 */

public class RewardsApiModelImpl {

    private static final String TAG = RewardsApiModelImpl.class.getSimpleName();

    public RewardsApiModelImpl() {
    }

    public void getRewardsData(int roomId, int configId, RewardsDataListener listener) {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            loadRewardsData(roomId, configId ,listener);
        } else {
            Log.d(TAG, "No Network connection");
            listener.onNoInternet();
        }
    }

    private void loadRewardsData(int roomId, int configId, final RewardsDataListener listener) {

        /* Before Joining Room send Config Id , After Joining Room send roomId */
        Integer finalRoomId;
        if (roomId == -1) {
            finalRoomId = null;
        }else {
            finalRoomId = roomId;
        }

        Integer finalConfigId;
        if (configId == -1) {
            finalConfigId = null;
        }else {
            finalConfigId = configId;
        }

        MyWebService.getInstance().getRewardsDetails(finalRoomId,finalConfigId).enqueue(new ApiCallBack<RewardsResponse>() {
            @Override
            public void onResponse(Call<RewardsResponse> call, Response<RewardsResponse> response) {
                super.onResponse(call, response);

                if (response.isSuccessful() && response.body() != null && response.body() != null) {
                    Log.d(TAG, "Server response success");
                    handleRewardsListResponse(response.body().getRewardsList(),
                            response.body().getChallengeEndTime(),listener);
                } else {
                    Log.d(TAG, "Server response null");
                    listener.onFailedConfigsApi();
                }
            }

            @Override
            public void onFailure(Call<RewardsResponse> call, Throwable t) {
                super.onFailure(call, t);
                Log.d(TAG, "Server response Failed");
                listener.onFailedConfigsApi();
            }
        });
    }

    private void handleRewardsListResponse(List<Rewards> rewardsList,String challengeEndTime,
                                           final RewardsApiModelImpl.RewardsDataListener listener) {
        if (null == rewardsList || rewardsList.isEmpty()) {
            listener.onEmpty();
            return;
        }

        listener.onData(rewardsList,challengeEndTime);
    }

    public interface RewardsDataListener {

        void onData(@Nullable List<Rewards> rewardsList,String challengeEndTime);
        void onError(int status);
        void onNoInternet();
        void onFailedConfigsApi();
        void onEmpty();
    }
}
