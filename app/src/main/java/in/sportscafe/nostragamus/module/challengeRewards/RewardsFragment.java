package in.sportscafe.nostragamus.module.challengeRewards;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.parceler.Parcels;

import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.challengeRewards.dto.RewardScreenData;
import in.sportscafe.nostragamus.module.challengeRewards.dto.Rewards;
import in.sportscafe.nostragamus.module.challengeRewards.rewardsPool.RewardsPoolContestAdapter;
import in.sportscafe.nostragamus.module.challengeRewards.rewardsPool.RewardsPoolContestAdapterListener;
import in.sportscafe.nostragamus.module.common.NostraBaseFragment;
import in.sportscafe.nostragamus.module.contest.dto.PoolPrizeEstimationScreenData;
import in.sportscafe.nostragamus.module.contest.poolContest.PoolPrizesEstimationActivity;

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

    public void setScreenData(RewardScreenData screenData) {
        this.mScreenData = screenData;
    }

    public void setLauncherParent(int mLauncherParent) {
        this.mLauncherParent = mLauncherParent;
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

        fetchRewardsDataFromServer();
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
            public void onData(@Nullable List<Rewards> rewardsList, String challengeEndTime) {
                hideLoadingProgressBar();
                if (getView() != null && getActivity() != null && !getActivity().isFinishing()) {
                    if (mScreenData != null && mRcvRewards != null) {

                        if (mScreenData.isPoolContest()) {
                            RewardsPoolContestAdapter poolAdapter = new RewardsPoolContestAdapter(rewardsList, challengeEndTime,
                                    getPoolRewardsAdapterListener());
                            mRcvRewards.setAdapter(poolAdapter);
                            mRcvRewards.setVisibility(View.VISIBLE);

                        } else {
                            RewardsAdapter mConfigAdapter = createAdapter(rewardsList, challengeEndTime);
                            mRcvRewards.setAdapter(mConfigAdapter);
                            mRcvRewards.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onError(int status) {
                hideLoadingProgressBar();
                handleError(status);
            }

            @Override
            public void onNoInternet() {
                hideLoadingProgressBar();
                handleError(Constants.DataStatus.NO_INTERNET);
            }

            @Override
            public void onFailedConfigsApi() {
                hideLoadingProgressBar();
                handleError(Constants.DataStatus.FROM_SERVER_API_FAILED);
                // showMessage(Constants.Alerts.API_FAIL);
            }

            @Override
            public void onEmpty() {
                hideLoadingProgressBar();
                // showMessage(Constants.Alerts.POLL_LIST_EMPTY);
            }
        };
    }

    @NonNull
    private RewardsPoolContestAdapterListener getPoolRewardsAdapterListener() {
        return new RewardsPoolContestAdapterListener() {
            @Override
            public void onClickHereButtonClicked() {
                launchPoolContestRewardCalculation();
            }
        };
    }

    private void launchPoolContestRewardCalculation() {
        PoolPrizeEstimationScreenData screenData = new PoolPrizeEstimationScreenData();
        screenData.setRewardScreenLauncherParent(mLauncherParent);
        screenData.setContestName(mScreenData.getContestName());

        Bundle args = new Bundle();
        args.putInt(Constants.BundleKeys.SCREEN_LAUNCHED_FROM_PARENT, mLauncherParent);
        args.putParcelable(Constants.BundleKeys.POOL_PRIZE_ESTIMATION_SCREEN_DATA, Parcels.wrap(screenData));

        Intent intent = new Intent(this.getContext(), PoolPrizesEstimationActivity.class);
        intent.putExtras(args);
        startActivity(intent);
    }

    private RewardsAdapter createAdapter(List<Rewards> rewardsList,String challengeEndTime) {
        return new RewardsAdapter(getContext(), rewardsList, challengeEndTime);
    }

    private void handleError(int status) {
        if (getView() != null && getActivity() != null && !getActivity().isFinishing()) {
            switch (status) {
                case Constants.DataStatus.NO_INTERNET:
                    Snackbar.make(getView(), Constants.Alerts.NO_INTERNET_CONNECTION, Snackbar.LENGTH_LONG).show();
                    break;

                default:
                    Snackbar.make(getView(), Constants.Alerts.SOMETHING_WRONG, Snackbar.LENGTH_LONG).show();
                    break;
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
