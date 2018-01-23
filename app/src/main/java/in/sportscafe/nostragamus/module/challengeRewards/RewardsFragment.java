package in.sportscafe.nostragamus.module.challengeRewards;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.parceler.Parcels;

import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.challengeRewards.dto.RewardScreenData;
import in.sportscafe.nostragamus.module.challengeRewards.dto.Rewards;
import in.sportscafe.nostragamus.module.challengeRewards.dto.RewardsResponse;
import in.sportscafe.nostragamus.module.common.NostraBaseFragment;
import in.sportscafe.nostragamus.module.customViews.CustomSnackBar;
import in.sportscafe.nostragamus.module.resultspeek.FeedWebView;

/**
 * Created by deepanshi on 9/6/17.
 */

public class RewardsFragment extends NostraBaseFragment implements View.OnClickListener {

    private static final String TAG = RewardsFragment.class.getSimpleName();

    private RewardScreenData mScreenData;
    private RecyclerView mRcvRewards;
    private int mLauncherParent = RewardsLaunchedFrom.NEW_CHALLENGE_CONTEST_DETAILS;

    public RewardsFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_rewards, container, false);
        initViews(rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initMembers();
        fetchRewardsDataFromServer();
    }

    private void initMembers() {
        Bundle args = getArguments();
        if (args != null) {
            if (args.containsKey(Constants.BundleKeys.REWARDS_SCREEN_DATA)) {
                mScreenData = Parcels.unwrap(args.getParcelable(Constants.BundleKeys.REWARDS_SCREEN_DATA));
            }

            mLauncherParent = args.getInt(Constants.BundleKeys.SCREEN_LAUNCHED_FROM_PARENT,
                    RewardsLaunchedFrom.NEW_CHALLENGE_CONTEST_DETAILS);
        }
    }


    private void initViews(View rootView) {
        this.mRcvRewards = (RecyclerView)rootView. findViewById(R.id.rewards_rcv);
        this.mRcvRewards.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        this.mRcvRewards.setHasFixedSize(true);
    }

    private void fetchRewardsDataFromServer() {
        if (mScreenData != null) {
            showLoadingProgressBar();
            new RewardsApiModelImpl().getRewardsData(mScreenData.getRoomId(),
                    mScreenData.getConfigId(), getApiListener());
        }
    }

    @NonNull
    private RewardsApiModelImpl.RewardsDataListener getApiListener() {
        return new RewardsApiModelImpl.RewardsDataListener() {
            @Override
            public void onData(RewardsResponse rewardsResponse) {
                hideLoadingProgressBar();

                onRewardsDataFetchedSuccessfully(rewardsResponse);
            }

            @Override
            public void onError(int status) {
                hideLoadingProgressBar();
                handleError("", status);
            }

            @Override
            public void onNoInternet() {
                hideLoadingProgressBar();
                handleError("", Constants.DataStatus.NO_INTERNET);
            }

            @Override
            public void onFailedConfigsApi() {
                hideLoadingProgressBar();
                handleError("", Constants.DataStatus.FROM_SERVER_API_FAILED);
                // showMessage(Constants.Alerts.API_FAIL);
            }

            @Override
            public void onEmpty() {
                hideLoadingProgressBar();
                /* If empty data, while showing/resuming screen; it should be shown */
                // showMessage(Constants.Alerts.POLL_LIST_EMPTY);
            }
        };
    }

    private void onRewardsDataFetchedSuccessfully(RewardsResponse rewardsResponse) {
        if (getView() != null && getActivity() != null && !getActivity().isFinishing()) {
            if (rewardsResponse != null && rewardsResponse.getRewardsList() != null) {

                RewardsAdapter rewardsAdapter =
                        createAdapter(rewardsResponse.getRewardsList(), rewardsResponse.getChallengeEndTime());
                if (rewardsAdapter != null) {
                    mRcvRewards.setAdapter(rewardsAdapter);
                    mRcvRewards.setVisibility(View.VISIBLE);
                }
            } else {
                handleError("", -1);
            }
        }
    }

    @Nullable
    private RewardsAdapter createAdapter(List<Rewards> rewardsList, String challengeEndTime) {
        if (rewardsList != null && !TextUtils.isEmpty(challengeEndTime)) {
            return new RewardsAdapter(getContext(), rewardsList, challengeEndTime, new RewardsAdapterListener() {
                @Override
                public void onNostraRulesClicked() {
                    openRulesWebView();
                }
            });
        }
        return null;
    }

    private void openRulesWebView() {
        startActivity(new Intent(getContext(), FeedWebView.class).putExtra("url", Constants.WebPageUrls.RULES));
    }

    private void handleError(String msg, int status) {
        if (getView() != null && getActivity() != null && !getActivity().isFinishing()) {
            if (!TextUtils.isEmpty(msg)) {
                CustomSnackBar.make(getView(), msg, CustomSnackBar.DURATION_LONG).show();
            } else {
                switch (status) {
                    case Constants.DataStatus.NO_INTERNET:
                        CustomSnackBar.make(getView(), Constants.Alerts.NO_INTERNET_CONNECTION, CustomSnackBar.DURATION_LONG).show();
                        break;

                    default:
                        CustomSnackBar.make(getView(), Constants.Alerts.SOMETHING_WRONG, CustomSnackBar.DURATION_LONG).show();
                        break;
                }
            }
        }
    }

    private void showLoadingProgressBar() {
        if (getView() != null) {
            getView().findViewById(R.id.rewards_rcv).setVisibility(View.GONE);
            getView().findViewById(R.id.rewards_progress_bar_layout).setVisibility(View.VISIBLE);
        }
    }

    private void hideLoadingProgressBar() {
        if (getView() != null) {
            getView().findViewById(R.id.rewards_progress_bar_layout).setVisibility(View.GONE);
            getView().findViewById(R.id.rewards_rcv).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

}
