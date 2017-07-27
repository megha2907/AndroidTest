package in.sportscafe.nostragamus.module.store;

import android.support.annotation.NonNull;

import com.jeeva.android.Log;

import java.util.List;

import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.store.dto.StoreApiResponse;
import in.sportscafe.nostragamus.module.store.dto.StoreSections;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import in.sportscafe.nostragamus.webservice.UserReferralHistoryResponse;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by deepanshi on 7/25/17.
 */

public class StoreApiModelImpl {

    private static final String TAG = StoreApiModelImpl.class.getSimpleName();
    private StoreApiModelImpl.StoreApiListener mListener;

    private StoreApiModelImpl(StoreApiModelImpl.StoreApiListener listener) {
        this.mListener = listener;
    }

    public static StoreApiModelImpl newInstance(@NonNull StoreApiModelImpl.StoreApiListener listener) {
        return new StoreApiModelImpl(listener);
    }

    public void performApiCall(String category) {

        if (Nostragamus.getInstance().hasNetworkConnection()) {

            MyWebService.getInstance().getStoreDetails(category).enqueue(new NostragamusCallBack<StoreApiResponse>() {
                @Override
                public void onResponse(Call<StoreApiResponse> call, Response<StoreApiResponse> response) {
                    super.onResponse(call, response);

                    if (response != null && response.isSuccessful() && response.body() != null) {
                        Log.d(TAG, "Response : " + response.body());
                        List<StoreSections> storeSectionsList = response.body().getStoreSections();

                        if (mListener != null) {
                            mListener.onSuccessResponse(storeSectionsList);
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

    public interface StoreApiListener {
        void noInternet();
        void onApiFailed();
        void onSuccessResponse(List<StoreSections> storeSectionsList);
    }

}
