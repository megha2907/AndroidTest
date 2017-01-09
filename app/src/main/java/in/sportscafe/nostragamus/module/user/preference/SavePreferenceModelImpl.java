package in.sportscafe.nostragamus.module.user.preference;

import java.util.List;

import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.common.ApiResponse;
import in.sportscafe.nostragamus.module.user.sportselection.dto.PreferenceRequest;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
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
        if(Nostragamus.getInstance().hasNetworkConnection()) {
            callPreferenceApi(selectedSports);
        } else {
            mSavePreferenceModelListener.onNoInternet();
        }
    }

    private void callPreferenceApi(final List<Integer> selectedSports) {
        PreferenceRequest preferenceRequest = new PreferenceRequest();
        preferenceRequest.setSportPreferences(selectedSports);

        MyWebService.getInstance().savePreferenceRequest(preferenceRequest).enqueue(
                new NostragamusCallBack<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if(response.isSuccessful()) {
                            NostragamusDataHandler.getInstance().setFavoriteSportsIdList(selectedSports);
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