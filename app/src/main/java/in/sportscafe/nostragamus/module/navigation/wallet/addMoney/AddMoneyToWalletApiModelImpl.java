package in.sportscafe.nostragamus.module.navigation.wallet.addMoney;

import android.support.annotation.NonNull;

import com.jeeva.android.Log;

import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.dto.AddMoneyToWalletRequest;
import in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank.dto.GenerateOrderResponse;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by sandip on 08/06/17.
 */

public class AddMoneyToWalletApiModelImpl {

    private static final String TAG = AddMoneyToWalletApiModelImpl.class.getSimpleName();

    private AddMoneyToWalletApiListener mListener;

    private AddMoneyToWalletApiModelImpl(AddMoneyToWalletApiListener listener) {
        this.mListener = listener;
    }

    public static AddMoneyToWalletApiModelImpl newInstance(@NonNull AddMoneyToWalletApiListener listener) {
        return new AddMoneyToWalletApiModelImpl(listener);
    }

    public void callAddMoneyApi(double txnAmount) {

        AddMoneyToWalletRequest request = new AddMoneyToWalletRequest();
        request.setAmount(txnAmount);

        if (Nostragamus.getInstance().hasNetworkConnection()) {
            MyWebService.getInstance().addMoneyToWalletRequest(request).enqueue(new NostragamusCallBack<GenerateOrderResponse>() {
                @Override
                public void onResponse(Call<GenerateOrderResponse> call, Response<GenerateOrderResponse> response) {
                    super.onResponse(call, response);

                    if (response != null && response.isSuccessful() && response.body() != null) {
                        GenerateOrderResponse apiResponse = response.body();

                        if (mListener != null) {
                            mListener.onSuccessResponse(apiResponse);
                        }
                    } else {
                        Log.d(TAG, "Api response empty!");
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

    public interface AddMoneyToWalletApiListener {
        void noInternet();
        void onNoApiResponse();
        void onSuccessResponse(GenerateOrderResponse response);
    }

}
