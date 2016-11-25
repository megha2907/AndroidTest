package in.sportscafe.nostragamus.webservice;

import in.sportscafe.nostragamus.Constants;
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