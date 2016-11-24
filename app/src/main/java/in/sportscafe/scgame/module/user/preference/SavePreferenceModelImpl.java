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
        if(ScGame.getInstance().hasNetworkConnection()) {
            callPreferenceApi(selectedSports);
        } else {
            mSavePreferenceModelListener.onNoInternet();
        }
    }

    private void callPreferenceApi(final List<Integer> selectedSports) {
        PreferenceRequest preferenceRequest = new PreferenceRequest(
                ScGameDataHandler.getInstance().getUserId());
        preferenceRequest.setSportPreferences(selectedSports);

        MyWebService.getInstance().savePreferenceRequest(preferenceRequest).enqueue(
                new ScGameCallBack<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if(response.isSuccessful()) {
                            ScGameDataHandler.getInstance().setFavoriteSportsIdList(selectedSports);
                            mSavePreferenceModelListener.onSuccess();
                        } else {
                            mSavePreferenceModelListener.onFailed(response.message());
                        }
                    }
                });
    }

    public interface SavePreferenceModelListener {

        void onSuccess();

        void onNoInternet();

        void onFailed(String message);
    }
}