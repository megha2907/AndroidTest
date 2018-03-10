package in.sportscafe.nostragamus.module.newChallenges.dataProvider;

import android.content.Context;
import android.support.annotation.Nullable;

import com.jeeva.android.Log;

import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.newChallenges.dto.BannerResponseData;
import in.sportscafe.nostragamus.webservice.ApiCallBack;
import in.sportscafe.nostragamus.webservice.BannerResponse;
import in.sportscafe.nostragamus.webservice.MyWebService;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by deepanshi on 2/1/18.
 */

public class BannerDataProvider {

    private static final String TAG = BannerDataProvider.class.getSimpleName();

    public BannerDataProvider() {
    }

    public void getBanners(Context appContext, BannerDataProvider.BannerDataProviderListener listener) {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            loadBannersFromServer(appContext, listener);
        } else {
            Log.d(TAG, "No Network connection");
        }
    }

    private void loadBannersFromServer(final Context appContext, final BannerDataProvider.BannerDataProviderListener listener) {

        MyWebService.getInstance().getBannerData().enqueue(new ApiCallBack<BannerResponse>() {
            @Override
            public void onResponse(Call<BannerResponse> call, Response<BannerResponse> response) {
                super.onResponse(call, response);

                if (response.isSuccessful() && response.body() != null && response.body() != null) {
                    Log.d(TAG, "Server response success");
                    if (listener != null) {
                        listener.onData(Constants.DataStatus.FROM_SERVER_API_SUCCESS, response.body().getBannerResponseDataList());
                    }
                } else {
                    Log.d(TAG, "Server response null");
                }
            }

            @Override
            public void onFailure(Call<BannerResponse> call, Throwable t) {
                super.onFailure(call, t);
                Log.d(TAG, "Server response Failed");
            }
        });
    }

    public interface BannerDataProviderListener {
        void onData(int status, @Nullable List<BannerResponseData> bannerResponseDataList);
        void onError(int status);
    }
}
