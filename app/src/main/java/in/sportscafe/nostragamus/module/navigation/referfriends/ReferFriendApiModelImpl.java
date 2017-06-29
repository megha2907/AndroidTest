package in.sportscafe.nostragamus.module.navigation.referfriends;

import android.support.annotation.NonNull;

import com.jeeva.android.Log;

import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import in.sportscafe.nostragamus.webservice.UserReferralResponse;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by deepanshi on 6/23/17.
 */

public class ReferFriendApiModelImpl {

    private static final String TAG = ReferFriendApiModelImpl.class.getSimpleName();
    private ReferFriendApiModelImpl.ReferFriendApiListener mListener;

    private ReferFriendApiModelImpl(ReferFriendApiModelImpl.ReferFriendApiListener listener) {
        this.mListener = listener;
    }

    public static ReferFriendApiModelImpl newInstance(@NonNull ReferFriendApiModelImpl.ReferFriendApiListener listener) {
        return new ReferFriendApiModelImpl(listener);
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

    public interface ReferFriendApiListener {
        void noInternet();
        void onApiFailed();
        void onSuccessResponse(UserReferralResponse userReferralResponse);
    }

}