package in.sportscafe.nostragamus.module.challengeRules;
import com.jeeva.android.Log;


import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.challengeRules.dto.Rules;
import in.sportscafe.nostragamus.module.challengeRules.dto.RulesRequest;
import in.sportscafe.nostragamus.module.challengeRules.dto.RulesResponse;
import in.sportscafe.nostragamus.webservice.ApiCallBack;
import in.sportscafe.nostragamus.webservice.MyWebService;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by deepanshi on 9/15/17.
 */

public class RulesApiModelImpl {

    private static final String TAG = RulesApiModelImpl.class.getSimpleName();

    public RulesApiModelImpl() {
    }

    public void getRulesData(int contestId, RulesApiModelImpl.RulesDataListener listener) {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            loadRulesData(contestId, listener);
        } else {
            Log.d(TAG, "No Network connection");
            listener.onNoInternet();
        }
    }

    private void loadRulesData(int contestId, final RulesApiModelImpl.RulesDataListener listener) {

//        RulesRequest rulesRequest = new RulesRequest();
//        rulesRequest.setRoomId(contestId);

        MyWebService.getInstance().getContestRules(contestId).enqueue(new ApiCallBack<RulesResponse>() {
            @Override
            public void onResponse(Call<RulesResponse> call, Response<RulesResponse> response) {
                super.onResponse(call, response);

                if (response.isSuccessful() && response.body() != null && response.body() != null) {
                    Log.d(TAG, "Server response success");
                    handleRulesResponse(response.body().getRules(),listener);
                } else {
                    Log.d(TAG, "Server response null");
                    listener.onFailedConfigsApi();
                }
            }

            @Override
            public void onFailure(Call<RulesResponse> call, Throwable t) {
                super.onFailure(call, t);
                Log.d(TAG, "Server response Failed");
                listener.onFailedConfigsApi();
            }
        });
    }

    private void handleRulesResponse(Rules rules, RulesDataListener listener) {
        if (null == rules) {
            listener.onEmpty();
            return;
        }

        listener.onData(rules);
    }


    public interface RulesDataListener {

        void onData(Rules rules);

        void onError(int status);

        void onNoInternet();

        void onFailedConfigsApi();

        void onEmpty();

    }
}
