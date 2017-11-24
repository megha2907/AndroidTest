package in.sportscafe.nostragamus.module.common;

import android.support.annotation.NonNull;
import com.jeeva.android.Log;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.webservice.ApiCallBack;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.SubmitReportRequest;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by deepanshi on 11/16/17.
 */

public class NostraFeedbackDataProvider {

    private static final String TAG = NostraFeedbackDataProvider.class.getSimpleName();

    public NostraFeedbackDataProvider() {
    }

    public void sendReport(String type, String id, @NonNull NostraFeedbackDataProvider.NostraFeedbackDataProviderListener listener) {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            callSendReport(type, id, listener);
        } else {
            if (listener != null) {
                listener.onError(Constants.DataStatus.NO_INTERNET);
            }
        }
    }

    private void callSendReport(String type, String id, final NostraFeedbackDataProvider.NostraFeedbackDataProviderListener listener) {

        SubmitReportRequest request = new SubmitReportRequest();
        request.setId(id);
        request.setType(type);

        MyWebService.getInstance().sendErrorReport(request).enqueue(new ApiCallBack<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                super.onResponse(call, response);

                if (response.isSuccessful() && response.body() != null && response.body() != null) {
                    Log.d(TAG, "Server response success");
                    if (listener != null) {
                        listener.onSuccessResponse(Constants.DataStatus.FROM_SERVER_API_SUCCESS);
                    }
                } else {
                    Log.d(TAG, "Server response null");
                    if (listener != null) {
                        listener.onError(Constants.DataStatus.FROM_SERVER_API_FAILED);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                super.onFailure(call, t);
                Log.d(TAG, "Server response Failed");
                if (listener != null) {
                    listener.onError(Constants.DataStatus.FROM_SERVER_API_FAILED);
                }
            }
        });

    }


    public interface NostraFeedbackDataProviderListener {
        void onSuccessResponse(int status);
        void onError(int status);
    }
}
