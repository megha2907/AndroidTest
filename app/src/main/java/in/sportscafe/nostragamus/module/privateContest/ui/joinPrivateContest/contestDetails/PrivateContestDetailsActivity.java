package in.sportscafe.nostragamus.module.privateContest.ui.joinPrivateContest.contestDetails;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jeeva.android.Log;
import com.jeeva.android.widgets.CustomProgressbar;

import org.parceler.Parcels;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.challengeRewards.RewardsLaunchedFrom;
import in.sportscafe.nostragamus.module.challengeRules.RulesFragment;
import in.sportscafe.nostragamus.module.common.NostraBaseActivity;
import in.sportscafe.nostragamus.module.contest.contestDetailsBeforeJoining.CompletePaymentDialogFragment;
import in.sportscafe.nostragamus.module.contest.contestDetailsBeforeJoining.ContestDetailsActivity;
import in.sportscafe.nostragamus.module.contest.dto.JoinContestData;
import in.sportscafe.nostragamus.module.contest.dto.bumper.BumperPrizesEstimationScreenData;
import in.sportscafe.nostragamus.module.contest.helper.JoinContestHelper;
import in.sportscafe.nostragamus.module.contest.ui.bumperContest.BumperPrizesEstimationFragment;
import in.sportscafe.nostragamus.module.customViews.CustomSnackBar;
import in.sportscafe.nostragamus.module.navigation.wallet.WalletHelper;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.lowBalance.AddMoneyOnLowBalanceActivity;
import in.sportscafe.nostragamus.module.newChallenges.helpers.DateTimeHelper;
import in.sportscafe.nostragamus.module.nostraHome.helper.TimerHelper;
import in.sportscafe.nostragamus.module.nostraHome.ui.NostraHomeActivity;
import in.sportscafe.nostragamus.module.popups.timerPopup.TimerFinishDialogHelper;
import in.sportscafe.nostragamus.module.privateContest.ui.joinPrivateContest.dto.FindPrivateContestResponseContestData;
import in.sportscafe.nostragamus.module.privateContest.ui.joinPrivateContest.dto.FindPrivateContestResponseData;
import in.sportscafe.nostragamus.module.privateContest.ui.joinPrivateContest.dto.PrivateContestDetailsScreenData;

public class PrivateContestDetailsActivity extends NostraBaseActivity implements View.OnClickListener {

    private final String TAG = PrivateContestDetailsActivity.class.getSimpleName();
    public static final int ADD_MONEY_ON_LOW_BALANCE_REQUEST_CODE = 191;

    private PrivateContestDetailsScreenData mScreenData;
    private PrivateContestDetailsViewPagerAdapter mViewPagerAdapter;

    private TextView mTimerTextView;
    private ViewPager mViewPager;
    private Button mPayAndJoinButton;

    @Override
    public String getScreenName() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setShouldAnimateActivity(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_contest_details);

