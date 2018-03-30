package in.sportscafe.nostragamus.webservice;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.jeeva.android.Log;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.ResponseBody;
import okio.BufferedSource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Jeeva on 14/7/16.
 */
public abstract class NostragamusCallBack<T> implements Callback<T> {

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        // checking if it not authorized
        if (response.code() == 401) {
            Nostragamus.getInstance().logout();
        }

        try {
            if (response.isSuccessful() && response.body() != null && response.body() != null && call != null) {
                Crashlytics.log("Api Call:-->" + String.valueOf(call.request().url()) +
                        "\nTimeStamp:-->" + Nostragamus.getInstance().getServerTime()
                        + "\nResponse:-->" + new Gson().toJson(response.body()));
            }
        } catch (Exception e) {
             Log.e("Error","Something went wrong with Crashlytics log");
        }

    }

    @Override
    public final void onFailure(Call<T> call, Throwable t) {
        onResponse(call, (Response<T>) Response.error(getEmptyResponseBody(), new okhttp3.Response.Builder()
                .code(0)
                .message(Constants.Alerts.NO_NETWORK_CONNECTION)
                .protocol(Protocol.HTTP_1_1)
                .request(new Request.Builder().url("http://localhost/").build())
                .build()));
    }

    private ResponseBody getEmptyResponseBody() {
        return new ResponseBody() {
            @Override
            public MediaType contentType() {
                return null;
            }

            @Override
            public long contentLength() {
                return 0;
            }

            @Override
            public BufferedSource source() {
                return null;
            }
        };
    }
}