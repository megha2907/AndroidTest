package in.sportscafe.nostragamus.module.fuzzyplayers;

import android.content.Context;

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
public class FuzzyPlayerModelImpl extends AbstractFuzzySearchModelImpl<BasicUserInfo> {

    private Integer mMatchId;

    private FuzzyPlayerModelImpl(OnFuzzySearchModelListener listener, int matchId) {
        super(listener);
        this.mMatchId = matchId;
    }

    public static AbstractFuzzySearchModel newInstance(OnFuzzySearchModelListener listener, int matchId) {
        return new FuzzyPlayerModelImpl(listener,matchId);
    }

    @Override
    public Adapter getAdapter(Context context) {
        return new FuzzyPlayerAdapter(context);
    }

    @Override
    public void getFuzzySearchDetails(String key) {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            callFuzzyPlayersApi(key,mMatchId);
        } else {
            onNoInternet();
        }
    }

    @Override
    public void onNoSearch() {

    }

    private void callFuzzyPlayersApi(String key, Integer matchId) {
        MyWebService.getInstance().getFuzzyPlayersRequest(key,matchId)
                .enqueue(new NostragamusCallBack<FuzzyPlayerResponse>() {
                    @Override
                    public void onResponse(Call<FuzzyPlayerResponse> call, Response<FuzzyPlayerResponse> response) {
                        super.onResponse(call, response);

                        if (response.isSuccessful()) {
                            addAll(response.body().getPlayers());
                        } else {
                            onFailed();
                        }
                    }
                });
    }
}