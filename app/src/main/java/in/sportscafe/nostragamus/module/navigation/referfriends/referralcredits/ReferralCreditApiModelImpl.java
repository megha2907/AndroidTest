package in.sportscafe.nostragamus.module.navigation.referfriends.referralcredits;

import android.support.annotation.NonNull;

import com.jeeva.android.Log;

import java.util.List;

import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.allchallenges.dto.ChallengeConfigsResponse;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import in.sportscafe.nostragamus.webservice.UserReferralHistoryResponse;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by deepanshi on 6/23/17.
 */

public class ReferralCreditApiModelImpl {

    private static final String TAG = ReferralCreditApiModelImpl.class.getSimpleName();
    private ReferralCreditApiModelImpl.ReferralCreditApiListener mListener;

    private ReferralCreditApiModelImpl(ReferralCreditApiModelImpl.ReferralCreditApiListener listener) {
        this.mListener = listener;
    }

    public static ReferralCreditApiModelImpl newInstance(@NonNull ReferralCreditApiModelImpl.ReferralCreditApiListener listener) {
        return new ReferralCreditApiModelImpl(listener);
    }

    public void performApiCall(String flavor) {

        if (Nostragamus.getInstance().hasNetworkConnection()) {

            MyWebService.getInstance().getUserReferralHistory(flavor).enqueue(new NostragamusCallBack<UserReferralHistoryResponse>() {
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

    public interface ReferralCreditApiListener {
        void noInternet();
        void onApiFailed();
        void onSuccessResponse(List<ReferralHistory> referralHistoryList);
    }

}
