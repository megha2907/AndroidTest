package in.sportscafe.nostragamus.module.navigation.wallet.addMoney.addByCashFree;

import android.support.annotation.NonNull;

import com.jeeva.android.Log;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.dto.CashFreeGenerateOrderRequest;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.dto.CashFreeGenerateOrderResponse;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by deepanshi on 2/23/18.
 */

public class GenerateCashFreeOrderApiModelImpl {

    private static final String TAG = GenerateCashFreeOrderApiModelImpl.class.getSimpleName();

    private GenerateCashFreeOrderApiModelImpl.GenerateCashFreeOrderApiListener mListener;

    private GenerateCashFreeOrderApiModelImpl(GenerateCashFreeOrderApiModelImpl.GenerateCashFreeOrderApiListener listener) {
        this.mListener = listener;
    }

    public static GenerateCashFreeOrderApiModelImpl newInstance(@NonNull GenerateCashFreeOrderApiModelImpl.GenerateCashFreeOrderApiListener listener) {
        return new GenerateCashFreeOrderApiModelImpl(listener);
    }

    public void callGenerateOrderApi(double txnAmount) {

        CashFreeGenerateOrderRequest request = new CashFreeGenerateOrderRequest();
        request.setAmount(txnAmount);
        request.setTransactionType(Constants.AddMoneyPaymentModes.CASHFREE);

        if (Nostragamus.getInstance().hasNetworkConnection()) {
            MyWebService.getInstance().generateCashFreeOrderRequest(request).enqueue(new NostragamusCallBack<CashFreeGenerateOrderResponse>() {
                @Override
                public void onResponse(Call<CashFreeGenerateOrderResponse> call, Response<CashFreeGenerateOrderResponse> response) {
                    super.onResponse(call, response);

                    if (response != null && response.isSuccessful() && response.body() != null) {
                        CashFreeGenerateOrderResponse apiResponse = response.body();

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

    public interface GenerateCashFreeOrderApiListener {
        void noInternet();
        void onNoApiResponse();
        void onSuccessResponse(CashFreeGenerateOrderResponse response);
    }
}
