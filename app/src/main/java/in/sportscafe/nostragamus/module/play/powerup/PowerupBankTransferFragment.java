package in.sportscafe.nostragamus.module.play.powerup;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
    private Button mAddDoublerButton;
    private Button mAddNoNegativeButton;
    private Button mAddAudiencePollButton;
    private Button mResetButton;
    private Button mTransferPowerUpButton;

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
        checkIfAlreadyTransferred();
        changeAddButtonsToBuyIfRequired();
    }

    private void checkIfAlreadyTransferred() {
        if (mApiResponse != null && mApiResponse.getAlreadyTransferred() != null) {
            int maxLimit = mApiResponse.getMaxTransferLimit();

            if (mApiResponse.getAlreadyTransferred().getDoubler() >= maxLimit) {
                onMaxDoublerPowerUpAdded();
            }
            if (mApiResponse.getAlreadyTransferred().getNoNegative() >= maxLimit) {
                onMaxNoNegativePowerUpAdded();
            }
            if (mApiResponse.getAlreadyTransferred().getAudiencePoll() >= maxLimit) {
                onMaxAudiencePollPowerUpAdded();
            }
        }
    }

    private void changeAddButtonsToBuyIfRequired() {
        if (getView() != null && mApiResponse != null && mApiResponse.getUserBalance() != null) {

            Button buyDoublerButton = (Button) getView().findViewById(R.id.powerup_bank_buy_double_button);
            Button buyNoNegativeButton = (Button) getView().findViewById(R.id.powerup_bank_buy_no_negative_button);
            Button buyAudiencePollButton = (Button) getView().findViewById(R.id.powerup_bank_buy_audience_poll_button);
            buyDoublerButton.setOnClickListener(this);
            buyNoNegativeButton.setOnClickListener(this);
            buyAudiencePollButton.setOnClickListener(this);

            /* Doubler */
            if (mApiResponse.getUserBalance().getDoubler() <= 0) {
                mAddDoublerButton.setVisibility(View.GONE);
                buyDoublerButton.setVisibility(View.VISIBLE);
            } else {
                mAddDoublerButton.setVisibility(View.VISIBLE);
                buyDoublerButton.setVisibility(View.GONE);
            }

            /* No Negative */
            if (mApiResponse.getUserBalance().getNoNegative() <= 0) {
                mAddNoNegativeButton.setVisibility(View.GONE);
                buyNoNegativeButton.setVisibility(View.VISIBLE);
            } else {
                mAddNoNegativeButton.setVisibility(View.VISIBLE);
                buyNoNegativeButton.setVisibility(View.GONE);
            }

            /* Audience Poll */
            if (mApiResponse.getUserBalance().getAudiencePoll() <= 0) {
                mAddAudiencePollButton.setVisibility(View.GONE);
                buyAudiencePollButton.setVisibility(View.VISIBLE);
            } else {
                mAddAudiencePollButton.setVisibility(View.VISIBLE);
                buyAudiencePollButton.setVisibility(View.GONE);
            }
        }
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

        enableResetButton(false);
        enableTransferToChallengeButton(false);
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
                            Thread.sleep(300);
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

        mAddDoublerButton = (Button) rootView.findViewById(R.id.powerup_bank_add_double_button);
        mAddNoNegativeButton = (Button) rootView.findViewById(R.id.powerup_bank_add_no_negative_button);
        mAddAudiencePollButton = (Button) rootView.findViewById(R.id.powerup_bank_add_audience_poll_button);
        mResetButton = (Button) rootView.findViewById(R.id.powerup_bank_reset_button);
        mTransferPowerUpButton = (Button) rootView.findViewById(R.id.powerup_bank_transfer_to_challenge_button);

        mAddDoublerButton.setOnClickListener(this);
        mAddNoNegativeButton.setOnClickListener(this);
        mAddAudiencePollButton.setOnClickListener(this);
        mResetButton.setOnClickListener(this);
        mTransferPowerUpButton.setOnClickListener(this);
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

            case R.id.powerup_bank_buy_double_button:
                onBuyDoublerClicked();
                break;

            case R.id.powerup_bank_buy_no_negative_button:
                onBuyNoNegativeClicked();
                break;

            case R.id.powerup_bank_buy_audience_poll_button:
                onBuyAudiencePollClicked();
                break;
        }
    }

    private void onBuyAudiencePollClicked() {

    }

    private void onBuyNoNegativeClicked() {


    }

    private void onBuyDoublerClicked() {

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

                setUserBalancePowerup(challengeUserInfo);
                NostragamusAnalytics.getInstance().trackPowerBank(Constants.AnalyticsActions.COMPLETED);
                onPowerupTransferred(challengeUserInfo);
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

    private void setUserBalancePowerup(ChallengeUserInfo challengeUserInfo) {
        UserInfo userInfo = NostragamusDataHandler.getInstance().getUserInfo();
        if (userInfo != null && challengeUserInfo != null && challengeUserInfo.getPowerUps() != null) {
            userInfo.getInfoDetails().setPowerUps(challengeUserInfo.getPowerUps());
        }
        NostragamusDataHandler.getInstance().setUserInfo(userInfo);
    }

    private void onPowerupTransferred(ChallengeUserInfo challengeUserInfo) {
        broadcastPowerUpUpdated(challengeUserInfo);
        updatePowerUpDetailsOnUi();
    }

    private void broadcastPowerUpUpdated(ChallengeUserInfo challengeUserInfo) {
        Intent intent = new Intent(Constants.IntentActions.ACTION_POWERUPS_UPDATED);
        if (challengeUserInfo != null) {
            intent.putExtra(Constants.BundleKeys.UPDATED_CHALLENGE_USER_INFO, Parcels.wrap(challengeUserInfo));
        }
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
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

                    enableResetButton(true);
                    enableTransferToChallengeButton(true);
                } else {
                    onMaxAudiencePollPowerUpAdded();
                }
            } else {
                // No more doubler in bank.... change to buy
                mAddAudiencePollButton.setEnabled(false);
            }
        }
    }

    private void onMaxAudiencePollPowerUpAdded() {
        if (getView() != null) {
            TextView errMsgTextView = (TextView) getView().findViewById(R.id.powerup_bank_audience_add_err_textView);
            errMsgTextView.setText(getString(R.string.max_added));
            errMsgTextView.setVisibility(View.VISIBLE);

            ImageView img = (ImageView) getView().findViewById(R.id.powerup_bank_audience_poll_img);
            img.setImageResource(R.drawable.audience_poll_grey_powerup);

            mAddAudiencePollButton.setEnabled(false);
        }
    }

    private void enableResetButton(boolean enable) {
        if (getView() != null) {
            mResetButton.setEnabled(enable);
        }
    }

    private void enableTransferToChallengeButton(boolean enable) {
        if (getView() != null) {
            mTransferPowerUpButton.setEnabled(enable);
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
                    enableResetButton(true);
                    enableTransferToChallengeButton(true);
                } else {
                    onMaxNoNegativePowerUpAdded();
                }
            } else {
                // No more doubler in bank.... change to buy
                mAddNoNegativeButton.setEnabled(false);
            }
        }
    }

    private void onMaxNoNegativePowerUpAdded() {
        if (getView() != null) {
            TextView errMsgTextView = (TextView) getView().findViewById(R.id.powerup_bank_no_neg_add_err_textView);
            errMsgTextView.setText(getString(R.string.max_added));
            errMsgTextView.setVisibility(View.VISIBLE);

            ImageView img = (ImageView) getView().findViewById(R.id.powerup_bank_noneg_img);
            img.setImageResource(R.drawable.no_negative_grey_powerup);

            mAddNoNegativeButton.setEnabled(false);
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

                    enableResetButton(true);
                    enableTransferToChallengeButton(true);
                } else {
                    onMaxDoublerPowerUpAdded();
                }
            } else {
                // No more doubler in bank.... change to buy
                mAddDoublerButton.setEnabled(false);
            }
        }
    }

    private void onMaxDoublerPowerUpAdded() {
        if (getView() != null) {
            TextView errMsgTextView = (TextView) getView().findViewById(R.id.powerup_bank_doubler_add_err_textView);
            errMsgTextView.setText(getString(R.string.max_added));
            errMsgTextView.setVisibility(View.VISIBLE);

            ImageView img = (ImageView) getView().findViewById(R.id.powerup_bank_doubler_img);
            img.setImageResource(R.drawable.double_grey_powerup);

            mAddDoublerButton.setEnabled(false);
        }
    }

    private boolean isWithdrawLimitValid(int withdrawDemand, int alreadyWithdrawn, int maxWithdrawLimit) {
        boolean valid = true;
        if ((withdrawDemand + alreadyWithdrawn) > maxWithdrawLimit) {
            valid = false;
        }
        return valid;
    }

    private boolean isPowerupBankBalanceAvailable(int balPowerup) {
        boolean isValid = false;
        if (balPowerup > 0) {
            isValid = true;
        }
        return isValid;
    }
}
