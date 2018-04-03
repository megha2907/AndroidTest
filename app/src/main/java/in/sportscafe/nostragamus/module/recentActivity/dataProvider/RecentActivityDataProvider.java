package in.sportscafe.nostragamus.module.recentActivity.dataProvider;

import android.content.Context;
import android.support.annotation.Nullable;

import com.jeeva.android.Log;

import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.newChallenges.dataProvider.BannerDataProvider;
import in.sportscafe.nostragamus.module.newChallenges.dto.BannerResponseData;
import in.sportscafe.nostragamus.module.recentActivity.dto.RecentActivity;
import in.sportscafe.nostragamus.module.recentActivity.dto.RecentActivityResponse;
import in.sportscafe.nostragamus.webservice.ApiCallBack;
import in.sportscafe.nostragamus.webservice.BannerResponse;
import in.sportscafe.nostragamus.webservice.MyWebService;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by deepanshi on 3/22/18.
 */

public class RecentActivityDataProvider {

    private static final String TAG = RecentActivityDataProvider.class.getSimpleName();

    public RecentActivityDataProvider() {
    }

    public void getRecentActivity(Context appContext, RecentActivityDataProvider.RecentActivityDataProviderListener listener) {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            loadRecentActivityFromServer(appContext, listener);
        } else {
            Log.d(TAG, "No Network connection");
        }
    }

    private void loadRecentActivityFromServer(final Context appContext, final RecentActivityDataProvider.RecentActivityDataProviderListener listener) {

        MyWebService.getInstance().getRecentActivityData().enqueue(new ApiCallBack<RecentActivityResponse>() {
            @Override
            public void onResponse(Call<RecentActivityResponse> call, Response<RecentActivityResponse> response) {
                super.onResponse(call, response);

                if (response.isSuccessful() && response.body() != null && response.body() != null) {
                    Log.d(TAG, "Server response success");
                    if (listener != null) {
                        listener.onData(Constants.DataStatus.FROM_SERVER_API_SUCCESS, response.body().getRecentActivityList());
                    }
                } else {
                    Log.d(TAG, "Server response null");
                    if (listener != null) {
                        listener.onError(Constants.DataStatus.FROM_SERVER_API_FAILED);
                    }
                }
            }

            @Override
            public void onFailure(Call<RecentActivityResponse> call, Throwable t) {
                super.onFailure(call, t);
                Log.d(TAG, "Server response Failed");
                if (listener != null) {
                    listener.onError(Constants.DataStatus.FROM_SERVER_API_FAILED);
                }
            }
        });
    }

    public interface RecentActivityDataProviderListener {
        void onData(int status, @Nullable List<RecentActivity> recentActivityList);
        void onError(int status);
    }
}
