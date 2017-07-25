package in.sportscafe.nostragamus.module.user.myprofile.verify;

import android.support.annotation.NonNull;

import com.jeeva.android.Log;

import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.common.ApiResponse;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import in.sportscafe.nostragamus.webservice.VerifyOTPResponse;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by deepanshi on 7/19/17.
 */

public class VerifyOTPApiModelImpl {

    private static final String TAG = VerifyOTPApiModelImpl.class.getSimpleName();
    private VerifyOTPApiModelImpl.VerifyOTPApiListener mListener;

    private VerifyOTPApiModelImpl(VerifyOTPApiModelImpl.VerifyOTPApiListener listener) {
        this.mListener = listener;
    }

    public static VerifyOTPApiModelImpl newInstance(@NonNull VerifyOTPApiModelImpl.VerifyOTPApiListener listener) {
        return new VerifyOTPApiModelImpl(listener);
    }

    public void performApiCall(String otp) {

        if (Nostragamus.getInstance().hasNetworkConnection()) {
            MyWebService.getInstance().verifyOTPRequest(otp).enqueue(new NostragamusCallBack<VerifyOTPResponse>() {
                @Override
                public void onResponse(Call<VerifyOTPResponse> call, Response<VerifyOTPResponse> response) {
                    super.onResponse(call, response);

                    if (response != null && response.isSuccessful() && response.body() != null) {
                        Log.d(TAG, "Response : " + response.body());
                        if (mListener != null) {
                            if (response.body()!=null) {

                                VerifyOTPInfo verifyOTPInfo = null;
                                if (response.body().getVerifyOTPInfo()!=null) {
                                    verifyOTPInfo = response.body().getVerifyOTPInfo();
                                }

                                mListener.onSuccessResponse(verifyOTPInfo);
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

    public interface VerifyOTPApiListener {
        void noInternet();
        void onApiFailed();
        void onSuccessResponse(VerifyOTPInfo verifyOTPInfo);
    }

}
