package in.sportscafe.scgame.module.user.preference;

import java.util.List;

import in.sportscafe.scgame.ScGame;
import in.sportscafe.scgame.ScGameDataHandler;
import in.sportscafe.scgame.module.common.ApiResponse;
import in.sportscafe.scgame.module.user.sportselection.dto.PreferenceRequest;
import in.sportscafe.scgame.webservice.MyWebService;
import in.sportscafe.scgame.webservice.ScGameCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Jeeva on 30/6/16.
 */
public class SavePreferenceModelImpl {

    private SavePreferenceModelListener mSavePreferenceModelListener;

    public SavePreferenceModelImpl(SavePreferenceModelListener modelListener) {
        this.mSavePreferenceModelListener = modelListener;
    }

    public void savePreference(List<Integer> selectedSports) {
        ScGameDataHandler scGameDataHandler = ScGameDataHandler.getInstance();
        if(scGameDataHandler.isLoggedInUser()) {

            if(ScGame.getInstance().hasNetworkConnection()) {
                PreferenceRequest preferenceRequest = new PreferenceRequest(scGameDataHandler.getUserId());
                preferenceRequest.setSportPreferences(selectedSports);

                callPreferenceApi(preferenceRequest);
            } else {
                mSavePreferenceModelListener.onNoInternet();
            }
        } else {
            mSavePreferenceModelListener.requireLogin();
        }
    }

    private void callPreferenceApi(PreferenceRequest preferenceRequest) {
        MyWebService.getInstance().savePreferenceRequest(preferenceRequest).enqueue(
                new ScGameCallBack<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if(response.isSuccessful()) {
                            mSavePreferenceModelListener.onSuccess();
                        } else {
                            mSavePreferenceModelListener.onFailed(response.message());
                        }
                    }
                });
    }

    public interface SavePreferenceModelListener {

        void onSuccess();

        void requireLogin();

        void onNoInternet();

        void onFailed(String message);
    }
}