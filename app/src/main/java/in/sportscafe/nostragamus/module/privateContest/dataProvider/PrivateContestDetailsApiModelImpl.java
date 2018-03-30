package in.sportscafe.nostragamus.module.privateContest.dataProvider;

import com.google.gson.Gson;
import com.jeeva.android.Log;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.common.ErrorResponse;
import in.sportscafe.nostragamus.module.privateContest.ui.createContest.dto.CreatePrivateContestResponse;
import in.sportscafe.nostragamus.module.privateContest.ui.joinPrivateContest.dto.FindPrivateContestResponse;
import in.sportscafe.nostragamus.webservice.ApiCallBack;
import in.sportscafe.nostragamus.webservice.MyWebService;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by sc on 27/3/18.
 */

public class PrivateContestDetailsApiModelImpl {

    private final String TAG = PrivateContestDetailsApiModelImpl.class.getSimpleName();

    public void fetchPrivateContestData(String privateCode, final PrivateContestDetailApiListener apiListener) {

        if (Nostragamus.getInstance().hasNetworkConnection()) {

            MyWebService.getInstance().getPrivateContestDetails(privateCode)
                    .enqueue(new ApiCallBack<FindPrivateContestResponse>() {
                        @Override
                        public void onResponse(Call<FindPrivateContestResponse> call,
                                               Response<FindPrivateContestResponse> response) {
                            super.onResponse(call, response);

                            if (response.code() == 400 && response.errorBody() != null) {
                                Log.d(TAG, "Response code 400");
                                handleErrorCode400(response, apiListener);

                            } else {

                                if (response.isSuccessful() && response.body() != null) {
                                    Log.d(TAG, "Server response success");
                                    if (apiListener != null) {
                                        apiListener.onSuccessResponse(Constants.DataStatus.FROM_SERVER_API_SUCCESS, response.body());
                                    }
                                } else {
                                    Log.d(TAG, "Server response null");
                                    if (apiListener != null) {
                                        apiListener.onError(Constants.DataStatus.FROM_SERVER_API_FAILED);
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<FindPrivateContestResponse> call, Throwable t) {
                            super.onFailure(call, t);

                            Log.d(TAG, "Server response Failed");
                            if (apiListener != null) {
                                apiListener.onError(Constants.DataStatus.FROM_SERVER_API_FAILED);
                            }
                        }
                    });

        } else {
            if (apiListener != null) {
                apiListener.onError(Constants.DataStatus.NO_INTERNET);
            }
        }

    }


    private void handleErrorCode400(Response<FindPrivateContestResponse> response,
                                    PrivateContestDetailApiListener listener) {
        ErrorResponse errorResponse = null;
        try {
            errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
        } catch (Exception e) {e.printStackTrace();}
        if (listener != null) {
            if (errorResponse != null) {
                listener.onServerSentError(errorResponse.getError());
            } else {
                listener.onError(Constants.DataStatus.FROM_SERVER_API_FAILED);
            }
        }
    }

    public interface PrivateContestDetailApiListener {
        void onSuccessResponse(int status, FindPrivateContestResponse response);
        void onError(int status);
        void onServerSentError(String errorMsg);
    }
}
