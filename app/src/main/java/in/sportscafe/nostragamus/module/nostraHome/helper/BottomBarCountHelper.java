package in.sportscafe.nostragamus.module.nostraHome.helper;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.jeeva.android.Log;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.cache.CacheManagementHelper;
import in.sportscafe.nostragamus.module.inPlay.dataProvider.InPlayDataProvider;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayContestDto;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayContestMatchDto;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayResponse;
import in.sportscafe.nostragamus.module.newChallenges.helpers.DateTimeHelper;

/**
 * Created by sc on 12/1/18.
 */

public class BottomBarCountHelper {
    private static final String TAG = BottomBarCountHelper.class.getSimpleName();

    /**
     *
     * @param appContext
     * @param listener
     */
    public void getInplayCounter(Context appContext, final BottomBarCountHelperListener listener) {
        CacheManagementHelper cacheManagementHelper = new CacheManagementHelper();
        cacheManagementHelper.loadInPlayFromDatabase(appContext, new InPlayDataProvider.InPlayDataProviderListener() {
            @Override
            public void onData(int status, @Nullable List<InPlayResponse> inPlayResponseList) {
                switch (status) {
                    case Constants.DataStatus.FROM_DATABASE_CACHED_DATA:
                        onInplayDbResponse(inPlayResponseList, listener);
                        break;

                    default:
                        android.util.Log.d(TAG, "DB error, can not update counter");
                        break;
                }
            }

            @Override
            public void onError(int status) {
                android.util.Log.d(TAG, "DB error, can not update counter");
            }
        });
    }

    private void onInplayDbResponse(final List<InPlayResponse> inPlayResponseList, final BottomBarCountHelperListener listener) {
        if (inPlayResponseList != null) {

            new AsyncTask<Void, Void, Integer>() {

                @Override
                protected Integer doInBackground(Void... params) {
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
                    } else {
                        Log.e(TAG, "No InplayMatch list, null/empty - Cant continue for In-app notification");
                    }
                    return unPlayedMatches;
                }

                @Override
                protected void onPostExecute(Integer unPlayedMatches) {
                    super.onPostExecute(unPlayedMatches);

                    if (listener != null && unPlayedMatches != null) {
                        listener.onData(Constants.DataStatus.FROM_SERVER_API_SUCCESS, unPlayedMatches);
                    }
                }
            }.execute();

        } else {
            Log.e(TAG, "Database response null - Cant continue for In-app notification");
            if (listener != null) {
                listener.onError(Constants.DataStatus.FROM_DATABASE_ERROR);
            }
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

    public interface BottomBarCountHelperListener {
        void onData(int status, int unPlayedMatchCount);
        void onError(int status);
    }
}
