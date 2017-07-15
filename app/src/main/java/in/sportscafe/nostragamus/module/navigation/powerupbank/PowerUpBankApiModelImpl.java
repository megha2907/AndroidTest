package in.sportscafe.nostragamus.module.navigation.powerupbank;

import android.support.annotation.NonNull;

import com.jeeva.android.Log;

import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import in.sportscafe.nostragamus.webservice.UserReferralResponse;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by deepanshi on 7/12/17.
 */

public class PowerUpBankApiModelImpl {

    private static final String TAG = PowerUpBankApiModelImpl.class.getSimpleName();
    private PowerUpBankApiModelImpl.PowerUpBankApiListener mListener;

    private PowerUpBankApiModelImpl(PowerUpBankApiModelImpl.PowerUpBankApiListener listener) {
        this.mListener = listener;
    }

    public static PowerUpBankApiModelImpl newInstance(@NonNull PowerUpBankApiModelImpl.PowerUpBankApiListener listener) {
        return new PowerUpBankApiModelImpl(listener);
    }

    public void performApiCall(String flavor) {

        if (Nostragamus.getInstance().hasNetworkConnection()) {
            MyWebService.getInstance().getUserReferralInfo(flavor).enqueue(new NostragamusCallBack<UserReferralResponse>() {
                @Override
                public void onResponse(Call<UserReferralResponse> call, Response<UserReferralResponse> response) {
                    super.onResponse(call, response);

                    if (response != null && response.isSuccessful() && response.body() != null) {
                        Log.d(TAG, "Response : " + response.body());
                        UserReferralResponse userReferralResponse = response.body();

                        if (mListener != null) {
                            if (userReferralResponse!=null) {
                                mListener.onSuccessResponse(userReferralResponse);
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

    public interface PowerUpBankApiListener {
        void noInternet();
        void onApiFailed();
        void onSuccessResponse(UserReferralResponse userReferralResponse);
    }

}
