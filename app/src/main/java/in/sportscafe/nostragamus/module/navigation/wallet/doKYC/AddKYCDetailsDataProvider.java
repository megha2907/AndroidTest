package in.sportscafe.nostragamus.module.navigation.wallet.doKYC;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.jeeva.android.Log;

import java.io.File;

import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.common.ApiResponse;
import in.sportscafe.nostragamus.module.common.ErrorResponse;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by deepanshi on 3/27/18.
 */

public class AddKYCDetailsDataProvider {

    private static final String TAG = AddKYCDetailsDataProvider.class.getSimpleName();

    public AddKYCDetailsDataProvider() {
    }

    public void saveUserPANDetails(String userName, String panNumber, String dob, File file, AddKYCDetailsDataProvider.AddKYCDetailsDataProviderModelListener addKYCDetailsDataProviderModelListener) {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            MyWebService.getInstance().addUserPANCardDetails(userName, panNumber, dob, file).enqueue(getUserPANDetailsSavedCallBack(addKYCDetailsDataProviderModelListener));
        } else {
            addKYCDetailsDataProviderModelListener.onNoInternet();
        }
    }

    @NonNull
    private NostragamusCallBack<ApiResponse> getUserPANDetailsSavedCallBack(final AddKYCDetailsDataProviderModelListener addKYCDetailsDataProviderModelListener) {
        return new NostragamusCallBack<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                super.onResponse(call, response);

                if (response.code() == 400 && response.errorBody() != null) {
                    Log.d(TAG, "Response code 400");
                    ErrorResponse errorResponse = null;

                    try {
                        errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (addKYCDetailsDataProviderModelListener != null) {
                        if (errorResponse != null) {
                            addKYCDetailsDataProviderModelListener.onServerSentError(errorResponse.getError());
                        } else {
                            addKYCDetailsDataProviderModelListener.onAddDetailFailed();
                        }
                    }

                } else {
                    if (response.isSuccessful() && response.body() != null) {
                        addKYCDetailsDataProviderModelListener.onAddDetailSuccess();
                    } else {
                        addKYCDetailsDataProviderModelListener.onAddDetailFailed();
                    }
                }
            }
        };
    }


    public interface AddKYCDetailsDataProviderModelListener {

        void onAddDetailSuccess();

        void onNoInternet();

        void onAddDetailFailed();

        void onServerSentError(String errorMsg);
    }

}