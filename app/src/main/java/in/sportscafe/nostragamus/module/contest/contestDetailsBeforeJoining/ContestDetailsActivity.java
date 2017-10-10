package in.sportscafe.nostragamus.module.contest.contestDetailsBeforeJoining;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;

import com.jeeva.android.Log;
import com.jeeva.android.widgets.CustomProgressbar;

import org.parceler.Parcels;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostraBaseActivity;
import in.sportscafe.nostragamus.module.contest.dto.Contest;
import in.sportscafe.nostragamus.module.contest.dto.ContestScreenData;
import in.sportscafe.nostragamus.module.contest.dto.JoinContestData;
import in.sportscafe.nostragamus.module.contest.helper.JoinContestHelper;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.lowBalance.AddMoneyOnLowBalanceActivity;
import in.sportscafe.nostragamus.module.nostraHome.ui.NostraHomeActivity;
import in.sportscafe.nostragamus.module.popups.timerPopup.TimerFinishDialogHelper;
import in.sportscafe.nostragamus.utils.FragmentHelper;

/**
 * Created by deepanshi on 9/10/17.
 */

public class ContestDetailsActivity extends NostraBaseActivity implements ContestDetailsFragmentListener {

    private static final String TAG = ContestDetailsActivity.class.getSimpleName();
    public static final int ADD_MONEY_ON_LOW_BALANCE_REQUEST_CODE = 1151;

    private ContestDetailsFragment mContestDetailsFragment;
    private ContestScreenData mContestScreenData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setShouldAnimateActivity(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contest_details);
        loadFragment();
    }

    private void loadFragment() {
        Bundle args = null;
        if (getIntent() != null && getIntent().getExtras() != null) {
            args = getIntent().getExtras();
        }

        mContestDetailsFragment = new ContestDetailsFragment();
        if (args != null) {
            if (args.containsKey(Constants.BundleKeys.CONTEST_SCREEN_DATA)) {
                mContestScreenData = Parcels.unwrap(args.getParcelable(Constants.BundleKeys.CONTEST_SCREEN_DATA));
            }

            mContestDetailsFragment.setArguments(args);
        }

        FragmentHelper.replaceFragment(this, R.id.fragment_container, mContestDetailsFragment);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        passIntentToFragment(intent);
    }

    private void passIntentToFragment(Intent intent) {
        if (intent != null && mContestDetailsFragment != null) {
            mContestDetailsFragment.onNewIntent(intent);
        }
    }

    @Override
    public void onJoinContestClicked(Bundle args) {
        performJoinContest(args);
    }

    @Override
    public void onBackBtnClicked() {
        onBackPressed();
    }

    private void performJoinContest(Bundle args) {
        if (args != null) {
            JoinContestData joinContestData = null;

            /* Received contest when join button clicked on contest card */
            if (args.containsKey(Constants.BundleKeys.CONTEST)) {
                final Contest contest = Parcels.unwrap(args.getParcelable(Constants.BundleKeys.CONTEST));
                if (contest != null) {
                    joinContestData = new JoinContestData();
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

            if (joinContestData != null) {

                if (TimerFinishDialogHelper.canJoinContest( (mContestScreenData != null) ? mContestScreenData.getChallengeStartTime() : "")) {

                    if (Nostragamus.getInstance().hasNetworkConnection()) {
                        CustomProgressbar.getProgressbar(this).show();

                        JoinContestHelper joinContestHelper = new JoinContestHelper();
                        joinContestHelper.JoinContest(joinContestData, this,
                                new JoinContestHelper.JoinContestProcessListener() {
                                    @Override
                                    public void noInternet() {
                                        CustomProgressbar.getProgressbar(ContestDetailsActivity.this).dismissProgress();
                                        handleError(Constants.DataStatus.NO_INTERNET, "");
                                    }

                                    @Override
                                    public void lowWalletBalance(JoinContestData joinContestData) {
                                        CustomProgressbar.getProgressbar(ContestDetailsActivity.this).dismissProgress();
                                        launchLowBalanceActivity(joinContestData);
                                    }

                                    @Override
                                    public void joinContestSuccess(JoinContestData contestJoinedSuccessfully) {
                                        CustomProgressbar.getProgressbar(ContestDetailsActivity.this).dismissProgress();
                                        onContestJoinedSuccessfully(contestJoinedSuccessfully);
                                    }

                                    @Override
                                    public void onApiFailure() {
                                        CustomProgressbar.getProgressbar(ContestDetailsActivity.this).dismissProgress();
                                        handleError(Constants.DataStatus.FROM_SERVER_API_FAILED, "");
                                    }

                                    @Override
                                    public void onServerReturnedError(String msg) {
                                        CustomProgressbar.getProgressbar(ContestDetailsActivity.this).dismissProgress();
                                        if (TextUtils.isEmpty(msg)) {
                                            msg = Constants.Alerts.SOMETHING_WRONG;
                                        }
                                        handleError(-1, msg);
                                    }

                                    @Override
                                    public void hideProgressBar() {
                                        CustomProgressbar.getProgressbar(ContestDetailsActivity.this).dismissProgress();
                                    }

                                    @Override
                                    public void showProgressBar() {
                                        CustomProgressbar.getProgressbar(ContestDetailsActivity.this).show();
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
                        performJoinContest(data.getExtras());
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

    private void handleError(int status, String msg) {
        if (!this.isFinishing()) {
            View view = findViewById(R.id.contest_details_before_join_activity_parent);

            if (!TextUtils.isEmpty(msg)) {
                Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show();
            } else {
                Snackbar.make(view, Constants.Alerts.SOMETHING_WRONG, Snackbar.LENGTH_LONG).show();
            }
        }
    }


}