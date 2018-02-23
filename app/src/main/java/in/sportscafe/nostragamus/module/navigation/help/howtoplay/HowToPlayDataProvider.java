package in.sportscafe.nostragamus.module.navigation.help.howtoplay;

import android.content.Context;
import android.support.annotation.Nullable;

import com.jeeva.android.Log;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.navigation.help.howtoplay.dto.HowToPlayDetails;
import in.sportscafe.nostragamus.module.navigation.help.howtoplay.dto.HowToPlayResponse;
import in.sportscafe.nostragamus.webservice.ApiCallBack;
import in.sportscafe.nostragamus.webservice.MyWebService;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by deepanshi on 2/21/18.
 */

public class HowToPlayDataProvider {

    private static final String TAG = HowToPlayDataProvider.class.getSimpleName();

    public HowToPlayDataProvider() {
    }

    public void getHowToPlayData(Context appContext, HowToPlayDataProvider.HowToPlayDataProviderListener listener) {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            loadHowToPlayDataFromServer(appContext, listener);
        } else {
            Log.d(TAG, "No Network connection");
        }
    }

    private void loadHowToPlayDataFromServer(final Context appContext, final HowToPlayDataProvider.HowToPlayDataProviderListener listener) {

        MyWebService.getInstance().getHowToPlayData().enqueue(new ApiCallBack<HowToPlayResponse>() {
            @Override
            public void onResponse(Call<HowToPlayResponse> call, Response<HowToPlayResponse> response) {
                super.onResponse(call, response);

                if (response.isSuccessful() && response.body() != null && response.body() != null) {
                    Log.d(TAG, "Server response success");
                    if (listener != null) {
                        listener.onData(Constants.DataStatus.FROM_SERVER_API_SUCCESS, response.body().getHowToPlayDetails());
                    }
                } else {
                    Log.d(TAG, "Server response null");
                }
            }

            @Override
            public void onFailure(Call<HowToPlayResponse> call, Throwable t) {
                super.onFailure(call, t);
                Log.d(TAG, "Server response Failed");
            }
        });
    }

    public interface HowToPlayDataProviderListener {
        void onData(int status, @Nullable HowToPlayDetails howToPlayDetails);
        void onError(int status);
    }
}
