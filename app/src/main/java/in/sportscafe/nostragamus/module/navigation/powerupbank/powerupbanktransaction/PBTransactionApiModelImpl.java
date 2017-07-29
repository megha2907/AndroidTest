package in.sportscafe.nostragamus.module.navigation.powerupbank.powerupbanktransaction;

import android.support.annotation.NonNull;

import com.jeeva.android.Log;

import java.util.List;

import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.navigation.referfriends.referralcredits.ReferralHistory;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import in.sportscafe.nostragamus.webservice.UserReferralHistoryResponse;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by deepanshi on 7/13/17.
 */

public class PBTransactionApiModelImpl {

    private static final String TAG = PBTransactionApiModelImpl.class.getSimpleName();
    private PBTransactionApiModelImpl.PBTransactionHistoryApiListener mListener;

    private PBTransactionApiModelImpl(PBTransactionApiModelImpl.PBTransactionHistoryApiListener listener) {
        this.mListener = listener;
    }

    public static PBTransactionApiModelImpl newInstance(@NonNull PBTransactionApiModelImpl.PBTransactionHistoryApiListener listener) {
        return new PBTransactionApiModelImpl(listener);
    }

    public void performApiCall(String flavor) {

        if (Nostragamus.getInstance().hasNetworkConnection()) {

            MyWebService.getInstance().getPBTransactionHistory(flavor,"powerups").enqueue(new NostragamusCallBack<UserReferralHistoryResponse>() {
                @Override
                public void onResponse(Call<UserReferralHistoryResponse> call, Response<UserReferralHistoryResponse> response) {
                    super.onResponse(call, response);

                    if (response != null && response.isSuccessful() && response.body() != null) {
                        Log.d(TAG, "Response : " + response.body());
                        UserReferralHistoryResponse userReferralHistoryResponse = response.body();

                        if (mListener != null) {
                            mListener.onSuccessResponse(userReferralHistoryResponse.getReferralHistoryList());
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

    public interface PBTransactionHistoryApiListener {
        void noInternet();
        void onApiFailed();
        void onSuccessResponse(List<ReferralHistory> referralHistoryList);
    }
}
