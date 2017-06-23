package in.sportscafe.nostragamus.module.navigation.wallet.withdrawMoney;

import android.support.annotation.NonNull;

import com.jeeva.android.Log;

import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.navigation.wallet.withdrawMoney.dto.WithdrawFromWalletRequest;
import in.sportscafe.nostragamus.module.navigation.wallet.withdrawMoney.dto.WithdrawFromWalletResponse;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by sandip on 13/06/17.
 */

public class WithdrawFromWalletApiModelImpl {

    private static final String TAG = WithdrawFromWalletApiModelImpl.class.getSimpleName();

    private WithdrawMoneyFromWalletApiListener mListener;

    private WithdrawFromWalletApiModelImpl(WithdrawMoneyFromWalletApiListener listener) {
        this.mListener = listener;
    }

    public static WithdrawFromWalletApiModelImpl newInstance(@NonNull WithdrawMoneyFromWalletApiListener listener) {
        return new WithdrawFromWalletApiModelImpl(listener);
    }

    public void callWithdrawMoneyApi(double amount, String payoutType) {

        final WithdrawFromWalletRequest request = new WithdrawFromWalletRequest();
        request.setAmount(amount);
        request.setPayoutChoice(payoutType);

        if (Nostragamus.getInstance().hasNetworkConnection()) {
            MyWebService.getInstance().withdrawFromWallet(request).enqueue(new NostragamusCallBack<WithdrawFromWalletResponse>() {
                @Override
                public void onResponse(Call<WithdrawFromWalletResponse> call, Response<WithdrawFromWalletResponse> response) {
                    super.onResponse(call, response);

                    if (response != null && response.body() != null && response.isSuccessful()) {
                        WithdrawFromWalletResponse walletResponse = response.body();
                        if (mListener != null) {
                            mListener.onSuccessResponse(walletResponse);
                        }
                    } else {
                        Log.d(TAG, "Api Failed!");
                        if (mListener != null) {
                            mListener.onNoApiResponse();
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

    public interface WithdrawMoneyFromWalletApiListener {
        void noInternet();
        void onNoApiResponse();
        void onSuccessResponse(WithdrawFromWalletResponse walletResponse);
    }
}
