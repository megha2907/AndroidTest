package in.sportscafe.nostragamus.module.contest.ui.poolContest;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jeeva.android.BaseFragment;
import com.jeeva.android.Log;

import org.parceler.Parcels;

import java.util.List;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.challengeRewards.RewardsApiModelImpl;
import in.sportscafe.nostragamus.module.challengeRewards.RewardsLaunchedFrom;
import in.sportscafe.nostragamus.module.challengeRewards.dto.Rewards;
import in.sportscafe.nostragamus.module.challengeRewards.dto.RewardsResponse;
import in.sportscafe.nostragamus.module.contest.adapter.PoolRewardsAdapter;
import in.sportscafe.nostragamus.module.contest.dto.PoolPrizeEstimationScreenData;
import in.sportscafe.nostragamus.module.contest.dto.pool.PoolPayoutMap;
import in.sportscafe.nostragamus.module.contest.helper.PoolPrizesEstimationHelper;
import in.sportscafe.nostragamus.module.customViews.CustomSnackBar;
import in.sportscafe.nostragamus.module.newChallenges.helpers.DateTimeHelper;
import in.sportscafe.nostragamus.module.resultspeek.FeedWebView;
import in.sportscafe.nostragamus.utils.timeutils.TimeAgo;
import in.sportscafe.nostragamus.utils.timeutils.TimeUnit;
import in.sportscafe.nostragamus.utils.timeutils.TimeUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class PoolPrizesEstimationFragment extends BaseFragment implements View.OnClickListener {

    private final String TAG = PoolPrizesEstimationFragment.class.getSimpleName();
    private PoolPrizesEstimationHelper mPoolPrizesEstimationHelper;
    private PoolPrizeEstimationScreenData mScreenData;
    private RewardsResponse mRewardsApiResponse;
    private RecyclerView mRecyclerView;
    private SeekBar mSeekBar;

    public PoolPrizesEstimationFragment() {
        mPoolPrizesEstimationHelper = new PoolPrizesEstimationHelper();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pool_prize_estimation, container, false);
        initRootView(rootView);
        return rootView;
    }

    private void initRootView(View rootView) {
        mSeekBar = (SeekBar) rootView.findViewById(R.id.estimation_seekbar);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.pool_estimation_recyclerView);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        mSeekBar.setOnSeekBarChangeListener(getSeekBarChangeListener());
    }

    @NonNull
    private SeekBar.OnSeekBarChangeListener getSeekBarChangeListener() {
        return new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setEntries(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                onSeekBarTrackingCompleted(progress);
            }
        };
    }

    private void onSeekBarTrackingCompleted(int progress) {
        if (mRewardsApiResponse != null) {
            PoolRewardsAdapter adapter = new PoolRewardsAdapter(
                    getEstimatedRewardsList(progress),
                    mRewardsApiResponse.getChallengeEndTime(),
                    mRewardsApiResponse.getChallengeStartTime(), null);
            mRecyclerView.setAdapter(adapter);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void setEntries(int progress) {
        if (getView() != null) {
            TextView entriesTextView = (TextView) getView().findViewById(R.id.estimation_slider_progress_textView);
            int entries = getMinParticipants() + progress;
            entriesTextView.setText(entries + " Entries");

            /* Entries msg */
            String msgStr = String.format(getString(R.string.pool_prize_estimation_msg),
                    String.valueOf(entries));
            TextView msgTextview = (TextView) getView().findViewById(R.id.estimation_msg_textView);

            int startIndex = msgStr.indexOf(String.valueOf(entries));
            int endIndex = msgStr.indexOf(String.valueOf(entries)) + String.valueOf(entries).length() + String.valueOf(" entries").length();
            SpannableStringBuilder builder = new SpannableStringBuilder();
            SpannableString whiteSpannable = new SpannableString(msgStr);
            whiteSpannable.setSpan(new ForegroundColorSpan(Color.WHITE), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            whiteSpannable.setSpan(new StyleSpan(Typeface.BOLD), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.append(whiteSpannable);
            msgTextview.setText(builder, TextView.BufferType.SPANNABLE);

        }
    }

    @Nullable
    private List<Rewards> getEstimatedRewardsList(int progress) {
        List<Rewards> estimatedRewardsList = null;

        if (mRewardsApiResponse != null && mPoolPrizesEstimationHelper != null) {
            int participants = getMinParticipants() + progress;

            double totalPrize = mPoolPrizesEstimationHelper.getTotalPrize(participants, getPerUserPrizeValue());

            estimatedRewardsList = mPoolPrizesEstimationHelper.getEstimatedRewardsList(participants,
                    getPoolPayoutMapList(), getRoundingLevel(), totalPrize);
        }

        return estimatedRewardsList;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initMembers();
        fetchPoolRewardsDataFromServer();
    }

    private void initMembers() {
        Bundle args = getArguments();
        if (args != null) {
            if (args.containsKey(Constants.BundleKeys.POOL_PRIZE_ESTIMATION_SCREEN_DATA)) {
                mScreenData = Parcels.unwrap(args.getParcelable(Constants.BundleKeys.POOL_PRIZE_ESTIMATION_SCREEN_DATA));
            }
        }
    }

    private void fetchPoolRewardsDataFromServer() {
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
                onPoolDataFetchedSuccessfully(rewardsResponse);
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

    private void onPoolDataFetchedSuccessfully(RewardsResponse rewardsResponse) {
        if (getView() != null && getActivity() != null && !getActivity().isFinishing()) {
            if (rewardsResponse != null) {
                mRewardsApiResponse = rewardsResponse;

                boolean isChallengeStarted = DateTimeHelper.isChallengeTimeOver(mRewardsApiResponse.getChallengeStartTime());

                if (mScreenData != null) {

                    /* check if Rewards Opened in History Section */
                    if (mScreenData.getRewardScreenLauncherParent() == RewardsLaunchedFrom.CHALLENGE_HISTORY_CONTEST_DETAILS) {
                        showChallengeInHistoryView(rewardsResponse.getChallengeEndTime());
                    }
                    /* else check if challenge Not Started for New challenges section */
                    else if (!isChallengeStarted) {
                        showPrizeEstimationViews(rewardsResponse.getRewardsList());
                    }
                    /* else check if challenge Started for inPlay section */
                    else {
                        showChallengeStartedView();
                    }

                }

            } else {
                Log.e(TAG, "Rewards Response in Rewards for poolContest is null!!");
            }
        }
    }

    private void showChallengeInHistoryView(String challengeEndTime) {

        LinearLayout estimationLayout = (LinearLayout) getView().findViewById(R.id.pool_prize_estimation_layout);
        estimationLayout.setVisibility(View.GONE);

        LinearLayout challengeStartedInfoLayout = (LinearLayout) getView().findViewById(R.id.pool_prize_challenge_started_layout);
        challengeStartedInfoLayout.setVisibility(View.VISIBLE);

        TextView msgTextView = (TextView) getView().findViewById(R.id.pool_clng_started_msg_textView);

        String endTime = challengeEndTime;
        long endTimeMs = TimeUtils.getMillisecondsFromDateString(
                endTime,
                Constants.DateFormats.FORMAT_DATE_T_TIME_ZONE,
                Constants.DateFormats.GMT
        );

        int dayOfMonthinEndTime = Integer.parseInt(TimeUtils.getDateStringFromMs(endTimeMs, "d"));

        String prizesHandOutDate = dayOfMonthinEndTime + AppSnippet.ordinalOnly(dayOfMonthinEndTime) + " of " +
                TimeUtils.getDateStringFromMs(endTimeMs, "MMM");

        if (getChallengeOver(challengeEndTime)) {
            /* set when challenge is over */
            msgTextView.setText("Prizes were handed out on " + prizesHandOutDate + ".");
        } else {
            // Setting end date of the challenge
            msgTextView.setText("The challenge will end on " + prizesHandOutDate + ". Prizes will be handed out within a few hours of challenge completion.");
        }

        /* Set reward adapter */
        if (mRewardsApiResponse != null && mRewardsApiResponse.getRewardsList() != null) {
            PoolRewardsAdapter adapter = new PoolRewardsAdapter(
                    mRewardsApiResponse.getRewardsList(),
                    mRewardsApiResponse.getChallengeEndTime(),
                    mRewardsApiResponse.getChallengeStartTime(),
                    new PoolRewardsAdapterListener() {
                        @Override
                        public void onNostraRulesClicked() {
                            openRulesWebView();
                        }
                    });

            mRecyclerView.setAdapter(adapter);
            mRecyclerView.setVisibility(View.VISIBLE);

        }
    }

    private boolean getChallengeOver(String challengeEndTime) {

        boolean isChallengeOver = false;

        if (!TextUtils.isEmpty(challengeEndTime)) {
            String startTime = challengeEndTime.replace("+00:00", ".000Z");
            long startTimeMs = TimeUtils.getMillisecondsFromDateString(
                    startTime,
                    Constants.DateFormats.FORMAT_DATE_T_TIME_ZONE,
                    Constants.DateFormats.GMT
            );
            TimeAgo timeAgo = TimeUtils.calcTimeAgo(Nostragamus.getInstance().getServerTime(), startTimeMs);

            isChallengeOver = timeAgo.timeDiff <= 0
                    || timeAgo.timeUnit == TimeUnit.MILLISECOND
                    || timeAgo.timeUnit == TimeUnit.SECOND;
        }

        return isChallengeOver;
    }


    private void showChallengeStartedView() {
        if (getView() != null) {
            LinearLayout challengeStartedInfoLayout = (LinearLayout) getView().findViewById(R.id.pool_prize_challenge_started_layout);
            challengeStartedInfoLayout.setVisibility(View.VISIBLE);

            TextView allEntriesFilledTextView = (TextView) getView().findViewById(R.id.pool_clng_started_all_entries_filled_textView);
            TextView msgTextView = (TextView) getView().findViewById(R.id.pool_clng_started_msg_textView);

            int participants = getUsersJoined();
            if (isAllEntriesFilled()) {

                String str = "All " + participants + " entries filled!";
                allEntriesFilledTextView.setText(str);
                allEntriesFilledTextView.setVisibility(View.VISIBLE);

                String msg = "Maximum prize money up for grabs!";
                msgTextView.setText(msg);

            } else {

                String msg = "Prize money has been re-calculated as only " + participants + " members joined the contest out of a possible " + getMaxParticipants();

                int startIndex = msg.indexOf(String.valueOf(participants));
                int endIndex = msg.indexOf(String.valueOf(participants)) + String.valueOf(participants).length() + String.valueOf(" members").length();
                SpannableStringBuilder builder = new SpannableStringBuilder();
                SpannableString whiteSpannable = new SpannableString(msg);
                whiteSpannable.setSpan(new ForegroundColorSpan(Color.WHITE), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                whiteSpannable.setSpan(new StyleSpan(Typeface.BOLD), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                //whiteSpannable.setSpan(new ForegroundColorSpan(Color.WHITE), 43, 55, 0);
                //whiteSpannable.setSpan(new StyleSpan(Typeface.BOLD), 43, 55, 0);
                builder.append(whiteSpannable);
                msgTextView.setText(builder, TextView.BufferType.SPANNABLE);
            }

            /* Set reward adapter */
            if (mRewardsApiResponse != null && mRewardsApiResponse.getRewardsList() != null) {
                PoolRewardsAdapter adapter = new PoolRewardsAdapter(
                        mRewardsApiResponse.getRewardsList(),
                        mRewardsApiResponse.getChallengeEndTime(),
                        mRewardsApiResponse.getChallengeStartTime(),
                        new PoolRewardsAdapterListener() {
                            @Override
                            public void onNostraRulesClicked() {
                                openRulesWebView();
                            }
                        });

                mRecyclerView.setAdapter(adapter);
                mRecyclerView.setVisibility(View.VISIBLE);
            } else {
                Log.e(TAG, "Pool contest - challenge started , but rewards list is null!");
            }
        }
    }

    private void openRulesWebView() {
        startActivity(new Intent(getContext(), FeedWebView.class).putExtra("url", Constants.WebPageUrls.RULES));
    }

    private void showPrizeEstimationViews(List<Rewards> rewardsList) {
        if (getView() != null) {
            LinearLayout estimationLayout = (LinearLayout) getView().findViewById(R.id.pool_prize_estimation_layout);
            estimationLayout.setVisibility(View.VISIBLE);

            populateSeekbarValues();

            if (rewardsList != null && !rewardsList.isEmpty()) {
                PoolRewardsAdapter adapter = new PoolRewardsAdapter(
                        rewardsList,
                        mRewardsApiResponse.getChallengeEndTime(),
                        mRewardsApiResponse.getChallengeStartTime(), null);
                mRecyclerView.setAdapter(adapter);
                mRecyclerView.setVisibility(View.VISIBLE);
            }
        }
    }

    private void populateSeekbarValues() {
        if (getView() != null) {
            TextView minEntryTextView = (TextView) getView().findViewById(R.id.estimation_seekbar_min_textView);
            TextView maxEntryTextView = (TextView) getView().findViewById(R.id.estimation_seekbar_max_textView);

            int max = getMaxParticipants();
            int min = getMinParticipants();

            minEntryTextView.setText(String.valueOf(min));
            maxEntryTextView.setText(String.valueOf(max));

            /* Seekbar values */
            mSeekBar.setMax(max - min);   // As min can not be set into Seekbar, add min while considering progress
            mSeekBar.setProgress(max);
            onSeekBarTrackingCompleted(mSeekBar.getProgress());
        }
    }

    private boolean isAllEntriesFilled() {
        boolean isTrue = false;

        // TODO: change based on response

        return isTrue;
    }

    private int getMinParticipants() {
        int min = 0;
        if (mRewardsApiResponse != null && mRewardsApiResponse.getPoolContestResponse() != null) {
            min = mRewardsApiResponse.getPoolContestResponse().getMinParticipants();
        }
        return min;
    }

    private int getMaxParticipants() {
        int max = 0;
        if (mRewardsApiResponse != null && mRewardsApiResponse.getPoolContestResponse() != null) {
            max = mRewardsApiResponse.getPoolContestResponse().getMaxParticipants();
        }
        return max;
    }

    private double getPerUserPrizeValue() {
        double prize = 0;
        if (mRewardsApiResponse != null && mRewardsApiResponse.getPoolContestResponse() != null) {
            prize = mRewardsApiResponse.getPoolContestResponse().getPrizePerUser();
        }
        return prize;
    }

    private String getRoundingLevel() {
        String rounding = "";
        if (mRewardsApiResponse != null && mRewardsApiResponse.getPoolContestResponse() != null) {
            rounding = mRewardsApiResponse.getPoolContestResponse().getRoundingLevel();
        }
        return rounding;
    }

    @Nullable
    private List<PoolPayoutMap> getPoolPayoutMapList() {
        List<PoolPayoutMap> list = null;
        if (mRewardsApiResponse != null && mRewardsApiResponse.getPoolContestResponse() != null) {
            list = mRewardsApiResponse.getPoolContestResponse().getPoolPayoutMapList();
        }
        return list;
    }

    private int getUsersJoined() {
        int usersJoined = 0;
        if (mRewardsApiResponse != null && mRewardsApiResponse.getPoolContestResponse() != null) {
            usersJoined = mRewardsApiResponse.getPoolContestResponse().getUsersJoined();
        }
        return usersJoined;
    }

    private void showLoadingProgressBar() {
        if (getView() != null) {
            mRecyclerView.setVisibility(View.GONE);
            getView().findViewById(R.id.rewards_progress_bar_layout).setVisibility(View.VISIBLE);
        }
    }

    private void hideLoadingProgressBar() {
        if (getView() != null) {
            getView().findViewById(R.id.rewards_progress_bar_layout).setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }

}
