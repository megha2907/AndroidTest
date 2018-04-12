package in.sportscafe.nostragamus.module.navigation.wallet.doKYC;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.Nullable;

import com.jeeva.android.Log;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.navigation.wallet.doKYC.dto.KYCDetails;
import in.sportscafe.nostragamus.module.navigation.wallet.doKYC.dto.KYCResponse;
import in.sportscafe.nostragamus.webservice.ApiCallBack;
import in.sportscafe.nostragamus.webservice.MyWebService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by deepanshi on 3/30/18.
 */

public class GetKYCDetailsDataProvider {

    private static final String TAG = GetKYCDetailsDataProvider.class.getSimpleName();

    public GetKYCDetailsDataProvider() {
    }

    public void getKYCDetails(Context appContext, GetKYCDetailsDataProvider.GetKYCDetailsDataProviderListener listener) {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            loadKYCDetailsFromServer(appContext, listener);
            loadPANImageFromServer(appContext, listener);
        } else {
            listener.onNoInternet();
        }
    }

    private void loadKYCDetailsFromServer(final Context appContext, final GetKYCDetailsDataProvider.GetKYCDetailsDataProviderListener listener) {

        MyWebService.getInstance().getKYCUserDetails().enqueue(new ApiCallBack<KYCResponse>() {
            @Override
            public void onResponse(Call<KYCResponse> call, Response<KYCResponse> response) {
                super.onResponse(call, response);

                if (response.isSuccessful() && response.body() != null && response.body() != null) {
                    Log.d(TAG, "Server response success");
                    if (listener != null) {
                        listener.onData(Constants.DataStatus.FROM_SERVER_API_SUCCESS, response.body().getKycDetails());
                    }
                } else {
                    Log.d(TAG, "Server response null");
                }
            }

            @Override
            public void onFailure(Call<KYCResponse> call, Throwable t) {
                super.onFailure(call, t);
                Log.d(TAG, "Server response Failed");
            }
        });
    }

    private void loadPANImageFromServer(Context appContext, final GetKYCDetailsDataProviderListener listener) {

        MyWebService.getInstance().getKYCPANImage().enqueue(new ApiCallBack<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                super.onResponse(call, response);

                if (response.isSuccessful() && response.body() != null && response.body() != null) {
                    Log.d(TAG, "Server response success");
                    if (listener != null) {
                        listener.onImageData(Constants.DataStatus.FROM_SERVER_API_SUCCESS, response.body());
                    } else {
                        listener.onImageFailed();
                    }
                } else {
                    Log.d(TAG, "Server response null");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                super.onFailure(call, t);
                Log.d(TAG, "Server response Failed");
                if (listener != null) {
                  listener.onImageFailed();
                }
            }
        });
    }


    public interface GetKYCDetailsDataProviderListener {
        void onData(int status, @Nullable KYCDetails kycDetails);

        void onError(int status);

        void onImageData(int status, @Nullable ResponseBody body);

        void onImageFailed();

        void onNoInternet();
    }
}
