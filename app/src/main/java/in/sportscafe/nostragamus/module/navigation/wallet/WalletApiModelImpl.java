package in.sportscafe.nostragamus.module.navigation.wallet;

import android.support.annotation.NonNull;

import com.jeeva.android.Log;

import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.common.ApiResponse;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by sandip on 08/06/17.
 */

public class WalletApiModelImpl {

    private static final String TAG = WalletApiModelImpl.class.getSimpleName();
    private WalletApiListener mListener;

    private WalletApiModelImpl(WalletApiListener listener) {
        this.mListener = listener;
    }

    public static WalletApiModelImpl newInstance(@NonNull WalletApiListener listener) {
        return new WalletApiModelImpl(listener);
    }

    public void performApiCall() {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            MyWebService.getInstance().getUserWallet().enqueue(new NostragamusCallBack<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    super.onResponse(call, response);

                    if (response != null && response.isSuccessful() && response.body() != null) {
                        Log.d(TAG, "Response : " + response.body());

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

    public interface WalletApiListener {
        void noInternet();
        void onApiFailed();
        void onSuccessResponse();
    }

}
