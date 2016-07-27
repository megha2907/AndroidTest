package in.sportscafe.scgame.module.user.preference;

import java.util.List;

import in.sportscafe.scgame.ScGame;
import in.sportscafe.scgame.ScGameDataHandler;
import in.sportscafe.scgame.module.user.sportselection.dto.UserSports;
import in.sportscafe.scgame.webservice.MyWebService;
import in.sportscafe.scgame.webservice.ScGameCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Jeeva on 30/6/16.
 */
public class GetPreferenceModelImpl {

    private GetPreferenceModelListener mGetPreferenceModelListener;

    public GetPreferenceModelImpl(GetPreferenceModelListener modelListener) {
        this.mGetPreferenceModelListener = modelListener;
    }

    public void getPreference() {
        ScGameDataHandler scGameDataHandler = ScGameDataHandler.getInstance();
        if(scGameDataHandler.isLoggedInUser()) {

            if(ScGame.getInstance().hasNetworkConnection()) {
                callGetPreferenceApi();
            } else {
                mGetPreferenceModelListener.onNoInternet();
            }
        }
    }

    private void callGetPreferenceApi() {
        MyWebService.getInstance().getUserSportsRequest().enqueue(
                new ScGameCallBack<UserSports>() {
                    @Override
                    public void onResponse(Call<UserSports> call, Response<UserSports> response) {
                        if(response.isSuccessful()) {
                            List<Integer> userSports = response.body().getSports();

                            ScGameDataHandler scGameDataHandler = ScGameDataHandler.getInstance();
                            scGameDataHandler.setFavoriteSportsIdList(userSports);

                            mGetPreferenceModelListener.onSuccess();
                        } else {
                            mGetPreferenceModelListener.onFailed(response.code(), response.message());
                        }
                    }
                });
    }

    public interface GetPreferenceModelListener {

        void onSuccess();

        void onNoInternet();

        void onFailed(int code, String message);
    }
}