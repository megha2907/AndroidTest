package in.sportscafe.nostragamus.module.play.powerup;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jeeva.android.BaseFragment;

import org.parceler.Parcels;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.allchallenges.dto.Challenge;
import in.sportscafe.nostragamus.module.allchallenges.dto.ChallengeUserInfo;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.play.powerup.dto.AlreadyTransferredPowerupDto;
import in.sportscafe.nostragamus.module.play.powerup.dto.PowerUpBankStatusResponse;
import in.sportscafe.nostragamus.module.play.powerup.dto.UserBalancePowerupDto;
import in.sportscafe.nostragamus.module.play.powerup.dto.UserDemandPowerUpDto;
import in.sportscafe.nostragamus.module.play.powerup.io.PowerupBankStatusApiModelImpl;
import in.sportscafe.nostragamus.module.popups.banktransfer.BankTranferApiModelImpl;
import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;

/**
 * A simple {@link Fragment} subclass.
 */
public class PowerupBankTransferFragment extends BaseFragment implements View.OnClickListener {

    private PowerUpBankTransferFragmentListener mFragmentListener;

    private Challenge mChallenge;
    private PowerUpBankStatusResponse mApiResponse;
    private PowerUpBankStatusResponse mApiResponseForReset;
    private UserDemandPowerUpDto mUserDemandPowerup;
    private TextView mUserBalDoublerTextView;
    private TextView mDemandedDoublerTextView;
    private TextView mUserBalNoNegativeTextView;
    private TextView mDemandedNoNegativeTextView;
    private TextView mUserBalAudiencePollTextView;
    private TextView mDemandedAudiencePollTextView;

