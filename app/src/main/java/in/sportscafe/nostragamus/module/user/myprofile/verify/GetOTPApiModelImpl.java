package in.sportscafe.nostragamus.module.user.myprofile.verify;

import android.support.annotation.NonNull;

import com.jeeva.android.Log;

import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.common.ApiResponse;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by deepanshi on 7/19/17.
 */

public class GetOTPApiModelImpl {

    private static final String TAG = GetOTPApiModelImpl.class.getSimpleName();
    private GetOTPApiModelImpl.GetOTPApiListener mListener;

    private GetOTPApiModelImpl(GetOTPApiModelImpl.GetOTPApiListener listener) {
        this.mListener = listener;
    }

    public static GetOTPApiModelImpl newInstance(@NonNull GetOTPApiModelImpl.GetOTPApiListener listener) {
        return new GetOTPApiModelImpl(listener);
    }

    public void performApiCall(String phoneNumber) {

        if (Nostragamus.getInstance().hasNetworkConnection()) {
            MyWebService.getInstance().getOTPRequest(phoneNumber).enqueue(new NostragamusCallBack<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    super.onResponse(call, response);

                    if (response != null && response.isSuccessful() && response.body() != null) {
                        Log.d(TAG, "Response : " + response.body());
                        if (mListener != null) {
                            if (response.body()!=null) {
                                mListener.onSuccessResponse();
                            }
                        }

                    } else {
                        Log.d(TAG, "Api response not proper/null");
                        if (mListener != null) {
                            mListener.onApiFailed();
                        }
                    }
                }
            });
        } else {
            if (mListener != null) {
                mListener.noInternet();
            }
        }
    }

    public interface GetOTPApiListener {
        void noInternet();
        void onApiFailed();
        void onSuccessResponse();
    }

}