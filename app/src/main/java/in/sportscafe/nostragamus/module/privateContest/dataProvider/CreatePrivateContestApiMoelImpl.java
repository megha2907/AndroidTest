package in.sportscafe.nostragamus.module.privateContest.dataProvider;

import com.google.gson.Gson;
import com.jeeva.android.Log;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.common.ErrorResponse;
import in.sportscafe.nostragamus.module.privateContest.ui.createContest.dto.CreatePrivateContestRequest;
import in.sportscafe.nostragamus.module.privateContest.ui.createContest.dto.CreatePrivateContestResponse;
import in.sportscafe.nostragamus.module.privateContest.helper.JoinPrivateContestHelper;
import in.sportscafe.nostragamus.webservice.ApiCallBack;
import in.sportscafe.nostragamus.webservice.MyWebService;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by sc on 21/3/18.
 */

public class CreatePrivateContestApiMoelImpl {

    private static final String TAG = CreatePrivateContestApiMoelImpl.class.getSimpleName();

    public void callCreateContestApi(int challengeId, double fee, String cofigName,
                                     int minParticipants, int maxParticipants, String step,
            final JoinPrivateContestHelper.JoinPrivateContestProcessListener listener) {

        CreatePrivateContestRequest request = new CreatePrivateContestRequest();
        request.setChallengeId(challengeId);
        request.setFee(fee);
        request.setConfigName(cofigName);
        request.setMinParticipants(minParticipants);
        request.setMaxParticipants(maxParticipants);
        request.setStep(step);

        if (Nostragamus.getInstance().hasNetworkConnection()) {

            MyWebService.getInstance().createPrivateContest(request)
                    .enqueue(new ApiCallBack<CreatePrivateContestResponse>() {
                @Override
                public void onResponse(Call<CreatePrivateContestResponse> call,
                                       Response<CreatePrivateContestResponse> response) {
                    super.onResponse(call, response);

                    if (response.code() == 400 && response.errorBody() != null) {
                        Log.d(TAG, "Response code 400");
                        handleErrorCode400(response, listener);

                    } else {

                        if (response.isSuccessful() && response.body() != null) {
                            Log.d(TAG, "Server response success");
                            if (listener != null) {
                                listener.joinPrivateContestSuccess(Constants.DataStatus.FROM_SERVER_API_SUCCESS, response.body());
                            }
                        } else {
                            Log.d(TAG, "Server response null");
                            if (listener != null) {
                                listener.onApiFailure();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<CreatePrivateContestResponse> call, Throwable t) {
                    super.onFailure(call, t);

                    Log.d(TAG, "Server response Failed");
                    if (listener != null) {
                        listener.onApiFailure();
                    }
                }
            });

        } else {
            if (listener != null) {
                listener.noInternet();
            }
        }

    }

    private void handleErrorCode400(Response<CreatePrivateContestResponse> response,
                                    JoinPrivateContestHelper.JoinPrivateContestProcessListener listener) {
        ErrorResponse errorResponse = null;
        try {
            errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
        } catch (Exception e) {e.printStackTrace();}
        if (listener != null) {
            if (errorResponse != null) {
                listener.onServerReturnedError(errorResponse.getError());
            } else {
                listener.onApiFailure();
            }
        }
    }

    public interface CreatePrivateContestApiListener {
        void onSuccessResponse(int status, CreatePrivateContestResponse response);
        void onError(int status);
        void onServerSentError(String errorMsg);
    }

}