    public PowerupBankTransferFragment() {
        mUserDemandPowerup = new UserDemandPowerUpDto();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_powerup_bank_transfer, container, false);
        initView(rootView);
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PowerUpBankTransferFragmentListener) {
            mFragmentListener = (PowerUpBankTransferFragmentListener) context;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initChallengeDetails();
        fetchPowerupBankStatusFromServer();
    }

    private void fetchPowerupBankStatusFromServer() {
        if (mChallenge != null) {
            if (Nostragamus.getInstance().hasNetworkConnection()) {
                showProgressbar();
                PowerupBankStatusApiModelImpl apiModel = PowerupBankStatusApiModelImpl.newInstance(getApiListener());
                apiModel.performApiCall(mChallenge.getChallengeId());
            } else {
                showMessage(Constants.Alerts.NO_NETWORK_CONNECTION);
            }
        }
    }

    private PowerupBankStatusApiModelImpl.PowerupBankStatusApiListener getApiListener() {
        return new PowerupBankStatusApiModelImpl.PowerupBankStatusApiListener() {
            @Override
            public void noInternet() {
                dismissProgressbar();
                showMessage(Constants.Alerts.NO_NETWORK_CONNECTION);
            }

            @Override
            public void onApiFailed() {
                dismissProgressbar();
                showMessage(Constants.Alerts.API_FAIL);
            }

            @Override
            public void onSuccessResponse(PowerUpBankStatusResponse response) {
                dismissProgressbar();
                onApiSuccess(response);

                if (mFragmentListener != null && mChallenge != null && response != null) {
                    Bundle args = new Bundle();
                    args.putString(Constants.BundleKeys.CHALLENGE_NAME, mChallenge.getName());
                    args.putInt(Constants.BundleKeys.MAX_TRANSFER_COUNT, response.getMaxTransferLimit());
                    mFragmentListener.updatePowerUpInfoDetails(args);
                }
            }
        };
    }

    private void onApiSuccess(PowerUpBankStatusResponse response) {
        if (response != null) {
            mApiResponse = response;
            mApiResponseForReset = mApiResponse.clone();
        }
        updatePowerUpDetailsOnUi();
    }

    private void updatePowerUpDetailsOnUi() {
        if (mApiResponse != null && mApiResponse.getUserBalance() != null && getView() != null) {
            UserBalancePowerupDto userBalance = mApiResponse.getUserBalance();
            mUserBalDoublerTextView.setText(String.valueOf(userBalance.getDoubler()));
            mUserBalNoNegativeTextView.setText(String.valueOf(userBalance.getNoNegative()));
            mUserBalAudiencePollTextView.setText(String.valueOf(userBalance.getAudiencePoll()));
        }

        if (mUserDemandPowerup != null) {
            mUserDemandPowerup.clearData();
            mDemandedDoublerTextView.setText(String.valueOf(mUserDemandPowerup.getDoubler()));
            mDemandedNoNegativeTextView.setText(String.valueOf(mUserDemandPowerup.getNoNegative()));
            mDemandedAudiencePollTextView.setText(String.valueOf(mUserDemandPowerup.getAudiencePoll()));
        }
    }

    private void resetUi() {
        /* This is to perform RESET animation */
        if (mUserDemandPowerup != null && mApiResponse != null && mApiResponse.getUserBalance() != null) {
            new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        Thread.currentThread(); // This is AsyncTask thread, NOT UI thread.
                        while (mUserDemandPowerup.getDoubler() > 0 ||
                                mUserDemandPowerup.getNoNegative() > 0 ||
                                mUserDemandPowerup.getAudiencePoll() > 0) {

                            if (mUserDemandPowerup.getDoubler() > 0) {
                                mUserDemandPowerup.setDoubler(mUserDemandPowerup.getDoubler() - 1);
                                mApiResponse.getUserBalance().setDoubler(mApiResponse.getUserBalance().getDoubler() + 1);
                            }

                            if (mUserDemandPowerup.getNoNegative() > 0) {
                                mUserDemandPowerup.setNoNegative(mUserDemandPowerup.getNoNegative() - 1);
                                mApiResponse.getUserBalance().setNoNegative(mApiResponse.getUserBalance().getNoNegative() + 1);
                            }

                            if (mUserDemandPowerup.getAudiencePoll() > 0) {
                                mUserDemandPowerup.setAudiencePoll(mUserDemandPowerup.getAudiencePoll() - 1);
                                mApiResponse.getUserBalance().setAudiencePoll(mApiResponse.getUserBalance().getAudiencePoll() + 1);
                            }

                            publishProgress(null);
                            Thread.sleep(150);
                        }
                        cancel(false);

                    } catch (InterruptedException ex) {}
                    return null;
                }

                @Override
                protected void onProgressUpdate(Void... values) {
                    super.onProgressUpdate(values);

                    UserBalancePowerupDto userBalance = mApiResponse.getUserBalance();
                    if (userBalance != null) {
                        mUserBalDoublerTextView.setText(String.valueOf(userBalance.getDoubler()));
                        mUserBalNoNegativeTextView.setText(String.valueOf(userBalance.getNoNegative()));
                        mUserBalAudiencePollTextView.setText(String.valueOf(userBalance.getAudiencePoll()));
                    }
                    if (mUserDemandPowerup != null) {
                        mDemandedDoublerTextView.setText(String.valueOf(mUserDemandPowerup.getDoubler()));
                        mDemandedNoNegativeTextView.setText(String.valueOf(mUserDemandPowerup.getNoNegative()));
                        mDemandedAudiencePollTextView.setText(String.valueOf(mUserDemandPowerup.getAudiencePoll()));
                    }
                }

                @Override
                protected void onCancelled() {
                    super.onCancelled();

                    // Transfer reset values so that any issue while reset-ui operation does not affect other things
                    if (mApiResponseForReset != null) {
                        mApiResponse = mApiResponseForReset.clone();
                    }
                    updatePowerUpDetailsOnUi();
                }
            }.execute();
        }
    }

    private void initChallengeDetails() {
        Bundle args = getArguments();
        if (args != null && args.containsKey(Constants.BundleKeys.CHALLENGE_INFO)) {
            mChallenge = Parcels.unwrap(args.getParcelable(Constants.BundleKeys.CHALLENGE_INFO));
        }
    }

    private void initView(View rootView) {
        mUserBalDoublerTextView = (TextView) rootView.findViewById(R.id.powerup_bank_user_bal_doubler_textView);
        mDemandedDoublerTextView = (TextView) rootView.findViewById(R.id.powerup_bank_doubler_demand_textView);
        mUserBalNoNegativeTextView = (TextView) rootView.findViewById(R.id.powerup_bank_user_bal_no_neg_textView);
        mDemandedNoNegativeTextView = (TextView) rootView.findViewById(R.id.powerup_bank_no_neg_demand_textView);
        mUserBalAudiencePollTextView = (TextView) rootView.findViewById(R.id.powerup_bank_user_bal_audience_poll_textView);
        mDemandedAudiencePollTextView = (TextView) rootView.findViewById(R.id.powerup_bank_audience_demand_textView);

        Button addDoublerButton = (Button) rootView.findViewById(R.id.powerup_bank_add_double_button);
        Button addNoNegativeButton = (Button) rootView.findViewById(R.id.powerup_bank_add_no_negative_button);
        Button addAudiencePollButton = (Button) rootView.findViewById(R.id.powerup_bank_add_audience_poll_button);
        Button resetButton = (Button) rootView.findViewById(R.id.powerup_bank_reset_button);
        Button transferPowerupButton = (Button) rootView.findViewById(R.id.powerup_bank_transfer_to_challenge_button);

        addDoublerButton.setOnClickListener(this);
        addNoNegativeButton.setOnClickListener(this);
        addAudiencePollButton.setOnClickListener(this);
        resetButton.setOnClickListener(this);
        transferPowerupButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.powerup_bank_add_double_button:
                addDoubler();
                break;

            case R.id.powerup_bank_add_no_negative_button:
                addNoNegative();
                break;

            case R.id.powerup_bank_add_audience_poll_button:
                addAudiencePoll();
                break;

            case R.id.powerup_bank_reset_button:
                resetUi();
                break;

            case R.id.powerup_bank_transfer_to_challenge_button:
                onTransferClicked();

                break;
        }
    }

    private void onTransferClicked() {
        NostragamusAnalytics.getInstance().trackPowerBank(Constants.AnalyticsActions.ADDED);
        performTransferPowerupApiCall();
    }

    private void performTransferPowerupApiCall() {
        if (isPowerupDemanded() && mUserDemandPowerup != null) {

            showProgressbar();
            BankTranferApiModelImpl.newInstance(getTransferToChallengeListener()).
                    transferToChallenge(mUserDemandPowerup.getValueMap(),
                            (mChallenge != null) ? mChallenge.getChallengeId() : 0);

        } else {
            showMessage("Please add any Powerup to transfer to Challenge");
        }
    }

    private boolean isPowerupDemanded() {
        boolean isDemanded = false;

        if (mUserDemandPowerup != null &&
                (mUserDemandPowerup.getDoubler() > 0 || mUserDemandPowerup.getNoNegative() > 0 || mUserDemandPowerup.getAudiencePoll() > 0)) {
            isDemanded = true;
        }

        return isDemanded;
    }

    @NonNull
    private BankTranferApiModelImpl.BankTransferApiModelListener getTransferToChallengeListener() {
        return new BankTranferApiModelImpl.BankTransferApiModelListener() {
            @Override
            public void onSuccess(ChallengeUserInfo challengeUserInfo) {
                dismissProgressbar();

                UserInfo userInfo = NostragamusDataHandler.getInstance().getUserInfo();
                userInfo.getInfoDetails().setPowerUps(mApiResponse.getUserBalance().getValueMap());
                NostragamusDataHandler.getInstance().setUserInfo(userInfo);

                NostragamusAnalytics.getInstance().trackPowerBank(Constants.AnalyticsActions.COMPLETED);

                onPowerupTransferred();
            }

            @Override
            public void onNoInternet() {
                dismissProgressbar();
                showMessage(Constants.Alerts.NO_NETWORK_CONNECTION, Toast.LENGTH_LONG);
            }

            @Override
            public void onFailed(String message) {
                dismissProgressbar();
                showMessage(message, Toast.LENGTH_LONG);
            }
        };
    }

    /* Powerup Successfully transferred */
    private void onPowerupTransferred() {
        // animation
        // finish
        if (mFragmentListener != null) {
            mFragmentListener.finishActivity();
        }

    }

    private void addAudiencePoll() {
        if (getView() != null && mApiResponse != null &&  mApiResponse.getAlreadyTransferred() != null && mApiResponse.getUserBalance() != null) {
            AlreadyTransferredPowerupDto alreadyTransferredPowerup = mApiResponse.getAlreadyTransferred();
            UserBalancePowerupDto userBalancePowerup = mApiResponse.getUserBalance();

            if (isPowerupBankBalanceAvailable(userBalancePowerup.getAudiencePoll())) {
                if (isWithdrawLimitValid(1, alreadyTransferredPowerup.getAudiencePoll(), mApiResponse.getMaxTransferLimit())) {

                    userBalancePowerup.setAudiencePoll(userBalancePowerup.getAudiencePoll() - 1);
                    mUserDemandPowerup.setAudiencePoll(mUserDemandPowerup.getAudiencePoll() + 1);

                    // Update ui
                    mUserBalAudiencePollTextView.setText(String.valueOf(userBalancePowerup.getAudiencePoll()));
                    mDemandedAudiencePollTextView.setText(String.valueOf(mUserDemandPowerup.getAudiencePoll()));
                } else {
                    showMessage("You can't add more than " + mApiResponse.getMaxTransferLimit() + " powerups in one category");
                }
            } else {
                // No more doubler in bank.... change to buy
                showMessage("No Audience-poll powerup balance in your Bank");
            }
        }
    }

    private void addNoNegative() {
        if (getView() != null && mApiResponse != null &&  mApiResponse.getAlreadyTransferred() != null && mApiResponse.getUserBalance() != null) {
            AlreadyTransferredPowerupDto alreadyTransferredPowerup = mApiResponse.getAlreadyTransferred();
            UserBalancePowerupDto userBalancePowerup = mApiResponse.getUserBalance();

            if (isPowerupBankBalanceAvailable(userBalancePowerup.getNoNegative())) {
                if (isWithdrawLimitValid(1, alreadyTransferredPowerup.getNoNegative(), mApiResponse.getMaxTransferLimit())) {

                    userBalancePowerup.setNoNegative(userBalancePowerup.getNoNegative() - 1);
                    mUserDemandPowerup.setNoNegative(mUserDemandPowerup.getNoNegative() + 1);

                    // Update ui
                    mUserBalNoNegativeTextView.setText(String.valueOf(userBalancePowerup.getNoNegative()));
                    mDemandedNoNegativeTextView.setText(String.valueOf(mUserDemandPowerup.getNoNegative()));
                } else {
                    showMessage("You can't add more than " + mApiResponse.getMaxTransferLimit() + " powerups in one category");
                }
            } else {
                // No more doubler in bank.... change to buy
                showMessage("No No-Negative powerup balance in your Bank");
            }
        }
    }

    private void addDoubler() {
        if (getView() != null && mApiResponse != null &&  mApiResponse.getAlreadyTransferred() != null && mApiResponse.getUserBalance() != null) {
            AlreadyTransferredPowerupDto alreadyTransferredPowerup = mApiResponse.getAlreadyTransferred();
            UserBalancePowerupDto userBalancePowerup = mApiResponse.getUserBalance();

            if (isPowerupBankBalanceAvailable(userBalancePowerup.getDoubler())) {
                if (isWithdrawLimitValid(1, alreadyTransferredPowerup.getDoubler(), mApiResponse.getMaxTransferLimit())) {

                    userBalancePowerup.setDoubler(userBalancePowerup.getDoubler() - 1);
                    mUserDemandPowerup.setDoubler(mUserDemandPowerup.getDoubler() + 1);

                    // Update ui
                    mUserBalDoublerTextView.setText(String.valueOf(userBalancePowerup.getDoubler()));
                    mDemandedDoublerTextView.setText(String.valueOf(mUserDemandPowerup.getDoubler()));
                } else {
                    showMessage("You can't add more than " + mApiResponse.getMaxTransferLimit() + " powerups in one category");
                }
            } else {
                // No more doubler in bank.... change to buy
                showMessage("No Doubler powerup balance in your Bank");
            }
        }
    }

    private boolean isWithdrawLimitValid(int withdrawDemand, int alreadyWithdrawn, int maxWithdrawLimit) {
        if (withdrawDemand > 0 && (withdrawDemand + alreadyWithdrawn) > maxWithdrawLimit) {
            showMessage("You can't add more than " + maxWithdrawLimit + " powerups in one category");
            return false;
        }
        return withdrawDemand >= 0;
    }

    private boolean isPowerupBankBalanceAvailable(int balPowerup) {
        boolean isValid = false;
        if (balPowerup > 0) {
            isValid = true;
        }
        return isValid;
    }
}
