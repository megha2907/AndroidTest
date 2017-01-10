package in.sportscafe.nostragamus.module.user.playerprofile;

import java.util.List;

import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.user.login.dto.BasicUserInfo;
import in.sportscafe.nostragamus.module.user.playerprofile.dto.FuzzyPlayersResponse;
import in.sportscafe.nostragamus.module.user.playerprofile.dto.PlayerInfo;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by deepanshi on 12/22/16.
 */
public class FuzzyPlayerModelImpl {

    private PlayerInfo mplayerInfo;

    private FuzzyPlayerModelImpl.OnFuzzyPlayerModelListener mFuzzyPlayerModelListener;

    private FuzzyPlayerModelImpl(FuzzyPlayerModelImpl.OnFuzzyPlayerModelListener listener) {
        this.mFuzzyPlayerModelListener = listener;
    }

    public static FuzzyPlayerModelImpl newInstance(FuzzyPlayerModelImpl.OnFuzzyPlayerModelListener listener) {
        return new FuzzyPlayerModelImpl(listener);
    }

    public void getFuzzyPlayers(String key) {
        if(Nostragamus.getInstance().hasNetworkConnection()) {
            callFuzzyPlayersApi(key);
        } else {
            mFuzzyPlayerModelListener.onNoInternet();
        }
    }

    private void callFuzzyPlayersApi(String key) {
        MyWebService.getInstance().getFuzzyPlayersRequest(key)
                .enqueue(new NostragamusCallBack<FuzzyPlayersResponse>() {
                    @Override
                    public void onResponse(Call<FuzzyPlayersResponse> call, Response<FuzzyPlayersResponse> response) {
                        super.onResponse(call, response);

                        if(response.isSuccessful()) {
                            mFuzzyPlayerModelListener.onGetPlayers(response.body().getPlayers());
                        } else {
                            mFuzzyPlayerModelListener.onFailed();
                        }
                    }
                });
    }

    public interface OnFuzzyPlayerModelListener {

        void onGetPlayers(List<BasicUserInfo> players);

        void onFailed();

        void onNoInternet();
    }
}