        initViews();
        initMembers();
        showContestDetails();
        loadViewPagerFragments();
    }

    private void initMembers() {
        if (getIntent() != null && getIntent().getExtras() != null) {
            if (getIntent().getExtras().containsKey(Constants.BundleKeys.PRIVATE_CONTEST_DETAILS_SCREEN_DATA)) {
                mScreenData = Parcels.unwrap(getIntent().getExtras()
                        .getParcelable(Constants.BundleKeys.PRIVATE_CONTEST_DETAILS_SCREEN_DATA));
            }
        }
    }

    private void initViews() {
        mTimerTextView = (TextView) findViewById(R.id.toolbar_heading_two);
        mViewPager = (ViewPager) findViewById(R.id.private_contest_detail_viewpager);
        mPayAndJoinButton = (Button) findViewById(R.id.join_contest_btn);

        findViewById(R.id.back_btn).setOnClickListener(this);
        mPayAndJoinButton.setOnClickListener(this);
    }

    private void showContestDetails() {
        if (mScreenData != null) {

            /* Header */
            TextView walletAmtTextView = (TextView) findViewById(R.id.toolbar_wallet_money);
            walletAmtTextView.setText(WalletHelper.getFormattedStringOfAmount(WalletHelper.getTotalBalance()));

            try {
                TextView contestNameTextView = (TextView) findViewById(R.id.toolbar_heading_one);
                contestNameTextView.setText(mScreenData.getPrivateContestDetailsResponse().getData().getPrivateContestData().get(0).getConfigName());
            } catch (Exception e) {}

            setTimer();

            /* Card values */
            showContestCardValues();
        }
    }

    private void showContestCardValues() {
        TextView challengeNameTextView = (TextView) findViewById(R.id.private_contest_challenge_name_textView);
        TextView contestTemplateTextView = (TextView) findViewById(R.id.private_contest_prize_template_textView);
        TextView prizeTextView = (TextView) findViewById(R.id.private_contest_prizes_textView);
        TextView maxEntriesTextView = (TextView) findViewById(R.id.private_contest_max_entries_textView);
        TextView entryFeeTextView = (TextView) findViewById(R.id.private_contest_entry_Fee_textView);

        if (mScreenData != null && mScreenData.getPrivateContestDetailsResponse() != null &&
                mScreenData.getPrivateContestDetailsResponse().getData() != null) {
            FindPrivateContestResponseData challengeData = mScreenData.getPrivateContestDetailsResponse().getData();

            if (!TextUtils.isEmpty(challengeData.getChallengeName())) {
                challengeNameTextView.setText(challengeData.getChallengeName());
            }

            if (challengeData.getPrivateContestData() != null && challengeData.getPrivateContestData().size() > 0) {
                FindPrivateContestResponseContestData contestData = challengeData.getPrivateContestData().get(0);

                prizeTextView.setText(WalletHelper.getFormattedStringOfAmount(contestData.getPrizeMoney()));
                maxEntriesTextView.setText(String.valueOf(contestData.getMaxParticipants()));
                entryFeeTextView.setText(WalletHelper.getFormattedStringOfAmount(contestData.getFee()));
            }
        }
    }

    private void setTimer() {
        if (mScreenData != null && mScreenData.getPrivateContestDetailsResponse() != null
                && mScreenData.getPrivateContestDetailsResponse().getData() != null
                && !TextUtils.isEmpty(mScreenData.getPrivateContestDetailsResponse().getData().getChallengeStarttime())) {

            final String challengeStartTime = mScreenData.getPrivateContestDetailsResponse().getData().getChallengeStarttime();
            long futureTime = TimerHelper.getCountDownFutureTime(challengeStartTime);

            CountDownTimer countDownTimer = new CountDownTimer(futureTime, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    mTimerTextView.setText(TimerHelper.getTimerFormatFromMillis(millisUntilFinished));
                }

                @Override
                public void onFinish() {
                    onMatchStarted(challengeStartTime);
                }
            };
            countDownTimer.start();
        }
    }

    private void onMatchStarted(String challengeStartTime) {
        if (mScreenData != null && !TextUtils.isEmpty(challengeStartTime)) {
            boolean isMatchStarted = DateTimeHelper.isMatchStarted(challengeStartTime);
            if (isMatchStarted) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!isFinishing() && mScreenData.getPrivateContestDetailsResponse() != null &&
                                mScreenData.getPrivateContestDetailsResponse().getData() != null) {

                            String msg = String.format(Constants.Alerts.CHALLENGE_STARTED_ALERT_FOR_TIMER,
                                    mScreenData.getPrivateContestDetailsResponse().getData().getChallengeName());

                            TimerFinishDialogHelper.showChallengeStartedTimerOutDialog(getSupportFragmentManager(), msg,
                                    new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finish();
                                }
                            });
                        }
                    }
                }, 500);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.back_btn:
                finish();
                break;

            case R.id.join_contest_btn:
                onJoinButtonClicked();
                break;

        }
    }

    private void onJoinButtonClicked() {
        JoinContestData joinContestData = getJoinContestData();
        if (joinContestData != null) {

            String startTime = (getPrivateChallengeData() != null) ? getPrivateChallengeData().getChallengeStarttime() : "";

            if (TimerFinishDialogHelper.canJoinContest(startTime)) {

                if (Nostragamus.getInstance().hasNetworkConnection()) {
                    CustomProgressbar.getProgressbar(this).show();

                    JoinContestHelper joinContestHelper = new JoinContestHelper();
                    joinContestHelper.JoinContest(joinContestData, this,
                            new JoinContestHelper.JoinContestProcessListener() {
                                @Override
                                public void noInternet() {
                                    CustomProgressbar.getProgressbar(PrivateContestDetailsActivity.this).dismissProgress();
                                    handleError(Constants.DataStatus.NO_INTERNET, "");
                                }

                                @Override
                                public void lowWalletBalance(JoinContestData joinContestData) {
                                    CustomProgressbar.getProgressbar(PrivateContestDetailsActivity.this).dismissProgress();
                                    launchLowBalanceActivity(joinContestData);
                                    NostragamusAnalytics.getInstance().trackClickEvent(Constants.AnalyticsCategory.CONTEST,
                                            Constants.AnalyticsClickLabels.JOIN_CONTEST + "-" + Constants.AnalyticsClickLabels.CONTEST_LOW_MONEY);
                                }

                                @Override
                                public void joinContestSuccess(JoinContestData contestJoinedSuccessfully) {
                                    CustomProgressbar.getProgressbar(PrivateContestDetailsActivity.this).dismissProgress();
                                    onContestJoinedSuccessfully(contestJoinedSuccessfully);

                                    NostragamusAnalytics.getInstance().trackClickEvent(Constants.AnalyticsCategory.CONTEST_JOINED,
                                            String.valueOf(contestJoinedSuccessfully.getContestId()));

                                    if (contestJoinedSuccessfully != null) {
                                        sendContestJoinedDataToAmplitude(contestJoinedSuccessfully);
                                    }
                                }

                                @Override
                                public void onApiFailure() {
                                    CustomProgressbar.getProgressbar(PrivateContestDetailsActivity.this).dismissProgress();
                                    handleError(Constants.DataStatus.FROM_SERVER_API_FAILED, "");
                                }

                                @Override
                                public void onServerReturnedError(String msg) {
                                    CustomProgressbar.getProgressbar(PrivateContestDetailsActivity.this).dismissProgress();
                                    if (TextUtils.isEmpty(msg)) {
                                        msg = Constants.Alerts.SOMETHING_WRONG;
                                    }
                                    handleError(-1, msg);
                                }

                                @Override
                                public void hideProgressBar() {
                                    CustomProgressbar.getProgressbar(PrivateContestDetailsActivity.this).dismissProgress();
                                }

                                @Override
                                public void showProgressBar() {
                                    CustomProgressbar.getProgressbar(PrivateContestDetailsActivity.this).show();
                                }
                            });

                } else {
                    handleError(Constants.DataStatus.NO_INTERNET, "");
                }
            } else {
                TimerFinishDialogHelper.showCanNotJoinTimerOutDialog(this);
            }

        } else {
            handleError(-1, "");
        }
    }

    private void launchLowBalanceActivity(JoinContestData joinContestData) {
        if (!this.isFinishing()) {
            Bundle args = new Bundle();
            args.putParcelable(Constants.BundleKeys.JOIN_CONTEST_DATA, Parcels.wrap(joinContestData));

            Intent intent = new Intent(this, AddMoneyOnLowBalanceActivity.class);
            intent.putExtras(args);
            startActivityForResult(intent, ADD_MONEY_ON_LOW_BALANCE_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {

                case ADD_MONEY_ON_LOW_BALANCE_REQUEST_CODE:
                    if (data != null && data.getExtras() != null) {
                        onJoinButtonClicked();
                    }
                    break;
            }
        }
    }

    private void onContestJoinedSuccessfully(JoinContestData joinContestData) {
        Log.d(TAG, "Contest join successful");
        if (!isFinishing()) {

            Bundle args = new Bundle();
            args.putParcelable(Constants.BundleKeys.JOIN_CONTEST_DATA, Parcels.wrap(joinContestData));

            Intent clearTaskIntent = new Intent(getApplicationContext(), NostraHomeActivity.class);
            clearTaskIntent.putExtra(Constants.BundleKeys.SCREEN_LAUNCH_REQUEST, NostraHomeActivity.LaunchedFrom.SHOW_IN_PLAY);
            clearTaskIntent.putExtras(args);
            clearTaskIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(clearTaskIntent);
        }
    }

    private JoinContestData getJoinContestData() {
        JoinContestData joinContestData = null;

        FindPrivateContestResponseContestData contestData = getPrivateContest();
        FindPrivateContestResponseData challengeData = getPrivateChallengeData();

        if (contestData != null && challengeData != null) {
            joinContestData = new JoinContestData();
            joinContestData.setContestId(contestData.getConfigId());
            joinContestData.setChallengeId(challengeData.getChallengeId());
            joinContestData.setChallengeName(challengeData.getChallengeName());
            joinContestData.setEntryFee(contestData.getFee());
            joinContestData.setJoiContestDialogLaunchMode(CompletePaymentDialogFragment.DialogLaunchMode.JOINING_CHALLENGE_LAUNCH);
            joinContestData.setContestName(contestData.getConfigName());
            joinContestData.setContestType(contestData.getContestType());
        }

        return joinContestData;
    }

    private void handleError(int status, String msg) {
        if (!isFinishing()) {
            View view = findViewById(R.id.private_contest_details_root);

            if (!TextUtils.isEmpty(msg)) {
                CustomSnackBar.make(view, msg, CustomSnackBar.DURATION_LONG).show();

            } else {
                switch (status) {
                    case Constants.DataStatus.NO_INTERNET:
                        CustomSnackBar.make(view, Constants.Alerts.NO_INTERNET_CONNECTION, CustomSnackBar.DURATION_LONG).show();
                        break;

                    default:
                        CustomSnackBar.make(view, Constants.Alerts.SOMETHING_WRONG, CustomSnackBar.DURATION_LONG).show();
                        break;
                }
            }
        }
    }

    private void sendContestJoinedDataToAmplitude(JoinContestData contest) {

        /* Joining a contest = Revenue */
        NostragamusAnalytics.getInstance().trackRevenue(contest.getEntryFee(), contest.getContestId(),
                contest.getContestName(), contest.getContestType());

        /* Send Contest Joined Details to Amplitude */
        NostragamusAnalytics.getInstance().trackContestJoined(contest.getContestId(),
                contest.getContestName(), contest.getContestType(),
                (int) contest.getEntryFee(), contest.getChallengeId(), "Private Contest Details - Join Contest");

    }


    private void loadViewPagerFragments() {
        if (!isFinishing()) {
            mViewPagerAdapter = new PrivateContestDetailsViewPagerAdapter(getSupportFragmentManager(), this);

            mViewPagerAdapter.addFragment(getMatchesFragment(), Constants.ContestDetailsTabs.MATCHES);
            mViewPagerAdapter.addFragment(getEntriesFragment(), Constants.ContestDetailsTabs.ENTRIES);
            mViewPagerAdapter.addFragment(getPrizeFragments(), Constants.ContestDetailsTabs.PRIZES);
            mViewPagerAdapter.addFragment(getRulesFragment(), Constants.ContestDetailsTabs.RULES);

            mViewPager.setAdapter(mViewPagerAdapter);
            mViewPager.setOffscreenPageLimit(3);
            setTabLayout(mViewPager);
        }
    }

    private RulesFragment getRulesFragment() {
        int contestId = 0;
        FindPrivateContestResponseContestData contestData = getPrivateContest();
        if (contestData != null) {
            contestId = contestData.getConfigId();
        }
        return  RulesFragment.newInstance(contestId);
    }

    private BumperPrizesEstimationFragment getPrizeFragments() {
        Bundle args = new Bundle();
        FindPrivateContestResponseContestData contestData = getPrivateContest();

        /*if (contestData != null) {
            RewardScreenData rewardScreenData = new RewardScreenData();
            rewardScreenData.setRoomId(-1);
            rewardScreenData.setConfigId(contestData.getConfigId());
            rewardScreenData.setContestName(contestData.getConfigName());
            rewardScreenData.setPoolContest(false);

            args.putParcelable(Constants.BundleKeys.REWARDS_SCREEN_DATA, Parcels.wrap(rewardScreenData));
        }

        RewardsFragment fragment = new RewardsFragment();
        fragment.setArguments(args);*/

        BumperPrizesEstimationScreenData screenData = new BumperPrizesEstimationScreenData();
        if (contestData != null) {
            screenData.setRewardScreenLauncherParent(RewardsLaunchedFrom.PRIVATE_CONTEST_DETAILS);
            screenData.setRoomId(-1);
            screenData.setConfigId(contestData.getConfigId());
            screenData.setContestName(contestData.getConfigName());
        }
        args.putParcelable(Constants.BundleKeys.BUMPER_PRIZE_ESTIMATION_SCREEN_DATA, Parcels.wrap(screenData));

        BumperPrizesEstimationFragment fragment = new BumperPrizesEstimationFragment();
        fragment.setArguments(args);

        return fragment;
    }

    private Fragment getEntriesFragment() {
        PrivateContestEntriesFragment entriesFragment = new PrivateContestEntriesFragment();
        entriesFragment.setScreenData(mScreenData);

        if (getIntent() != null && getIntent().getExtras() != null) {
            entriesFragment.setArguments(getIntent().getExtras());
        }
        return entriesFragment;
    }

    private Fragment getMatchesFragment() {
        PrivateContestMatchesFragment matchesFragment = new PrivateContestMatchesFragment();
        matchesFragment.setScreenData(mScreenData);

        if (getIntent() != null && getIntent().getExtras() != null) {
            matchesFragment.setArguments(getIntent().getExtras());
        }
        return matchesFragment;
    }

    private FindPrivateContestResponseData getPrivateChallengeData() {
        if (mScreenData != null && mScreenData.getPrivateContestDetailsResponse() != null &&
                mScreenData.getPrivateContestDetailsResponse().getData() != null) {
            return mScreenData.getPrivateContestDetailsResponse().getData();
        }
        return null;
    }

    private FindPrivateContestResponseContestData getPrivateContest() {
        if (mScreenData != null && mScreenData.getPrivateContestDetailsResponse() != null &&
                mScreenData.getPrivateContestDetailsResponse().getData() != null &&
                mScreenData.getPrivateContestDetailsResponse().getData().getPrivateContestData() != null &&
                mScreenData.getPrivateContestDetailsResponse().getData().getPrivateContestData().size() > 0) {
            return mScreenData.getPrivateContestDetailsResponse().getData().getPrivateContestData().get(0);
        }
        return null;
    }

    private void setTabLayout(ViewPager viewPager) {
        if (!isFinishing()) {
            TabLayout contestTabLayout = (TabLayout) findViewById(R.id.contest_details_tabs);
            contestTabLayout.setupWithViewPager(viewPager);

            if (mViewPagerAdapter != null) {
                for (int temp = 0; temp < contestTabLayout.getTabCount(); temp++) {
                    TabLayout.Tab tab = contestTabLayout.getTabAt(temp);
                    if (tab != null) {
                        tab.setCustomView(mViewPagerAdapter.getTabView(temp));
                    }
                }
            }
        }
    }
}
