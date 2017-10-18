package in.sportscafe.nostragamus.webservice;

import in.sportscafe.nostragamus.Nostragamus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sandip on 24/08/17.
 */

public abstract class ApiCallBack<T> implements Callback<T> {

    public interface ResponseCodes {
        int CODE_400 = 400;     // Server returned Error
        int CODE_401 = 401;     // UnAuthorized
        int CODE_200 = 200;     // Response Success
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        switch (response.code()) {
            case ResponseCodes.CODE_401:
                Nostragamus.getInstance().logout();
                break;

            default:
                break;
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {

    }
}
