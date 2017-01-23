package in.sportscafe.nostragamus.module.fuzzylbs;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import in.sportscafe.nostragamus.Constants.IntentActions;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.common.Adapter;
import in.sportscafe.nostragamus.module.fuzzysearch.AbstractFuzzySearchModel;
import in.sportscafe.nostragamus.module.fuzzysearch.AbstractFuzzySearchModelImpl;
import in.sportscafe.nostragamus.module.user.login.dto.BasicUserInfo;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by deepanshu on 5/10/16.
 */
public class FuzzyLbModelImpl extends AbstractFuzzySearchModelImpl<BasicUserInfo> {

    private FuzzyLbModelImpl(OnFuzzySearchModelListener listener) {
        super(listener);
    }

    public static AbstractFuzzySearchModel newInstance(OnFuzzySearchModelListener listener) {
        return new FuzzyLbModelImpl(listener);
    }

    @Override
    public Adapter getAdapter(Context context) {
        return new FuzzyLbAdapter(context, this);
    }

    @Override
    public void getFuzzySearchDetails(String key) {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            callFuzzyLbsApi(key);
        } else {
            onNoInternet();
        }
    }

    @Override
    public void onNoSearch() {
        LocalBroadcastManager.getInstance(Nostragamus.getInstance().getBaseContext())
                .sendBroadcast(new Intent(IntentActions.ACTION_FUZZY_LB_CLICK));
    }

    private void callFuzzyLbsApi(String key) {
        MyWebService.getInstance().getFuzzyLbsRequest(key)
                .enqueue(new NostragamusCallBack<FuzzyLbResponse>() {
                    @Override
                    public void onResponse(Call<FuzzyLbResponse> call, Response<FuzzyLbResponse> response) {
                        super.onResponse(call, response);

                        if (response.isSuccessful()) {
                            addAll(response.body().getLbs());
                        } else {
                            onFailed();
                        }
                    }
                });
    }
}