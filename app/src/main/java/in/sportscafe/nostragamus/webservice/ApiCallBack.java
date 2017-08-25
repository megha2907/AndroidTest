package in.sportscafe.nostragamus.webservice;

import in.sportscafe.nostragamus.Nostragamus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sandip on 24/08/17.
 */

public abstract class ApiCallBack<T> implements Callback<T> {

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if(response.code() == 401) {
            Nostragamus.getInstance().logout();
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {

    }
}
