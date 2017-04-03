package in.sportscafe.nostragamus.module.allchallenges.info;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.allchallenges.dto.ChallengeConfig;
import in.sportscafe.nostragamus.module.allchallenges.dto.ChallengeConfigsResponse;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Jeeva on 14/6/16.
 */
public class ChallengeConfigsApiModelImpl {

    private OnConfigsApiModelListener mApiModelListener;

    public ChallengeConfigsApiModelImpl(OnConfigsApiModelListener listener) {
        this.mApiModelListener = listener;
    }

    public static ChallengeConfigsApiModelImpl newInstance(OnConfigsApiModelListener listener) {
        return new ChallengeConfigsApiModelImpl(listener);
    }

    public void getConfigs(int challengeId) {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            callConfigsApi(challengeId);
        } else {
            mApiModelListener.onNoInternet();
        }
    }

    private void callConfigsApi(int challengeId) {
        mApiModelListener.onApiCallStarted();

        MyWebService.getInstance().getChallengeConfigsRequest(challengeId).enqueue(
                new NostragamusCallBack<ChallengeConfigsResponse>() {
                    @Override
                    public void onResponse(Call<ChallengeConfigsResponse> call, Response<ChallengeConfigsResponse> response) {
                        super.onResponse(call, response);

                        if (!mApiModelListener.onApiCallStopped()) {
                            return;
                        }

                        if (response.isSuccessful()) {
                            handlePoolListResponse(response.body().getConfigs());
                        } else {
                            mApiModelListener.onFailedConfigsApi();
                        }
                    }
                }
        );
    }

    private void handlePoolListResponse(List<ChallengeConfig> configs) {
        if (null == configs || configs.isEmpty()) {
            mApiModelListener.onEmpty();
            return;
        }

        mApiModelListener.onSuccessConfigsApi(configs);
    }

    private List<ChallengeConfig> getDummyPoolList() {
        String json = null;
        try {
            InputStream is = Nostragamus.getInstance().getAssets().open("json/pool_list.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        if (null != json) {
            return MyWebService.getInstance().getObjectFromJson(
                    json,
                    ChallengeConfigsResponse.class
            ).getConfigs();
        }
        return null;
    }

    public interface OnConfigsApiModelListener {

        void onSuccessConfigsApi(List<ChallengeConfig> poolList);

        void onEmpty();

        void onFailedConfigsApi();

        void onNoInternet();

        void onApiCallStarted();

        boolean onApiCallStopped();
    }
}