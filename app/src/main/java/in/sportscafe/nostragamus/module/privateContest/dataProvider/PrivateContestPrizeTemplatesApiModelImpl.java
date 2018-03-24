package in.sportscafe.nostragamus.module.privateContest.dataProvider;

import com.jeeva.android.Log;

import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.privateContest.dto.PrivateContestPrizeTemplateResponse;
import in.sportscafe.nostragamus.webservice.ApiCallBack;
import in.sportscafe.nostragamus.webservice.MyWebService;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by sc on 21/3/18.
 */

public class PrivateContestPrizeTemplatesApiModelImpl {

    private static final String TAG = PrivateContestPrizeTemplatesApiModelImpl.class.getSimpleName();

    public void fetchPrizeTemplates(final PrivateContestDetailApiListener listener) {
        if (Nostragamus.getInstance().hasNetworkConnection()) {

            MyWebService.getInstance().getPrivateContestPrizeTemplates()
                    .enqueue(new ApiCallBack<List<PrivateContestPrizeTemplateResponse>>() {
                        @Override
                        public void onResponse(Call<List<PrivateContestPrizeTemplateResponse>> call,
                                               Response<List<PrivateContestPrizeTemplateResponse>> response) {
                            super.onResponse(call, response);

                            if (response.isSuccessful() && response.body() != null) {
                                Log.d(TAG, "Server response success");

                                if (listener != null) {
                                    listener.onSuccessResponse(Constants.DataStatus.FROM_SERVER_API_SUCCESS, response.body());
                                }
                            } else {
                                Log.d(TAG, "Server response null");
                                if (listener != null) {
                                    listener.onError(Constants.DataStatus.FROM_SERVER_API_FAILED);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<List<PrivateContestPrizeTemplateResponse>> call, Throwable t) {
                            super.onFailure(call, t);

                            Log.d(TAG, "Server response Failed");
                            if (listener != null) {
                                listener.onError(Constants.DataStatus.FROM_SERVER_API_FAILED);
                            }
                        }
                    });
        } else {
            if (listener != null) {
                listener.onError(Constants.DataStatus.NO_INTERNET);
            }
        }
    }

    public interface PrivateContestDetailApiListener {
        void onSuccessResponse(int status, List<PrivateContestPrizeTemplateResponse> responseList);
        void onError(int status);
    }
}
