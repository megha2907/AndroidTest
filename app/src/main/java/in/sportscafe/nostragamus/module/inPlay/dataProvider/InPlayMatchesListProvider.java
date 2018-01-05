package in.sportscafe.nostragamus.module.inPlay.dataProvider;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.jeeva.android.Log;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayContestDto;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayContestMatchDto;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayResponse;
import in.sportscafe.nostragamus.module.newChallenges.helpers.DateTimeHelper;
import in.sportscafe.nostragamus.webservice.ApiCallBack;
import in.sportscafe.nostragamus.webservice.MyWebService;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by deepanshi on 1/5/18.
 */

public class InPlayMatchesListProvider {

    private static final String TAG = InPlayMatchesListProvider.class.getSimpleName();

    public InPlayMatchesListProvider() {
    }

    public void getInPlayChallenges(Context appContext, InPlayMatchesListProviderListener listener) {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            loadInPlayDataFromServer(appContext, listener);
        } else {
            Log.d(TAG, "No Network connection");
        }
    }

    private void loadInPlayDataFromServer(final Context appContext, final InPlayMatchesListProviderListener listener) {

        MyWebService.getInstance().getInPlayChallenges().enqueue(new ApiCallBack<List<InPlayResponse>>() {
            @Override
            public void onResponse(Call<List<InPlayResponse>> call, Response<List<InPlayResponse>> response) {
                super.onResponse(call, response);

                if (response.isSuccessful() && response.body() != null && response.body() != null) {
                    Log.d(TAG, "Server response success");
                    onDataReceived(appContext, response.body(), listener);
                } else {
                    Log.d(TAG, "Server response null");
                    listener.onError(Constants.DataStatus.FROM_SERVER_API_FAILED);
                }
            }

            @Override
            public void onFailure(Call<List<InPlayResponse>> call, Throwable t) {
                super.onFailure(call, t);
                Log.d(TAG, "Server response Failed");
                listener.onError(Constants.DataStatus.FROM_SERVER_API_FAILED);
            }
        });
    }

    private void onDataReceived(Context appContext, List<InPlayResponse> inPlayResponseList, InPlayMatchesListProviderListener listener) {
        if (inPlayResponseList != null) {

            int unPlayedMatches = 0;
            List<InPlayContestMatchDto> inPlayContestMatchList = getMatchesListFromInplayResponse(inPlayResponseList);
            if (inPlayContestMatchList != null && !inPlayContestMatchList.isEmpty()) {

                for (InPlayContestMatchDto matchDto : inPlayContestMatchList) {
                    String startTime = matchDto.getStartTime();
                    if (!TextUtils.isEmpty(startTime)) {

                        if (!DateTimeHelper.isMatchStarted(startTime)) {
                            if ((matchDto.getStatus().equalsIgnoreCase(Constants.InPlayMatchStatus.ONGOING)) &&
                                    ((matchDto.isPlayed() == Constants.GameAttemptedStatus.NOT ||
                                        matchDto.isPlayed() == Constants.GameAttemptedStatus.PARTIALLY))) {

                                    unPlayedMatches++;
                            }
                        }
                    }
                }

                listener.onData(Constants.DataStatus.FROM_SERVER_API_SUCCESS,unPlayedMatches);

            } else {
                Log.e(TAG, "No InplayMatch list, null/empty - Cant continue for In-app notification");
            }
        } else {
            Log.e(TAG, "Database response null - Cant continue for In-app notification");
        }
    }

    private List<InPlayContestMatchDto> getMatchesListFromInplayResponse(List<InPlayResponse> inPlayResponseList) {
        List<InPlayContestMatchDto> matchDtoList = null;

        if (inPlayResponseList != null) {
            matchDtoList = new ArrayList<>();

            for (InPlayResponse inPlayResponse : inPlayResponseList) {

                if (inPlayResponse.getContestList() != null) {
                    for (InPlayContestDto inPlayContestDto : inPlayResponse.getContestList()) {

                        if (inPlayContestDto.getMatches() != null) {
                            for (InPlayContestMatchDto matchDto : inPlayContestDto.getMatches()) {

                                matchDtoList.add(matchDto);
                            }
                        }
                    }
                }
            }
        }

        return matchDtoList;
    }

    public interface InPlayMatchesListProviderListener {
        void onData(int status, @Nullable int unPlayedMatchCount);
        void onError(int status);
    }
}
