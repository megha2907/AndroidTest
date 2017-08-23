package in.sportscafe.nostragamus.module.upgradeToPro;

import android.support.annotation.NonNull;

import com.jeeva.android.Log;

import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.dto.AddMoneyToWalletRequest;
import in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank.dto.GenerateOrderResponse;
import in.sportscafe.nostragamus.module.upgradeToPro.dto.UpgradeToProResponse;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by sandip on 04/08/17.
 */

public class UpgradeToProAppApiModelImpl {

    private static final String TAG = UpgradeToProAppApiModelImpl.class.getSimpleName();

    private UpgradeToProAppApiModelImpl.UpgradeToProAppApiListener mListener;

    private UpgradeToProAppApiModelImpl(UpgradeToProAppApiModelImpl.UpgradeToProAppApiListener listener) {
        this.mListener = listener;
    }

    public static UpgradeToProAppApiModelImpl newInstance
            (@NonNull UpgradeToProAppApiModelImpl.UpgradeToProAppApiListener listener) {
        return new UpgradeToProAppApiModelImpl(listener);
    }

    public void performApiCall() {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            MyWebService.getInstance().shouldShowUpgradeDialog().enqueue(new NostragamusCallBack<UpgradeToProResponse>() {
                @Override
                public void onResponse(Call<UpgradeToProResponse> call, Response<UpgradeToProResponse> response) {
                    super.onResponse(call, response);

                    if (response != null && response.body() != null && response.isSuccessful()) {
                        UpgradeToProResponse upgradeToProResponse = response.body();
                        if (mListener != null) {
                            mListener.onSuccessResponse(upgradeToProResponse);
                        }
                    } else {
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

    public interface UpgradeToProAppApiListener {
        void noInternet();
        void onNoApiResponse();
        void onSuccessResponse(UpgradeToProResponse response);
    }
}
