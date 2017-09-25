package in.sportscafe.nostragamus.module.contest.ui.viewPager;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import org.parceler.Parcels;

import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.allchallenges.join.CompletePaymentDialogFragment;
import in.sportscafe.nostragamus.module.common.NostraBaseFragment;
import in.sportscafe.nostragamus.module.contest.adapter.ContestAdapterListener;
import in.sportscafe.nostragamus.module.contest.adapter.ContestRecyclerAdapter;
import in.sportscafe.nostragamus.module.contest.contestDetailsBeforeJoining.ContestDetailsBeforeJoinedActivity;
import in.sportscafe.nostragamus.module.contest.dto.Contest;
import in.sportscafe.nostragamus.module.contest.dto.ContestType;
import in.sportscafe.nostragamus.module.contest.dto.JoinContestData;
import in.sportscafe.nostragamus.module.contest.helper.JoinContestHelper;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.lowBalance.AddMoneyOnLowBalanceActivity;
import in.sportscafe.nostragamus.utils.AlertsHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContestViewPagerFragment extends NostraBaseFragment {

    private static final String TAG = ContestViewPagerFragment.class.getSimpleName();
    public static final int ADD_MONEY_ON_LOW_BALANCE_REQUEST_CODE = 1101;

    private RecyclerView mRecyclerView;
    private ContestType contestType;
    private List<Contest> mContestList;
    private TextView mTvContestName;
    private TextView mTvContestDesc;

    private ProgressDialog mProgressDialog;

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
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initMembers();
        populateDataOnUi();
    }

    private void initMembers() {
        mProgressDialog = new ProgressDialog(getContext());
    }

    private void populateDataOnUi() {
        if (mRecyclerView != null && mContestList != null) {
            mRecyclerView.setAdapter(new ContestRecyclerAdapter(mRecyclerView.getContext(), mContestList,
                    getContestAdapterListener()));
            mTvContestName.setText(getContestType().getName());
            mTvContestDesc.setText(getContestType().getTagLine());
        }
    }

    private ContestAdapterListener getContestAdapterListener() {
        return new ContestAdapterListener() {
            @Override
            public void onContestClicked(Bundle args) {
                goToContestDetails(args);
            }

            @Override
            public void onJoinContestClicked(Bundle args) {
                performJoinContest(args);
            }
        };
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

            if (joinContestData != null) {
                if (Nostragamus.getInstance().hasNetworkConnection()) {
                    mProgressDialog.show();

                    JoinContestHelper joinContestHelper = new JoinContestHelper();
                    joinContestHelper.JoinContest(joinContestData, (AppCompatActivity) this.getActivity(),
                            new JoinContestHelper.JoinContestProcessListener() {
                                @Override
                                public void noInternet() {
                                    mProgressDialog.dismiss();
                                    handleError(Constants.DataStatus.NO_INTERNET);
                                }

                                @Override
                                public void lowWalletBalance(JoinContestData joinContestData) {
                                    mProgressDialog.dismiss();
                                    launchLowBalanceActivity(joinContestData);
                                }

                                @Override
                                public void joinContestSuccess() {
                                    mProgressDialog.dismiss();
                                    onContestJoinedSuccessfully();
                                }

                                @Override
                                public void onUnExpectedError() {
                                    mProgressDialog.dismiss();
                                    handleError(-1);
                                }

                                @Override
                                public void onApiFailure() {
                                    mProgressDialog.dismiss();
                                    handleError(Constants.DataStatus.FROM_SERVER_API_FAILED);
                                }

                                @Override
                                public void onServerReturnedError(String msg) {
                                    mProgressDialog.dismiss();
                                    if (TextUtils.isEmpty(msg)) {
                                        msg = Constants.Alerts.SOMETHING_WRONG;
                                    }
                                    AlertsHelper.showAlert(getContext(), "Error!", msg, null);
                                }
                            });

                } else {
                    handleError(Constants.DataStatus.NO_INTERNET);
                }
            } else {
                handleError(-1);
            }
        }
    }

    private void onContestJoinedSuccessfully() {
        Log.d(TAG, "Contest join successful");
    }

    private void launchLowBalanceActivity(JoinContestData joinContestData) {
        Bundle args = new Bundle();
        args.putParcelable(Constants.BundleKeys.JOIN_CONTEST_DATA, Parcels.wrap(joinContestData));

        Intent intent = new Intent(getActivity(), AddMoneyOnLowBalanceActivity.class);
        intent.putExtras(args);
        if (getActivity() != null) {
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

    private void handleError(int status) {
        switch (status) {
            case Constants.DataStatus.NO_INTERNET:
                AlertsHelper.showAlert(getContext(), "No Internet!", "Turn ON internet to continue", null);
                break;

            default:
                AlertsHelper.showAlert(getContext(), "Error!", Constants.Alerts.SOMETHING_WRONG, null);
                break;
        }
    }

    private void goToContestDetails(Bundle args) {
        if (getActivity() != null && !getActivity().isFinishing()) {
            Intent intent = new Intent(getActivity(), ContestDetailsBeforeJoinedActivity.class);
            intent.putExtras(args);
            getActivity().startActivity(intent);
        }
    }

    public ContestType getContestType() {
        return contestType;
    }

    public void setContestType(ContestType contestType) {
        this.contestType = contestType;
    }

    public void onContestData(List<Contest> contests) {
        mContestList = contests;
    }
}
