package in.sportscafe.nostragamus.module.contest.dataProvider;

import android.support.annotation.NonNull;

import com.jeeva.android.Log;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.contest.dto.ContestRequest;
import in.sportscafe.nostragamus.module.contest.dto.ContestResponse;
import in.sportscafe.nostragamus.module.contest.dto.ContestType;
import in.sportscafe.nostragamus.module.newChallenges.dto.NewChallengesResponse;
import in.sportscafe.nostragamus.webservice.ApiCallBack;
import in.sportscafe.nostragamus.webservice.MyWebService;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by sandip on 01/09/17.
 */

public class ContestDataProvider {

    private static final String TAG = ContestDataProvider.class.getSimpleName();

    public ContestDataProvider() {}

    public void getContestDetails(int challengeId, @NonNull ContestDataProviderListener listener) {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            loadContests(challengeId, listener);
        } else {
            if (listener != null) {
                listener.onError(Constants.DataStatus.NO_INTERNET);
            }
        }
    }

    private void loadContests(int challengeId, final ContestDataProviderListener listener) {

        MyWebService.getInstance().getContests(challengeId).enqueue(new ApiCallBack<ContestResponse>() {
            @Override
            public void onResponse(Call<ContestResponse> call, Response<ContestResponse> response) {
                super.onResponse(call, response);

                if (response.isSuccessful() && response.body() != null && response.body() != null) {
                    Log.d(TAG, "Server response success");
                    if (listener != null) {
                        listener.onSuccessResponse(Constants.DataStatus.FROM_SERVER_API_SUCCESS, response.body());
                    }
                } else {
                    Log.d(TAG, "Server response null");
                    if (listener != null) {
                        listener.onError(Constants.DataStatus.FROM_SERVER_API_FAILED);
                    }
                }
            }

            @Override
            public void onFailure(Call<ContestResponse> call, Throwable t) {
                super.onFailure(call, t);
                Log.d(TAG, "Server response Failed");
                if (listener != null) {
                    listener.onError(Constants.DataStatus.FROM_SERVER_API_FAILED);
                }
            }
        });

    }

    public List<ContestType> getContestTypeList () {
        List<ContestType> contestTypes = new ArrayList<>();

        ContestType h2h = new ContestType();
        h2h.setId(0);
        h2h.setName("H 2 H Contests");
        h2h.setTagLine("Finish on the Top Half of the leaderboards to win");

        ContestType doubleUp = new ContestType();
        doubleUp.setId(1);
        doubleUp.setName("Double Up Contests");
        doubleUp.setTagLine("Finish on the Top Half of the leaderboards to win");

        ContestType glory = new ContestType();
        glory.setId(2);
        glory.setName("Glory Contests");
        glory.setTagLine("Finish on the Top Half of the leaderboards to win");

        ContestType free = new ContestType();
        free.setId(3);
        free.setName("Free Contests");
        free.setTagLine("Finish on the Top Half of the leaderboards to win");

        contestTypes.add(h2h);
        contestTypes.add(doubleUp);
        contestTypes.add(glory);
        contestTypes.add(free);

        return contestTypes;
    }

    public interface ContestDataProviderListener {
        void onSuccessResponse(int status, ContestResponse response);
        void onError(int status);
    }
}
