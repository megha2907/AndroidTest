package in.sportscafe.nostragamus.module.contest.dataProvider;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.jeeva.android.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.contest.adapter.ContestAdapterItemType;
import in.sportscafe.nostragamus.module.contest.dto.Contest;
import in.sportscafe.nostragamus.module.contest.dto.ContestResponse;
import in.sportscafe.nostragamus.module.contest.dto.ContestType;
import in.sportscafe.nostragamus.module.contest.helper.ContestFilterHelper;
import in.sportscafe.nostragamus.webservice.ApiCallBack;
import in.sportscafe.nostragamus.webservice.MyWebService;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by sandip on 01/09/17.
 */

public class ContestDataProvider {

    private static final String TAG = ContestDataProvider.class.getSimpleName();
    public static final int JOINED_CONTEST_ID = -1;

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

        boolean needPoolContests = true;
        MyWebService.getInstance().getContests(challengeId,needPoolContests).enqueue(new ApiCallBack<ContestResponse>() {
            @Override
            public void onResponse(Call<ContestResponse> call, Response<ContestResponse> response) {
                super.onResponse(call, response);

                if (response.isSuccessful() && response.body() != null && response.body() != null) {
                    Log.d(TAG, "Server response success");
                    if (listener != null) {
                        listener.onSuccessResponse(Constants.DataStatus.FROM_SERVER_API_SUCCESS,
                                getFilteredJoinableContestList(response.body()));
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

    private ContestResponse getFilteredJoinableContestList(ContestResponse contestResponse) {
        if (contestResponse != null && contestResponse.getData() != null && contestResponse.getData().getContests() != null) {
            for(Contest contest : contestResponse.getData().getContests()) {
                if (!contest.isJoinable()) {
                    contest.setContestItemType(ContestAdapterItemType.NON_JOINABLE_CONTEST);
                }
            }
        }
        return contestResponse;
    }

    public List<ContestType> getContestTypeList(List<Contest> contestList) {
        List<ContestType> contestTypes = new ArrayList<>();

        Set<String> contestSet = new HashSet<>();
        if (contestList != null) {
            /* Add contest string for all contest category names */
            for (Contest contest : contestList) {
                if (contest.getContestType() != null && !TextUtils.isEmpty(contest.getContestType().getCategoryName()) ) {
                    String categoryName = contest.getContestType().getCategoryName();
                    if (!categoryName.contains("Contest")) {
                        categoryName = categoryName + " Contest";
                        contest.getContestType().setCategoryName(categoryName);
                    }
                }
            }

            /* Create a list of unique contest-names (Set will not keep duplicate values) */
            for (Contest contest : contestList) {
                if (contest.getContestType() != null && !TextUtils.isEmpty(contest.getContestType().getCategoryName())) {
                    contestSet.add(contest.getContestType().getCategoryName());
                }
            }

            /* create list of all contest-types based on contest-names  */
            for (String contestName : contestSet) {
                for (Contest contest : contestList) {
                    if (contest.getContestType() != null &&
                            contestName.equalsIgnoreCase(contest.getContestType().getCategoryName())) {
                        contestTypes.add(contest.getContestType());
                        break;
                    }
                }
            }

            Collections.sort(contestTypes, new Comparator<ContestType>() {
                @Override
                public int compare(ContestType contestType1, ContestType contestType2) {
                    if (contestType1.getPriority() < contestType2.getPriority()) {
                        return 1;
                    } else if (contestType1.getPriority() == contestType2.getPriority()) {
                        return 0;
                    }
                    return -1;
                }
            });
            
            /* Add joined-contest always at end  */
            ContestType joinedContestType = new ContestType();
            joinedContestType.setCategoryName(ContestFilterHelper.JOINED_CONTEST);
            joinedContestType.setCategoryDesc("These are the contests you've already joined");
            joinedContestType.setPriority(-1);

            contestTypes.add(joinedContestType);
        }

        return contestTypes;
    }

    public interface ContestDataProviderListener {
        void onSuccessResponse(int status, ContestResponse response);
        void onError(int status);
    }
}
