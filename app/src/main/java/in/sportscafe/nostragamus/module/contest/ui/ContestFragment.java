package in.sportscafe.nostragamus.module.contest.ui;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeeva.android.Log;
import com.jeeva.android.widgets.CustomProgressbar;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.common.NostraBaseFragment;
import in.sportscafe.nostragamus.module.contest.adapter.ContestAdapterItemType;
import in.sportscafe.nostragamus.module.contest.contestDetailsBeforeJoining.CompletePaymentDialogFragment;
import in.sportscafe.nostragamus.module.contest.dataProvider.ContestDataProvider;
import in.sportscafe.nostragamus.module.contest.dto.Contest;
import in.sportscafe.nostragamus.module.contest.dto.ContestResponse;
import in.sportscafe.nostragamus.module.contest.dto.ContestScreenData;
import in.sportscafe.nostragamus.module.contest.dto.ContestType;
import in.sportscafe.nostragamus.module.contest.dto.JoinContestData;
import in.sportscafe.nostragamus.module.contest.helper.ContestFilterHelper;
import in.sportscafe.nostragamus.module.contest.helper.JoinContestHelper;
import in.sportscafe.nostragamus.module.contest.ui.viewPager.ContestViewPagerAdapter;
import in.sportscafe.nostragamus.module.contest.ui.viewPager.ContestViewPagerFragment;
import in.sportscafe.nostragamus.module.customViews.CustomSnackBar;
import in.sportscafe.nostragamus.module.navigation.referfriends.ReferFriendActivity;
import in.sportscafe.nostragamus.module.navigation.referfriends.ReferFriendFragmentListener;
import in.sportscafe.nostragamus.module.navigation.wallet.WalletHelper;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.lowBalance.AddMoneyOnLowBalanceActivity;
import in.sportscafe.nostragamus.module.newChallenges.helpers.DateTimeHelper;
import in.sportscafe.nostragamus.module.newChallenges.ui.matches.NewChallengesMatchesFragment;
import in.sportscafe.nostragamus.module.nostraHome.helper.TimerHelper;
import in.sportscafe.nostragamus.module.nostraHome.ui.NostraHomeActivity;
import in.sportscafe.nostragamus.module.popups.timerPopup.TimerFinishDialogHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContestFragment extends NostraBaseFragment implements View.OnClickListener {

    private static final String TAG = ContestFragment.class.getSimpleName();
    public static final int ADD_MONEY_ON_LOW_BALANCE_REQUEST_CODE = 1101;

    private ContestsFragmentListener mContestsFragmentListener;

    public ContestFragment() {
    }

    private TextView mTvTBarHeading;
    private TextView mTvTBarSubHeading;
    private TextView mTvTBarWalletMoney;
    private ContestScreenData mContestScreenData;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof ContestsActivity) {
            mContestsFragmentListener = (ContestsFragmentListener) context;
        } else {
            throw new RuntimeException("Activity must implement " + TAG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contest, container, false);
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        mTvTBarHeading = (TextView) rootView.findViewById(R.id.toolbar_heading_one);
        mTvTBarSubHeading = (TextView) rootView.findViewById(R.id.toolbar_heading_two);
        mTvTBarWalletMoney = (TextView) rootView.findViewById(R.id.toolbar_wallet_money);
        rootView.findViewById(R.id.contest_back_btn).setOnClickListener(this);
        rootView.findViewById(R.id.toolbar_icon).setOnClickListener(this);
        rootView.findViewById(R.id.toolbar_wallet_rl).setOnClickListener(this);
    }

    public void onNewIntent(Intent intent) {
        int launchFrom = intent.getIntExtra(Constants.BundleKeys.SCREEN_LAUNCH_REQUEST, -1);
        switch (launchFrom) {
            case ContestsActivity.LaunchedFrom.SELECT_PAYMENT_MODE:
                showJoinContestDialog(intent.getExtras());
                break;
        }
    }

    private void showJoinContestDialog(Bundle args) {
        if (args != null) {
            performJoinContest(args);
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initMembers();
        loadData();
    }

    private void loadData() {
        if (mContestScreenData != null) {
            showLoadingProgressBar();

            final ContestDataProvider dataProvider = new ContestDataProvider();

            dataProvider.getContestDetails(mContestScreenData.getChallengeId(),
                    new ContestDataProvider.ContestDataProviderListener() {
                        @Override
                        public void onSuccessResponse(int status, ContestResponse response) {
                            hideLoadingProgressBar();

                            switch (status) {
                                case Constants.DataStatus.FROM_SERVER_API_SUCCESS:
                                    if (response != null && response.getData() != null && response.getData().getContests() != null) {
                                        showOnUi(dataProvider.getContestTypeList(response.getData().getContests()),
                                                response.getData().getContests(), response.getData().getMaxTransferLimit());

                                    } else {
                                        handleError(-1);
                                    }
                                    break;

                                default:
                                    handleError(status);
                                    break;
                            }
                        }

                        @Override
                        public void onError(int status) {
                            hideLoadingProgressBar();
                            handleError(status);
                        }
                    });
        }
    }


    private void initMembers() {
        Bundle args = getArguments();
        if (args != null) {
            if (args.containsKey(Constants.BundleKeys.CONTEST_SCREEN_DATA)) {
                mContestScreenData = Parcels.unwrap(args.getParcelable(Constants.BundleKeys.CONTEST_SCREEN_DATA));
            }
        }
    }

    private void handleError(int status) {
        if (getView() != null && getActivity() != null && !getActivity().isFinishing()) {
            switch (status) {
                case Constants.DataStatus.NO_INTERNET:
                    CustomSnackBar.make(getView(), Constants.Alerts.NO_NETWORK_CONNECTION, CustomSnackBar.DURATION_LONG).show();
                    break;

                default:
                    CustomSnackBar.make(getView(), Constants.Alerts.SOMETHING_WRONG, CustomSnackBar.DURATION_LONG).show();
                    break;
            }
        }
    }

    private void showOnUi(List<ContestType> contestTypeList, List<Contest> contestList, int maxPowerUpTransferLimit) {
        if (getView() != null && getActivity() != null && mContestScreenData != null) {
            if (contestTypeList != null && contestTypeList.size() > 0
                    && contestList != null && contestList.size() > 0) {

                addChallengeDetailsIntoContest(contestList);
                addReferCardIntoContestList(contestList);

                TabLayout contestTabLayout = (TabLayout) getView().findViewById(R.id.contest_tabs);
                ViewPager challengesViewPager = (ViewPager) getView().findViewById(R.id.contest_viewPager);

                ArrayList<ContestViewPagerFragment> fragmentList = new ArrayList<>();
                ContestFilterHelper filterHelper = new ContestFilterHelper();
                ContestViewPagerFragment tabFragment = null;

                /* For all the tabs */
                for (int temp = 0; temp < contestTypeList.size(); temp++) {
                    ContestType contestType = contestTypeList.get(temp);
                    tabFragment = new ContestViewPagerFragment();
                    List<Contest> contestFiltered = null;

                    if (contestType.getCategoryName().equalsIgnoreCase(ContestFilterHelper.JOINED_CONTEST)) {
                        contestFiltered = filterHelper.getJoinedContests(contestList);

                    } else {
                        contestFiltered = filterHelper.getFilteredContestByType(contestType.getCategoryName(), contestList);
                    }

                    if (contestFiltered != null) {
                        int contestCount = getContestCounter(contestFiltered);
                        if (contestCount > 0) {
                            contestType.setContestCount(contestCount);
                            tabFragment.onContestData(contestFiltered, mContestScreenData);
                            tabFragment.setContestType(contestType);
                            tabFragment.setMaxPowerupTransferLimit(maxPowerUpTransferLimit);
                            fragmentList.add(tabFragment);
                        }
                    }
                }

                ContestViewPagerAdapter viewPagerAdapter = new ContestViewPagerAdapter(
                        getActivity().getSupportFragmentManager(), fragmentList);
                challengesViewPager.setAdapter(viewPagerAdapter);

                contestTabLayout.setupWithViewPager(challengesViewPager);

                for (int temp = 0; temp < contestTabLayout.getTabCount(); temp++) {
                    TabLayout.Tab tab = contestTabLayout.getTabAt(temp);
                    if (tab != null) {
                        tab.setCustomView(viewPagerAdapter.getTabView(contestTabLayout.getContext(), temp));
                    }
                }

                setValues(contestList);

            } else {
                handleError(-1);
            }
        }
    }

    private int getContestCounter(List<Contest> contestFiltered) {
        int result = 0;
        for (Contest contest : contestFiltered) {
            if (contest.isJoinable() &&
                    contest.getContestItemType() != ContestAdapterItemType.REFER_FRIEND_AD) {
                result++;
            }
        }

        return result;
    }

    private void addReferCardIntoContestList(List<Contest> contestList) {
        if (contestList.size() >= 1) {
            Contest referContest = new Contest();
            referContest.setContestItemType(ContestAdapterItemType.REFER_FRIEND_AD);
            contestList.add(referContest);
        }
    }

    private void setValues(List<Contest> contestList) {
        int contestsAvailable = getAvailableContestCount(contestList);
        if (contestsAvailable > 0) {
            mTvTBarHeading.setText(contestsAvailable + " Contests" + " - " + mContestScreenData.getChallengeName());
        }
        setTimer();
        mTvTBarWalletMoney.setText(String.valueOf((int) WalletHelper.getTotalBalance()));
    }

    private void setTimer() {
        if (mContestScreenData != null && !TextUtils.isEmpty(mContestScreenData.getChallengeStartTime())) {
            long futureTime = TimerHelper.getCountDownFutureTime(mContestScreenData.getChallengeStartTime());

            CountDownTimer countDownTimer = new CountDownTimer(futureTime, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    mTvTBarSubHeading.setText(TimerHelper.getTimerFormatFromMillis(millisUntilFinished));
                }

                @Override
                public void onFinish() {
                    onChallengeStarted();
                }
            };
            countDownTimer.start();
        }
    }

    private void onChallengeStarted() {
        if (mContestScreenData != null && !TextUtils.isEmpty(mContestScreenData.getChallengeStartTime())) {
            boolean isMatchStarted = DateTimeHelper.isMatchStarted(mContestScreenData.getChallengeStartTime());
            if (isMatchStarted) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (ContestFragment.this.isVisible()) {
                            String msg = String.format(Constants.Alerts.CHALLENGE_STARTED_ALERT_FOR_TIMER, mContestScreenData.getChallengeName());

                            TimerFinishDialogHelper.showChallengeStartedTimerOutDialog(getChildFragmentManager(), msg, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (mContestsFragmentListener != null) {
                                        mContestsFragmentListener.onBackClicked();
                                    }
                                }
                            });
                        }
                    }
                }, 500);
            }
        }
    }

    private int getAvailableContestCount(List<Contest> contestList) {
        int count = 0;

        if (contestList != null) {
            for (Contest contest : contestList) {
                if (!contest.isContestJoined() && contest.isJoinable() &&
                        contest.getContestItemType() != ContestAdapterItemType.REFER_FRIEND_AD) {
                    count++;
                }
            }
        }

        return count;
    }

    private void addChallengeDetailsIntoContest(List<Contest> contestList) {
        if (mContestScreenData != null && contestList != null) {
            for (Contest contest : contestList) {
                contest.setChallengeName(mContestScreenData.getChallengeName());
                contest.setChallengeId(mContestScreenData.getChallengeId());
                contest.setChallengeStartTime(mContestScreenData.getChallengeStartTime());
            }
        }
    }

    private void showLoadingProgressBar() {
        if (getView() != null) {
            getView().findViewById(R.id.contestsContentLayout).setVisibility(View.GONE);
            getView().findViewById(R.id.contestsProgressBarLayout).setVisibility(View.VISIBLE);
        }
    }

    private void hideLoadingProgressBar() {
        if (getView() != null) {
            getView().findViewById(R.id.contestsProgressBarLayout).setVisibility(View.GONE);
            getView().findViewById(R.id.contestsContentLayout).setVisibility(View.VISIBLE);
        }
    }

    private void performJoinContest(Bundle args) {
        if (args != null) {
            JoinContestData joinContestData = new JoinContestData();


            /* Received contest when join button clicked on contest card */
            if (args.containsKey(Constants.BundleKeys.CONTEST)) {
                final Contest contest = Parcels.unwrap(args.getParcelable(Constants.BundleKeys.CONTEST));
                if (contest != null) {
                    joinContestData.setContestId(contest.getContestId());
                    joinContestData.setChallengeId(contest.getChallengeId());
                    joinContestData.setChallengeName(contest.getChallengeName());
                    joinContestData.setEntryFee(contest.getEntryFee());
                    joinContestData.setJoiContestDialogLaunchMode(CompletePaymentDialogFragment.DialogLaunchMode.JOINING_CHALLENGE_LAUNCH);
                    joinContestData.setContestName(contest.getConfigName());

                    if (contest.getContestType() != null) {
                        joinContestData.setContestType(contest.getContestType().getCategoryName());
                    }

                }
            }

            /* Received Join-contest-data when low money added */
            if (args.containsKey(Constants.BundleKeys.JOIN_CONTEST_DATA)) {
                joinContestData = Parcels.unwrap(args.getParcelable(Constants.BundleKeys.JOIN_CONTEST_DATA));
            }

            /* For InPlay Headless flow OR pseudoGame flow, send pseudoRoomId */
            if (mContestScreenData != null &&
                    (mContestScreenData.isHeadLessFlow() || mContestScreenData.isPseudoGameFlow())) {
                joinContestData.setShouldSendPseudoRoomId(true);
                joinContestData.setPseudoRoomId(mContestScreenData.getPseudoRoomId());
            }

            if (TimerFinishDialogHelper.canJoinContest((mContestScreenData != null) ? mContestScreenData.getChallengeStartTime() : "")) {

                if (Nostragamus.getInstance().hasNetworkConnection()) {
                    CustomProgressbar.getProgressbar(getContext()).show();

                    JoinContestHelper joinContestHelper = new JoinContestHelper();
                    joinContestHelper.JoinContest(joinContestData, (AppCompatActivity) this.getActivity(),
                            new JoinContestHelper.JoinContestProcessListener() {
                                @Override
                                public void noInternet() {
                                    CustomProgressbar.getProgressbar(getContext()).dismissProgress();
                                    handleError(Constants.DataStatus.NO_INTERNET, "");
                                }

                                @Override
                                public void lowWalletBalance(JoinContestData joinContestData) {
                                    CustomProgressbar.getProgressbar(getContext()).dismissProgress();
                                    launchLowBalanceActivity(joinContestData);
                                    NostragamusAnalytics.getInstance().trackClickEvent(Constants.AnalyticsCategory.CONTEST,
                                            Constants.AnalyticsClickLabels.JOIN_CONTEST + "-" + Constants.AnalyticsClickLabels.CONTEST_LOW_MONEY);
                                }

                                @Override
                                public void joinContestSuccess(JoinContestData contestJoinedSuccessfully, String orderId) {

                                    CustomProgressbar.getProgressbar(getContext()).dismissProgress();
                                    onContestJoinedSuccessfully(contestJoinedSuccessfully);

                                    NostragamusAnalytics.getInstance().trackClickEvent(Constants.AnalyticsCategory.CONTEST_JOINED,
                                            String.valueOf(contestJoinedSuccessfully.getContestId()));

                                    if (contestJoinedSuccessfully != null) {
                                        sendContestJoinedDataToAmplitude(contestJoinedSuccessfully,orderId);
                                    }
                                }

                                @Override
                                public void onApiFailure() {
                                    CustomProgressbar.getProgressbar(getContext()).dismissProgress();
                                    handleError(Constants.DataStatus.FROM_SERVER_API_FAILED, "");
                                }

                                @Override
                                public void onServerReturnedError(String msg) {
                                    CustomProgressbar.getProgressbar(getContext()).dismissProgress();
                                    if (TextUtils.isEmpty(msg)) {
                                        msg = Constants.Alerts.SOMETHING_WRONG;
                                    }
                                    handleError(-1, msg);
                                }

                                @Override
                                public void hideProgressBar() {
                                    CustomProgressbar.getProgressbar(getContext()).dismissProgress();
                                }

                                @Override
                                public void showProgressBar() {
                                    CustomProgressbar.getProgressbar(getContext()).show();
                                }
                            });

                } else {
                    handleError(Constants.DataStatus.NO_INTERNET, "");
                }
            } else {
                TimerFinishDialogHelper.showCanNotJoinTimerOutDialog((AppCompatActivity) getActivity());
            }
        }
    }

    private void onContestJoinedSuccessfully(JoinContestData joinContestData) {
        Log.d(TAG, "Contest join successful");
        if (getView() != null && getActivity() != null && !getActivity().isFinishing()) {

            Bundle args = new Bundle();
            args.putParcelable(Constants.BundleKeys.JOIN_CONTEST_DATA, Parcels.wrap(joinContestData));

            Intent clearTaskIntent = new Intent(getContext().getApplicationContext(), NostraHomeActivity.class);
            clearTaskIntent.putExtra(Constants.BundleKeys.SCREEN_LAUNCH_REQUEST, NostraHomeActivity.LaunchedFrom.SHOW_IN_PLAY);
            clearTaskIntent.putExtras(args);
            clearTaskIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(clearTaskIntent);
        }
    }

    private void launchLowBalanceActivity(JoinContestData joinContestData) {
        if (getActivity() != null && !getActivity().isFinishing()) {
            Bundle args = new Bundle();
            args.putParcelable(Constants.BundleKeys.JOIN_CONTEST_DATA, Parcels.wrap(joinContestData));

            Intent intent = new Intent(getActivity(), AddMoneyOnLowBalanceActivity.class);
            intent.putExtras(args);
            startActivityForResult(intent, ADD_MONEY_ON_LOW_BALANCE_REQUEST_CODE);
        }
    }

    private void handleError(int status, String msg) {
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

    private void sendContestJoinedDataToAmplitude(JoinContestData contest,String orderId) {

        /* Joining a contest = Revenue */
        NostragamusAnalytics.getInstance().trackRevenue(contest.getEntryFee(), contest.getContestId(),
                contest.getContestName(), contest.getContestType(),orderId);

        /* Send Contest Joined Details to Amplitude */
        Bundle activityBundle = null;
        if (getActivity() != null) {
            if (getActivity().getIntent() != null && getActivity().getIntent().getExtras() != null) {
                activityBundle = getActivity().getIntent().getExtras();

                if (activityBundle.containsKey(Constants.BundleKeys.SCREEN_LAUNCHED_FROM_PARENT)) {

                    int screenLaunchedFrom = activityBundle.getInt(Constants.BundleKeys.SCREEN_LAUNCHED_FROM_PARENT);
                    String screenName = "Contest";

                    switch (screenLaunchedFrom) {
                        case ContestsActivity.LaunchedFrom.NEW_CHALLENGE_MATCHES:
                            screenName = "New Challenge Games";
                            break;

                        case ContestsActivity.LaunchedFrom.IN_PLAY_HEAD_LESS_MATCHES:
                            screenName = "In Play Headless Games";
                            break;

                        case ContestsActivity.LaunchedFrom.IN_PLAY_MATCHES:
                            screenName = "In Play Matches";
                            break;

                        case ContestsActivity.LaunchedFrom.PLAY_SCREEN_HEAD_LESS:
                            screenName = "Play Screen Headless";
                            break;

                        case ContestsActivity.LaunchedFrom.RESULTS:
                            screenName = "Results";
                            break;

                        case ContestsActivity.LaunchedFrom.IN_PLAY_JOIN_CONTEST:
                            screenName = "In Play Join Contest";
                            break;

                        case ContestsActivity.LaunchedFrom.SELECT_PAYMENT_MODE:
                            screenName = "Wallet Low Balance";
                            break;

                    }

                    NostragamusAnalytics.getInstance().trackContestJoined(contest.getContestId(),
                            contest.getContestName(), contest.getContestType(),
                            (int) contest.getEntryFee(), contest.getChallengeId(), screenName);
                } else {
                    NostragamusAnalytics.getInstance().trackContestJoined(contest.getContestId(),
                            contest.getContestName(), contest.getContestType(),
                            (int) contest.getEntryFee(), contest.getChallengeId(), "contest");
                }
            }
        } else {
            NostragamusAnalytics.getInstance().trackContestJoined(contest.getContestId(),
                    contest.getContestName(), contest.getContestType(),
                    (int) contest.getEntryFee(), contest.getChallengeId(), "contest");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.contest_back_btn:
                mContestsFragmentListener.onBackClicked();
                break;
            case R.id.toolbar_icon:
                mContestsFragmentListener.onContestDetailsPopUpButtonClicked();
                NostragamusAnalytics.getInstance().trackClickEvent(Constants.AnalyticsCategory.CONTEST, Constants.AnalyticsClickLabels.CONTEST_MODES_POPUP);
                break;

            case R.id.toolbar_wallet_rl:
                mContestsFragmentListener.onWalletClicked();
                NostragamusAnalytics.getInstance().trackClickEvent(Constants.AnalyticsCategory.CONTEST, Constants.AnalyticsClickLabels.WALLET);
                break;
        }
    }
}
