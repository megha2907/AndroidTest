package in.sportscafe.nostragamus.module.privateContest.ui.joinPrivateContest.findContest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jeeva.android.BaseFragment;
import com.jeeva.android.widgets.CustomProgressbar;
import com.jeeva.android.widgets.HmImageView;

import org.parceler.Parcels;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.customViews.CustomSnackBar;
import in.sportscafe.nostragamus.module.navigation.wallet.WalletHelper;
import in.sportscafe.nostragamus.module.popups.timerPopup.TimerFinishDialogHelper;
import in.sportscafe.nostragamus.module.privateContest.dataProvider.PrivateContestDetailsApiModelImpl;
import in.sportscafe.nostragamus.module.privateContest.ui.joinPrivateContest.contestDetails.PrivateContestDetailsActivity;
import in.sportscafe.nostragamus.module.privateContest.ui.joinPrivateContest.dto.FindPrivateContestResponse;
import in.sportscafe.nostragamus.module.privateContest.ui.joinPrivateContest.dto.JoinPrivateContestWithInviteCodeScreenData;
import in.sportscafe.nostragamus.module.privateContest.ui.joinPrivateContest.dto.PrivateContestDetailsScreenData;
import in.sportscafe.nostragamus.utils.CodeSnippet;

/**
 * A simple {@link Fragment} subclass.
 */
public class JoinPrivateContestWithInviteCodeFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = JoinPrivateContestWithInviteCodeFragment.class.getSimpleName();

    private JoinPrivateContestWithInviteCodeFragmentListener mFragmentListener;
    private JoinPrivateContestWithInviteCodeScreenData mScreenData;

    private EditText mInviteCodeEditText;
    private Button mFindContestButton;
    private TextView mInviteCodeErrorTextView;

    public JoinPrivateContestWithInviteCodeFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof JoinPrivateContestWithInviteCodeFragmentListener) {
            mFragmentListener = (JoinPrivateContestWithInviteCodeFragmentListener) context;
        } else {
            throw new RuntimeException(TAG + "Should implement " +
                    JoinPrivateContestWithInviteCodeFragmentListener.class.getSimpleName());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_join_private_contest_with_invite_code, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        mInviteCodeEditText = (EditText) view.findViewById(R.id.join_contest_private_code_editText);
        mFindContestButton = (Button) view.findViewById(R.id.find_pvt_contest_details_btn);
        mInviteCodeErrorTextView = (TextView) view.findViewById(R.id.invite_code_err_TextView) ;

        view.findViewById(R.id.back_btn).setOnClickListener(this);
        view.findViewById(R.id.toolbar_wallet_linear_layout).setOnClickListener(this);
        view.findViewById(R.id.invite_code_layout).setOnClickListener(this);
        mFindContestButton.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initMembers();
        showHeaderValues();
        setInvitationCodeIfReceived();
    }

    private void initMembers() {
        Bundle args = getArguments();
        if (args != null && args.containsKey(Constants.BundleKeys.JOIN_PRIVATE_CONTEST_WITH_INVITATION_CODE_SCREEN_DATA)) {
            mScreenData = Parcels.unwrap(args.getParcelable(Constants.BundleKeys.JOIN_PRIVATE_CONTEST_WITH_INVITATION_CODE_SCREEN_DATA));
        }
    }

    private void showHeaderValues() {
        if (getView() != null) {
            TextView walletAmtTextView = (TextView) getView().findViewById(R.id.toolbar_wallet_money);
            walletAmtTextView.setText(CodeSnippet.getFormattedAmount(WalletHelper.getTotalBalance()));

            setInviteCodeActive();
        }
    }

    private void setInvitationCodeIfReceived() {
        if (mScreenData != null && getView() != null) {

            /* Private code in screenData , received means just populate and make auto search */
            if (!TextUtils.isEmpty(mScreenData.getPrivateCode())) {

                if (mScreenData.getShareDetails() != null) {
                    HmImageView photoImageView = (HmImageView) getView().findViewById(R.id.join_private_contest_invitee_photo);
                    photoImageView.setImageUrl(mScreenData.getShareDetails().getUserPhotoUrl());

                    String name = (!TextUtils.isEmpty(mScreenData.getShareDetails().getUserNick())) ?
                            mScreenData.getShareDetails().getUserNick() + " has invited you " :
                            "You have been invited ";
                    String msg = name + " to a private contest. Pay and join to play the private contest";

                    TextView msgTextView = (TextView) getView().findViewById(R.id.join_private_contest_header_msg_textView);
                    msgTextView.setText(msg);
                }

                // Set private invitation code
                mInviteCodeEditText.setText(mScreenData.getPrivateCode());
                int length = mInviteCodeEditText.getText().length();
                mInviteCodeEditText.setSelection(length, length);

                mFindContestButton.setVisibility(View.GONE);    // Don't show this button, if link opened

                // set auto click even
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onFindContestClicked();
                    }
                }, 1300);
            }
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.back_btn:
                if (mFragmentListener != null) {
                    mFragmentListener.onBackClicked();
                }
                break;

            case R.id.toolbar_wallet_linear_layout:
                if (mFragmentListener != null) {
                    mFragmentListener.onWalletBalClicked();
                }
                break;

            case R.id.invite_code_layout:
                mInviteCodeEditText.requestFocus();
                showKeyBoard(mInviteCodeEditText);
                setInviteCodeActive();
                break;

            case R.id.find_pvt_contest_details_btn:
                onFindContestClicked();
                NostragamusAnalytics.getInstance().trackClickEvent(Constants.AnalyticsCategory.JOIN_PRIVATE_CONTEST, Constants.AnalyticsClickLabels.FIND_PRIVATE_CONTEST);
                break;
        }
    }

    private void setInviteCodeActive() {
        if (getView() != null) {
            TextView headerTextView = (TextView) getView().findViewById(R.id.pvt_contest_invite_code_heading_textView);
            View lineView = getView().findViewById(R.id.pvt_contest_invite_code_line);

            headerTextView.setTextColor(ContextCompat.getColor(headerTextView.getContext(), R.color.blue_008ae1));
            lineView.setBackgroundColor(ContextCompat.getColor(lineView.getContext(), R.color.blue_008ae1));
        }
    }

    private void setInviteCodeErrorState() {
        if (getView() != null) {
            TextView headerTextView = (TextView) getView().findViewById(R.id.pvt_contest_invite_code_heading_textView);
            View lineView = getView().findViewById(R.id.pvt_contest_invite_code_line);

            headerTextView.setTextColor(ContextCompat.getColor(headerTextView.getContext(), R.color.radical_red));
            lineView.setBackgroundColor(ContextCompat.getColor(lineView.getContext(), R.color.radical_red));
        }
    }

    private void onFindContestClicked() {
        if (getView() != null && getActivity() != null && !getActivity().isFinishing()) {

            String privateCode = mInviteCodeEditText.getText().toString();
            if (!TextUtils.isEmpty(privateCode)) {
                if (privateCode.length() == Constants.PrivateContests.PRIVATE_CODE_LENGTH) {

                    CustomProgressbar.getProgressbar(getContext()).show();
                    new PrivateContestDetailsApiModelImpl().
                            fetchPrivateContestData(privateCode,
                                    getContestDetailsApiListener(privateCode));

                } else {
                    mInviteCodeErrorTextView.setText("Please enter valid invite code");
                    mInviteCodeErrorTextView.setVisibility(View.VISIBLE);
                    setInviteCodeErrorState();
                }
            } else {
                mInviteCodeErrorTextView.setText("Please enter invite code");
                mInviteCodeErrorTextView.setVisibility(View.VISIBLE);
                setInviteCodeErrorState();
            }
        }
    }

    @NonNull
    private PrivateContestDetailsApiModelImpl.PrivateContestDetailApiListener
    getContestDetailsApiListener(final String privateCode) {
        return new PrivateContestDetailsApiModelImpl.PrivateContestDetailApiListener() {
            @Override
            public void onSuccessResponse(int status, FindPrivateContestResponse response) {
                CustomProgressbar.getProgressbar(getContext()).dismissProgress();
                onContestDetailsSuccess(privateCode, response);
            }

            @Override
            public void onError(int status) {
                CustomProgressbar.getProgressbar(getContext()).dismissProgress();
                handleError("", status);
            }

            @Override
            public void onServerSentError(String errorMsg, int errorCode) {
                CustomProgressbar.getProgressbar(getContext()).dismissProgress();
                if (errorCode > 0 ) {
                    showServerSentErrorDialog(errorCode);
                } else {
                    handleError(errorMsg, -1);
                }
            }
        };
    }

    private void showServerSentErrorDialog(int errorCode) {
        switch (errorCode) {
            case Constants.PrivateContests.ErrorCodes.CONTEST_FULL:
                showContestFullDialog();
                break;

            case Constants.PrivateContests.ErrorCodes.INVALID_INVITE_PRIVATE_CODE:
                mInviteCodeErrorTextView.setText("Please enter valid invite code");
                mInviteCodeErrorTextView.setVisibility(View.VISIBLE);
                setInviteCodeErrorState();
                break;

            case Constants.PrivateContests.ErrorCodes.CHALLENGE_STARTED:
                onChallengeStarted();
                break;

            case Constants.PrivateContests.ErrorCodes.UNKNOWN_ERROR:
                showUnknownErrorDialog();
                break;

            case Constants.PrivateContests.ErrorCodes.CONTEST_ALREADY_JOINED:
                showContestAlreadyJoinedDialog();
                break;
        }
    }

    private void showContestAlreadyJoinedDialog() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (JoinPrivateContestWithInviteCodeFragment.this.isVisible()) {

                    String imgUrl = (mScreenData != null && mScreenData.getShareDetails() != null &&
                            !TextUtils.isEmpty(mScreenData.getShareDetails().getUserPhotoUrl())) ?
                            mScreenData.getShareDetails().getUserPhotoUrl() :
                            "";

                    TimerFinishDialogHelper.showPrivateContestAlreadyJoinedDialog(getChildFragmentManager(),
                            imgUrl,
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (mFragmentListener != null) {
                                        mFragmentListener.onBackClicked();
                                    }
                                }
                            });
                }
            }
        }, 500);
    }

    private void onChallengeStarted() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (JoinPrivateContestWithInviteCodeFragment.this.isVisible()) {

                            String msg = "Please join other challenges as this already started";

                            TimerFinishDialogHelper.showChallengeStartedTimerOutDialog(getChildFragmentManager(),
                                    msg,
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (mFragmentListener != null) {
                                                mFragmentListener.onBackClicked();
                                            }
                                        }
                                    });
                        }
                    }
                }, 500);
    }

    private void showUnknownErrorDialog() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (JoinPrivateContestWithInviteCodeFragment.this.isVisible()) {

                    TimerFinishDialogHelper.showPrivateContestUnknownErrorDialog(getChildFragmentManager(),
                            "Something went wrong, try again later",
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (mFragmentListener != null) {
                                        mFragmentListener.onBackClicked();
                                    }
                                }
                            });
                }
            }
        }, 500);
    }

    private void showContestFullDialog() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (JoinPrivateContestWithInviteCodeFragment.this.isVisible()) {

                    String name = "";
                    if (mScreenData != null && mScreenData.getShareDetails() != null) {
                        name = (!TextUtils.isEmpty(mScreenData.getShareDetails().getUserNick())) ?
                                mScreenData.getShareDetails().getUserNick() + "'s " : "";
                    }

                    String msg = String.format("%sPrivate contest is full. Join another contest to play this challenge",
                            name);

                    TimerFinishDialogHelper.showPrivateContestFullDialog(getChildFragmentManager(),
                            msg, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mFragmentListener != null) {
                                mFragmentListener.onBackClicked();
                            }
                        }
                    });
                }
            }
        }, 500);
    }

    private void onContestDetailsSuccess(String privateCode, FindPrivateContestResponse response) {
        if (getView() != null && getActivity() != null && !getActivity().isFinishing() && response != null) {

            PrivateContestDetailsScreenData screenData = new PrivateContestDetailsScreenData();
            screenData.setPrivateCode(privateCode);
            screenData.setPrivateContestDetailsResponse(response);
            if (mScreenData != null) {
                screenData.setShareDetails(mScreenData.getShareDetails());
            }

            Bundle args = new Bundle();
            args.putParcelable(Constants.BundleKeys.PRIVATE_CONTEST_DETAILS_SCREEN_DATA, Parcels.wrap(screenData));

            Intent intent = new Intent(getActivity(), PrivateContestDetailsActivity.class);
            intent.putExtras(args);
            getActivity().startActivityFromFragment(this, intent, 95);

            // Finish activity if screen opened from the click of the link
            if (mFindContestButton.getVisibility() == View.GONE) {
                if (mFragmentListener != null) {
                    mFragmentListener.onBackClicked();
                }
            }
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
}
