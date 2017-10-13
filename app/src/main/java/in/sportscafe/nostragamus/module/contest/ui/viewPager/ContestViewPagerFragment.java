package in.sportscafe.nostragamus.module.contest.ui.viewPager;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeeva.android.Log;
import com.jeeva.android.widgets.CustomProgressbar;

import org.parceler.Parcels;

import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostraBaseFragment;
import in.sportscafe.nostragamus.module.contest.adapter.ContestAdapterItemType;
import in.sportscafe.nostragamus.module.contest.adapter.ContestAdapterListener;
import in.sportscafe.nostragamus.module.contest.adapter.ContestRecyclerAdapter;
import in.sportscafe.nostragamus.module.contest.contestDetailsBeforeJoining.CompletePaymentDialogFragment;
import in.sportscafe.nostragamus.module.contest.contestDetailsBeforeJoining.ContestDetailsActivity;
import in.sportscafe.nostragamus.module.contest.dto.Contest;
import in.sportscafe.nostragamus.module.contest.dto.ContestScreenData;
import in.sportscafe.nostragamus.module.contest.dto.ContestType;
import in.sportscafe.nostragamus.module.contest.dto.JoinContestData;
import in.sportscafe.nostragamus.module.contest.helper.JoinContestHelper;
import in.sportscafe.nostragamus.module.contest.ui.DetailScreensLaunchRequest;
import in.sportscafe.nostragamus.module.navigation.referfriends.ReferFriendActivity;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.lowBalance.AddMoneyOnLowBalanceActivity;
import in.sportscafe.nostragamus.module.nostraHome.helper.TimerHelper;
import in.sportscafe.nostragamus.module.nostraHome.ui.NostraHomeActivity;
import in.sportscafe.nostragamus.module.popups.timerPopup.TimerFinishDialogHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContestViewPagerFragment extends NostraBaseFragment {

    private static final String TAG = ContestViewPagerFragment.class.getSimpleName();
    public static final int ADD_MONEY_ON_LOW_BALANCE_REQUEST_CODE = 1101;

    private RecyclerView mRecyclerView;
    private ContestType mContestType;
    private List<Contest> mContestList;
    private TextView mTvContestName;
    private TextView mTvContestDesc;
    private TextView mTimerTextView;
    private ContestScreenData mContestScreenData;

    public ContestViewPagerFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contest_view_pager, container, false);
        initRoot(rootView);
        return rootView;
    }

    private void initRoot(View rootView) {
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.contest_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);

        mTvContestName = (TextView) rootView.findViewById(R.id.contest_name);
        mTvContestDesc = (TextView) rootView.findViewById(R.id.contest_desc);
        mTimerTextView = (TextView) rootView.findViewById(R.id.contest_timer_textView);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initValues();
        setTimer();
        populateDataOnUi();
    }

    private void initValues() {
        if (mContestType != null) {
            mTvContestName.setText(mContestType.getCategoryName());
            mTvContestDesc.setText(mContestType.getCategoryDesc());
        }
    }

    private void populateDataOnUi() {
        if (mContestList != null && !mContestList.isEmpty()) {
            mRecyclerView.setAdapter(new ContestRecyclerAdapter(mRecyclerView.getContext(),
                    getContestListPreparedForAdapterItemTypes(mContestList),
                    getContestAdapterListener()));

        } else {
            showEmptyListMsg();
        }
    }

    private void showEmptyListMsg() {
        if (getView() != null) {
            mRecyclerView.setVisibility(View.GONE);
            TextView emptyTextView = (TextView)  getView().findViewById(R.id.empty_list_textView);
            emptyTextView.setText("You haven't joined any contest yet!");   // As contest-tabs are created dynamically, this is possible only in 'Joined-Contest' tab
            emptyTextView.setVisibility(View.VISIBLE);
        }
    }

    private void setTimer() {
        if (mContestScreenData != null && !TextUtils.isEmpty(mContestScreenData.getChallengeStartTime())) {
            long futureTime = TimerHelper.getCountDownFutureTime(mContestScreenData.getChallengeStartTime());

            CountDownTimer countDownTimer = new CountDownTimer(futureTime, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    mTimerTextView.setText(TimerHelper.getTimerFormatFromMillis(millisUntilFinished));
                }

                @Override
                public void onFinish() {

                }
            };
            countDownTimer.start();
        }
    }

    private List<Contest> getContestListPreparedForAdapterItemTypes(List<Contest> contestList) {
        if (contestList != null) {
            for (Contest contest : contestList) {
                if (contest.isContestJoined()) {
                    contest.setContestItemType(ContestAdapterItemType.JOINED_CONTEST);
                }
            }

            if (contestList.size() >= 1) {
                Contest referContest = new Contest();
                referContest.setContestItemType(ContestAdapterItemType.REFER_FRIEND_AD);
                contestList.add(referContest);
            }
        }

        return contestList;
    }

    private ContestAdapterListener getContestAdapterListener() {
        return new ContestAdapterListener() {
            @Override
            public void onContestClicked(Bundle args) {
                goToContestDetails(args, DetailScreensLaunchRequest.CONTESTS_DEFAULT_SCREEN);
            }

            @Override
            public void onJoinContestClicked(Bundle args) {
                performJoinContest(args);
            }

            @Override
            public void onPrizesClicked(Bundle args) {
                goToContestDetails(args, DetailScreensLaunchRequest.CONTESTS_REWARDS_SCREEN);
            }

            @Override
            public void onRulesClicked(Bundle args) {
                goToContestDetails(args, DetailScreensLaunchRequest.CONTESTS_RULES_SCREEN);
            }

            @Override
            public void onEntriesClicked(Bundle args) {
                goToContestDetails(args, DetailScreensLaunchRequest.CONTESTS_DEFAULT_SCREEN);
            }

            @Override
            public void onReferAFriendClicked() {
                goToReferAFriendScreen();
            }
        };
    }

    private void goToReferAFriendScreen() {
        Intent intent = new Intent(getActivity(), ReferFriendActivity.class);
        getActivity().startActivity(intent);
    }

    private void performJoinContest(Bundle args) {
        if (args != null) {
            JoinContestData joinContestData = new JoinContestData();;

            /* Received contest when join button clicked on contest card */
            if (args.containsKey(Constants.BundleKeys.CONTEST)) {
                final Contest contest = Parcels.unwrap(args.getParcelable(Constants.BundleKeys.CONTEST));
                if (contest != null) {
                    joinContestData.setContestId(contest.getContestId());
                    joinContestData.setChallengeId(contest.getChallengeId());
                    joinContestData.setChallengeName(contest.getChallengeName());
                    joinContestData.setEntryFee(contest.getEntryFee());
                    joinContestData.setJoiContestDialogLaunchMode(CompletePaymentDialogFragment.DialogLaunchMode.JOINING_CHALLENGE_LAUNCH);
                }
            }

            /* Received Join-contest-data when low money added */
            if (args.containsKey(Constants.BundleKeys.JOIN_CONTEST_DATA)) {
                joinContestData = Parcels.unwrap(args.getParcelable(Constants.BundleKeys.JOIN_CONTEST_DATA));
            }

            /* For InPlay Headless flow, send pseudoRoomId */
            if (mContestScreenData != null && mContestScreenData.isHeadLessFlow()) {
                joinContestData.setShouldSendPseudoRoomId(true);
                joinContestData.setPseudoRoomId(mContestScreenData.getPseudoRoomId());
            }

            if (TimerFinishDialogHelper.canJoinContest( (mContestScreenData != null) ? mContestScreenData.getChallengeStartTime() : "")) {

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
                                }

                                @Override
                                public void joinContestSuccess(JoinContestData contestJoinedSuccessfully) {
                                    CustomProgressbar.getProgressbar(getContext()).dismissProgress();
                                    onContestJoinedSuccessfully(contestJoinedSuccessfully);
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
            getActivity().startActivityForResult(intent, ADD_MONEY_ON_LOW_BALANCE_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {

                case ADD_MONEY_ON_LOW_BALANCE_REQUEST_CODE:
                    if (data != null && data.getExtras() != null) {
                        performJoinContest(data.getExtras());
                    }
                    break;
            }
        }
    }

    private void handleError(int status, String msg) {
        if (getView() != null && getActivity() != null && !getActivity().isFinishing()) {
            if (!TextUtils.isEmpty(msg)) {
                Snackbar.make(getView(), msg, Snackbar.LENGTH_LONG).show();
            } else {
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
    }

    private void goToContestDetails(Bundle args, int screenLaunchRequest) {
        if (getActivity() != null && !getActivity().isFinishing()) {
            if (args == null) {
                args = new Bundle();
            }
            args.putInt(Constants.BundleKeys.SCREEN_LAUNCH_REQUEST, screenLaunchRequest);
            args.putParcelable(Constants.BundleKeys.CONTEST_SCREEN_DATA, Parcels.wrap(mContestScreenData));

            Intent intent = new Intent(getActivity(), ContestDetailsActivity.class);
            intent.putExtras(args);
            getActivity().startActivity(intent);
        }
    }

    public ContestType getContestType() {
        return mContestType;
    }

    public void setContestType(ContestType contestType) {
        this.mContestType = contestType;
    }

    public void onContestData(List<Contest> contests, ContestScreenData contestScreenData) {
        mContestList = contests;
        mContestScreenData = contestScreenData;
    }
}
