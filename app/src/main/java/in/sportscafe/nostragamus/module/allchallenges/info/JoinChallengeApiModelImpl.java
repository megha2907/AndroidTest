package in.sportscafe.nostragamus.module.allchallenges.info;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.allchallenges.dto.Challenge;
import in.sportscafe.nostragamus.module.allchallenges.dto.ChallengeConfig;
import in.sportscafe.nostragamus.module.allchallenges.dto.ChallengeConfigsResponse;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Jeeva on 14/6/16.
 */
public class JoinChallengeApiModelImpl {

    private OnJoinChallengeApiModelListener mApiModelListener;

    public JoinChallengeApiModelImpl(OnJoinChallengeApiModelListener listener) {
        this.mApiModelListener = listener;
    }

    public static JoinChallengeApiModelImpl newInstance(OnJoinChallengeApiModelListener listener) {
        return new JoinChallengeApiModelImpl(listener);
    }

    public void joinChallenge(int challengeId, int config_index) {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            callJoinChallengeApi(challengeId, config_index);
        } else {
            mApiModelListener.onNoInternet();
        }
    }

    private void callJoinChallengeApi(int challengeId, int configIndex) {
        mApiModelListener.onApiCallStarted();

        MyWebService.getInstance().getJoinChallengeRequest(challengeId, configIndex).enqueue(
                new NostragamusCallBack<Challenge>() {
                    @Override
                    public void onResponse(Call<Challenge> call, Response<Challenge> response) {
                        super.onResponse(call, response);

                        if (!mApiModelListener.onApiCallStopped()) {
                            return;
                        }

                        if (response.isSuccessful()) {
                            Challenge challenge = response.body();
                            if(null != challenge) {
                                mApiModelListener.onSuccessJoinApi(challenge);
                                return;
                            }
                        }
                        mApiModelListener.onFailedJoinApi();
                    }
                }
        );
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

    public interface OnJoinChallengeApiModelListener {

        void onSuccessJoinApi(Challenge challenge);

        void onFailedJoinApi();

        void onNoInternet();

        void onApiCallStarted();

        boolean onApiCallStopped();
    }
}