package in.sportscafe.nostragamus.module.privateContest.ui.joinPrivateContest.findContest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import in.sportscafe.nostragamus.module.customViews.CustomSnackBar;
import in.sportscafe.nostragamus.module.navigation.wallet.WalletHelper;
import in.sportscafe.nostragamus.module.privateContest.dataProvider.PrivateContestDetailsApiModelImpl;
import in.sportscafe.nostragamus.module.privateContest.ui.joinPrivateContest.contestDetails.PrivateContestDetailsActivity;
import in.sportscafe.nostragamus.module.privateContest.ui.joinPrivateContest.dto.FindPrivateContestResponse;
import in.sportscafe.nostragamus.module.privateContest.ui.joinPrivateContest.dto.JoinPrivateContestWithInviteCodeScreenData;
import in.sportscafe.nostragamus.module.privateContest.ui.joinPrivateContest.dto.PrivateContestDetailsScreenData;

/**
 * A simple {@link Fragment} subclass.
 */
public class JoinPrivateContestWithInviteCodeFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = JoinPrivateContestWithInviteCodeFragment.class.getSimpleName();

    private JoinPrivateContestWithInviteCodeFragmentListener mFragmentListener;
    private JoinPrivateContestWithInviteCodeScreenData mScreenData;

    private EditText mPrivateCodeEditText;
    private Button mFindContestButton;

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
        mPrivateCodeEditText = (EditText) view.findViewById(R.id.join_contest_private_code_editText);
        mFindContestButton = (Button) view.findViewById(R.id.find_pvt_contest_details_btn);

        view.findViewById(R.id.back_btn).setOnClickListener(this);
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
            walletAmtTextView.setText(WalletHelper.getFormattedStringOfAmount(WalletHelper.getTotalBalance()));
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
                            mScreenData.getShareDetails().getUserNick() : "";
                    String msg = name + " invite you in private contest. So, let's join & play the contest";

                    TextView msgTextView = (TextView) getView().findViewById(R.id.join_private_contest_header_msg_textView);
                    msgTextView.setText(msg);
                }

                // Set private invitation code
                mPrivateCodeEditText.setText(mScreenData.getPrivateCode());
                int length = mPrivateCodeEditText.getText().length();
                mPrivateCodeEditText.setSelection(length, length);

                // User should not allow to click as auto-click event will occur
                mFindContestButton.setEnabled(false);

                // set auto click even
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mFindContestButton.setEnabled(true);
                        onFindContestClicked();
                    }
                }, 3000);
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

            case R.id.find_pvt_contest_details_btn:
                onFindContestClicked();
                break;
        }
    }

    private void onFindContestClicked() {
        if (getView() != null && getActivity() != null && !getActivity().isFinishing()) {

            String privateCode = mPrivateCodeEditText.getText().toString();
            if (!TextUtils.isEmpty(privateCode)) {
                if (privateCode.length() == Constants.PrivateContests.PRIVATE_CODE_LENGTH) {

                    CustomProgressbar.getProgressbar(getContext()).show();
                    new PrivateContestDetailsApiModelImpl().
                            fetchPrivateContestData(privateCode, getContestDetailsApiListener(privateCode));

                } else {
                    handleError("Please enter proper private code", -1);
                }
            } else {
                handleError("Please enter Private Code", -1);
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
            public void onServerSentError(String errorMsg) {
                CustomProgressbar.getProgressbar(getContext()).dismissProgress();
                handleError(errorMsg, -1);
            }
        };
    }

    private void onContestDetailsSuccess(String privateCode, FindPrivateContestResponse response) {
        if (getView() != null && getActivity() != null && !getActivity().isFinishing() && response != null) {

            PrivateContestDetailsScreenData screenData = new PrivateContestDetailsScreenData();
            screenData.setPrivateCode(privateCode);
            screenData.setPrivateContestDetailsResponse(response);

            Bundle args = new Bundle();
            args.putParcelable(Constants.BundleKeys.PRIVATE_CONTEST_DETAILS_SCREEN_DATA, Parcels.wrap(screenData));

            Intent intent = new Intent(getActivity(), PrivateContestDetailsActivity.class);
            intent.putExtras(args);
            getActivity().startActivityFromFragment(this, intent, 95);
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
