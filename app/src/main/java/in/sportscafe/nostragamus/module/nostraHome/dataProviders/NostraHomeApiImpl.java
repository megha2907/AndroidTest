package in.sportscafe.nostragamus.module.nostraHome.dataProviders;

import android.text.TextUtils;

import com.jeeva.android.Log;

import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.nostraHome.dto.TimeResponse;
import in.sportscafe.nostragamus.webservice.ApiCallBack;
import in.sportscafe.nostragamus.webservice.MyWebService;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by sandip on 04/10/17.
 */

public class NostraHomeApiImpl {

    private static final String TAG = NostraHomeApiImpl.class.getSimpleName();

    public NostraHomeApiImpl() {
    }

    public void fetchServerTimeFromServer(final NostraHomeApiListener listener) {
        if (Nostragamus.getInstance().hasNetworkConnection()) {

            MyWebService.getInstance().getServerTime().enqueue(
                    new ApiCallBack<TimeResponse>() {
                        @Override
                        public void onResponse(Call<TimeResponse> call, Response<TimeResponse> response) {
                            super.onResponse(call, response);

                            if (response.isSuccessful() && response.body() != null && response.body().getServerTime() != null) {
                                String serverTime = response.body().getServerTime();
                                setServerTimeForGloballyAvailability(serverTime, listener);

                            } else {
                                Log.d(TAG, "Timer response null/empty");
                                if (listener != null) {
                                    listener.onTimerFailure();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<TimeResponse> call, Throwable t) {
                            super.onFailure(call, t);
                            Log.d(TAG, "Timer response Failed");
                            if (listener != null) {
                                listener.onTimerFailure();
                            }
                        }
                    }
            );
        } else {
            if (listener != null) {
                listener.noInternet();
            }
        }
    }

    private void setServerTimeForGloballyAvailability(String serverTime, NostraHomeApiListener listener) {
        if (!TextUtils.isEmpty(serverTime)) {
            try {
                long time = Long.parseLong(serverTime);
                Nostragamus.getInstance().setServerTime(time);
                if (listener != null) {
                    listener.onTimerSuccess();
                    return;
                }
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
        }

        if (listener != null) {
            listener.onTimerFailure();
            return;
        }
    }

    public interface NostraHomeApiListener {
        void noInternet();
        void onTimerFailure();
        void onTimerSuccess();
    }

}
