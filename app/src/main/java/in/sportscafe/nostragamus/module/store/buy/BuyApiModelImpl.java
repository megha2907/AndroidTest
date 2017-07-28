package in.sportscafe.nostragamus.module.store.buy;

import android.support.annotation.NonNull;

import com.jeeva.android.Log;

import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by sandip on 27/07/17.
 */

public class BuyApiModelImpl {

    private static final String TAG = BuyApiModelImpl.class.getSimpleName();
    private BuyApiModelImpl.BuyApiListener mListener;

    private BuyApiModelImpl(BuyApiModelImpl.BuyApiListener listener) {
        this.mListener = listener;
    }

    public static BuyApiModelImpl newInstance(@NonNull BuyApiModelImpl.BuyApiListener listener) {
        return new BuyApiModelImpl(listener);
    }

    public void performApiCall(Integer productId) {

        if (Nostragamus.getInstance().hasNetworkConnection()) {

            BuyRequest request = new BuyRequest();
            request.setProductId(productId);

            MyWebService.getInstance().buyFromStore(request).enqueue(new NostragamusCallBack<BuyResponse>() {
                @Override
                public void onResponse(Call<BuyResponse> call, Response<BuyResponse> response) {
                    super.onResponse(call, response);

                    if (response != null && response.isSuccessful() && response.body() != null) {
                        Log.d("Buy", response.body().toString());
                        /*if (mListener != null) {
                            mListener.onSuccessResponse(response.body());
                        }*/

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

    public interface BuyApiListener {
        void noInternet();
        void onApiFailed();
        void onSuccessResponse(BuyResponse buyResponse);
    }

}
