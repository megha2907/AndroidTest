package in.sportscafe.nostragamus.module.prediction.powerupBank;


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

import in.sportscafe.nostragamus.BuildConfig;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.prediction.playScreen.dto.PlayScreenDataDto;
import in.sportscafe.nostragamus.module.prediction.playScreen.dto.PowerUp;
import in.sportscafe.nostragamus.module.prediction.powerupBank.dataProvider.PowerupBankStatusApiModelImpl;
import in.sportscafe.nostragamus.module.prediction.powerupBank.dataProvider.TransferPowerUpFromBankApiImpl;
import in.sportscafe.nostragamus.module.prediction.powerupBank.dto.PowerUpBankStatusResponse;
import in.sportscafe.nostragamus.module.prediction.powerupBank.dto.TransferPowerUpFromBankResponse;
import in.sportscafe.nostragamus.module.store.StoreLaunchMode;

/**
 * A simple {@link Fragment} subclass.
 */
public class PowerupBankTransferFragment extends BaseFragment implements View.OnClickListener {

    private PowerUpBankTransferFragmentListener mFragmentListener;

    private PlayScreenDataDto mPlayScreenData;
    private PowerUpBankStatusResponse mApiResponse;
    private PowerUpBankStatusResponse mApiResponseForReset;
    private PowerUp mUserDemandPowerup;
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
        mUserDemandPowerup = new PowerUp();
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
        if (mPlayScreenData != null) {
            if (Nostragamus.getInstance().hasNetworkConnection()) {
                showProgressbar();
                PowerupBankStatusApiModelImpl apiModel = PowerupBankStatusApiModelImpl.newInstance(getApiListener());
                apiModel.performApiCall(mPlayScreenData.getChallengeId(), mPlayScreenData.getRoomId());
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

                if (mFragmentListener != null && mPlayScreenData != null && response != null) {
                    Bundle args = new Bundle();
                    args.putString(Constants.BundleKeys.CHALLENGE_NAME, mPlayScreenData.getSubTitle());
                    args.putInt(Constants.BundleKeys.MAX_TRANSFER_COUNT, response.getMaxTransferLimit());
                    mFragmentListener.updatePowerUpInfoDetails(args);
                }
            }
        };
    }

    private void onApiSuccess(PowerUpBankStatusResponse response) {
        if (response != null) {
            mApiResponse = response;
            mApiResponseForReset = cloneResponse(response);
        }

        performResetOperation();
    }

    private PowerUpBankStatusResponse cloneResponse(PowerUpBankStatusResponse response) {
        PowerUpBankStatusResponse cloneResponse = new PowerUpBankStatusResponse();
        if (response != null) {
            PowerUp userBalance = new PowerUp();
            PowerUp alreadyTransferred = new PowerUp();

            if (response.getUserBalance() != null) {
                userBalance.setDoubler(response.getUserBalance().getDoubler());
                userBalance.setNoNegative(response.getUserBalance().getNoNegative());
                userBalance.setPlayerPoll(response.getUserBalance().getPlayerPoll());
            }
            if (response.getAlreadyTransferred() != null) {
                alreadyTransferred.setDoubler(response.getAlreadyTransferred().getDoubler());
                alreadyTransferred.setNoNegative(response.getAlreadyTransferred().getNoNegative());
                alreadyTransferred.setPlayerPoll(response.getAlreadyTransferred().getPlayerPoll());
            }

            cloneResponse.setMaxTransferLimit(response.getMaxTransferLimit());
            cloneResponse.setUserBalance(userBalance);
            cloneResponse.setAlreadyTransferred(alreadyTransferred);
        }
        return cloneResponse;
    }

    private void performResetOperation() {
        mAddDoublerButton.setEnabled(true);
        mAddNoNegativeButton.setEnabled(true);
        mAddAudiencePollButton.setEnabled(true);

        updatePowerUpDetailsOnUi();
        checkIfAlreadyTransferred();
        changeAddButtonsToBuyIfRequired();
    }

    private void checkIfAlreadyTransferred() {
        if (mApiResponse != null && mApiResponse.getAlreadyTransferred() != null && getView() != null) {
            int maxLimit = mApiResponse.getMaxTransferLimit();

            if (mApiResponse.getAlreadyTransferred().getDoubler() >= maxLimit) {
                onMaxDoublerPowerUpAdded(true);
            } else {
                onMaxDoublerPowerUpAdded(false);
            }

            if (mApiResponse.getAlreadyTransferred().getNoNegative() >= maxLimit) {
                onMaxNoNegativePowerUpAdded(true);
            } else {
                onMaxNoNegativePowerUpAdded(false);
            }

            if (mApiResponse.getAlreadyTransferred().getPlayerPoll() >= maxLimit) {
                onMaxAudiencePollPowerUpAdded(true);
            } else {
                onMaxAudiencePollPowerUpAdded(false);
            }
        }
    }

    private void changeAddButtonsToBuyIfRequired() {
        changeDoublerToBuy();
        changeNoNegativeToBuy();
        changeAudiencePollToBuy();
    }

    private void changeAudiencePollToBuy() {
        if (getView() != null && mApiResponse != null && mApiResponse.getUserBalance() != null) {
            Button buyAudiencePollButton = (Button) getView().findViewById(R.id.powerup_bank_buy_audience_poll_button);
            buyAudiencePollButton.setOnClickListener(this);
            ImageView img = (ImageView) getView().findViewById(R.id.powerup_bank_audience_poll_img);
            if (mApiResponse.getUserBalance().getPlayerPoll() <= 0) {
                if (BuildConfig.IS_PAID_VERSION) {
                    mAddAudiencePollButton.setVisibility(View.GONE);
                    buyAudiencePollButton.setVisibility(View.VISIBLE);
                    img.setImageResource(R.drawable.audience_poll_grey_powerup);
                } else {
                    mAddAudiencePollButton.setEnabled(false);
                }
            } else {
                mAddAudiencePollButton.setVisibility(View.VISIBLE);
                buyAudiencePollButton.setVisibility(View.GONE);
                img.setImageResource(R.drawable.audience_poll_powerup);
            }
        }
    }

    private void changeNoNegativeToBuy() {
        if (getView() != null && mApiResponse != null && mApiResponse.getUserBalance() != null) {
            Button buyNoNegativeButton = (Button) getView().findViewById(R.id.powerup_bank_buy_no_negative_button);
            buyNoNegativeButton.setOnClickListener(this);
            ImageView img = (ImageView) getView().findViewById(R.id.powerup_bank_noneg_img);
            if (mApiResponse.getUserBalance().getNoNegative() <= 0) {
                if (BuildConfig.IS_PAID_VERSION) {
                    mAddNoNegativeButton.setVisibility(View.GONE);
                    buyNoNegativeButton.setVisibility(View.VISIBLE);
                    img.setImageResource(R.drawable.no_negative_grey_powerup);
                } else {
                    mAddNoNegativeButton.setEnabled(false);
                }
            } else {
                mAddNoNegativeButton.setVisibility(View.VISIBLE);
                buyNoNegativeButton.setVisibility(View.GONE);
                img.setImageResource(R.drawable.no_negative_powerup);
            }
        }
    }

    private void changeDoublerToBuy() {
        if (getView() != null && mApiResponse != null && mApiResponse.getUserBalance() != null) {
            Button buyDoublerButton = (Button) getView().findViewById(R.id.powerup_bank_buy_double_button);
            buyDoublerButton.setOnClickListener(this);
            ImageView img = (ImageView) getView().findViewById(R.id.powerup_bank_doubler_img);

            if (mApiResponse.getUserBalance().getDoubler() <= 0) {
                if (BuildConfig.IS_PAID_VERSION) {
                    mAddDoublerButton.setVisibility(View.GONE);
                    buyDoublerButton.setVisibility(View.VISIBLE);
                    img.setImageResource(R.drawable.double_grey_powerup);
                } else {
                    mAddDoublerButton.setEnabled(false);
                }
            } else {
                mAddDoublerButton.setVisibility(View.VISIBLE);
                buyDoublerButton.setVisibility(View.GONE);
                img.setImageResource(R.drawable.double_powerup);
            }
        }
    }

    private void updatePowerUpDetailsOnUi() {
        if (mApiResponse != null && mApiResponse.getUserBalance() != null && getView() != null) {
            PowerUp userBalance = mApiResponse.getUserBalance();
            mUserBalDoublerTextView.setText(String.valueOf(userBalance.getDoubler()));
            mUserBalNoNegativeTextView.setText(String.valueOf(userBalance.getNoNegative()));
            mUserBalAudiencePollTextView.setText(String.valueOf(userBalance.getPlayerPoll()));
        }

        if (mUserDemandPowerup != null) {
            mUserDemandPowerup = new PowerUp();
            mDemandedDoublerTextView.setText(String.valueOf(mUserDemandPowerup.getDoubler()));
            mDemandedNoNegativeTextView.setText(String.valueOf(mUserDemandPowerup.getNoNegative()));
            mDemandedAudiencePollTextView.setText(String.valueOf(mUserDemandPowerup.getPlayerPoll()));
        }

        enableTransferToChallengeButton(false);
        enableResetButton(false);
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
                                mUserDemandPowerup.getPlayerPoll() > 0) {

                            if (mUserDemandPowerup.getDoubler() > 0) {
                                mUserDemandPowerup.setDoubler(mUserDemandPowerup.getDoubler() - 1);
                                mApiResponse.getUserBalance().setDoubler(mApiResponse.getUserBalance().getDoubler() + 1);
                            }

                            if (mUserDemandPowerup.getNoNegative() > 0) {
                                mUserDemandPowerup.setNoNegative(mUserDemandPowerup.getNoNegative() - 1);
                                mApiResponse.getUserBalance().setNoNegative(mApiResponse.getUserBalance().getNoNegative() + 1);
                            }

                            if (mUserDemandPowerup.getPlayerPoll() > 0) {
                                mUserDemandPowerup.setPlayerPoll(mUserDemandPowerup.getPlayerPoll() - 1);
                                mApiResponse.getUserBalance().setPlayerPoll(mApiResponse.getUserBalance().getPlayerPoll() + 1);
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

                    PowerUp userBalance = mApiResponse.getUserBalance();
                    if (userBalance != null) {
                        mUserBalDoublerTextView.setText(String.valueOf(userBalance.getDoubler()));
                        mUserBalNoNegativeTextView.setText(String.valueOf(userBalance.getNoNegative()));
                        mUserBalAudiencePollTextView.setText(String.valueOf(userBalance.getPlayerPoll()));
                    }
                    if (mUserDemandPowerup != null) {
                        mDemandedDoublerTextView.setText(String.valueOf(mUserDemandPowerup.getDoubler()));
                        mDemandedNoNegativeTextView.setText(String.valueOf(mUserDemandPowerup.getNoNegative()));
                        mDemandedAudiencePollTextView.setText(String.valueOf(mUserDemandPowerup.getPlayerPoll()));
                    }
                }

                @Override
                protected void onCancelled() {
                    super.onCancelled();

                    // Transfer reset values so that any issue while reset-ui operation does not affect other things
                    if (mApiResponseForReset != null) {
                        mApiResponse = cloneResponse(mApiResponseForReset);
                    }
                    performResetOperation();
                }
            }.execute();
        }
    }

    private void initChallengeDetails() {
        Bundle args = getArguments();
        if (args != null && args.containsKey(Constants.BundleKeys.PLAY_SCREEN_DATA)) {
            mPlayScreenData = Parcels.unwrap(args.getParcelable(Constants.BundleKeys.PLAY_SCREEN_DATA));
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
        performBuy(Constants.StoreBuyProductCategory.POWER_UP_AUDIENCE_POLL);
    }

    private void onBuyNoNegativeClicked() {
        performBuy(Constants.StoreBuyProductCategory.POWER_UP_NO_NEGATIVE);
    }

    private void onBuyDoublerClicked() {
        performBuy(Constants.StoreBuyProductCategory.POWER_UP_DOUBLER);
    }

    private void performBuy(int buyCategory) {
        if (BuildConfig.IS_PAID_VERSION) {
            Bundle args = new Bundle();
            args.putInt(Constants.BundleKeys.STORE_BUY_PRODUCT_CATEGORY, buyCategory);
            args.putInt(Constants.BundleKeys.LAUNCH_MODE, StoreLaunchMode.POWER_UP_BANK_LAUCNH);

            if (mFragmentListener != null) {
                mFragmentListener.launchStore(args);
            }
        }
    }

    private void onTransferClicked() {
        NostragamusAnalytics.getInstance().trackPowerBank(Constants.AnalyticsActions.ADDED);
        performTransferPowerupApiCall();
    }

    private void performTransferPowerupApiCall() {
        if (isPowerupDemanded() && mUserDemandPowerup != null && mPlayScreenData != null) {

            showProgressbar();
            TransferPowerUpFromBankApiImpl.newInstance(getTransferToChallengeListener()).
                    transferToChallenge(mUserDemandPowerup,
                            mPlayScreenData.getChallengeId(),
                            mPlayScreenData.getRoomId());

        } else {
            showMessage("Please add any Powerup to transfer to NewChallengesResponse");
        }
    }

    private boolean isPowerupDemanded() {
        boolean isDemanded = false;

        if (mUserDemandPowerup != null &&
                (mUserDemandPowerup.getDoubler() > 0 || mUserDemandPowerup.getNoNegative() > 0 || mUserDemandPowerup.getPlayerPoll() > 0)) {
            isDemanded = true;
        }

        return isDemanded;
    }

    @NonNull
    private TransferPowerUpFromBankApiImpl.BankTransferApiModelListener getTransferToChallengeListener() {
        return new TransferPowerUpFromBankApiImpl.BankTransferApiModelListener() {
            @Override
            public void onSuccess(TransferPowerUpFromBankResponse response) {
                dismissProgressbar();

                if (response != null) {
                    setUserBalancePowerUp(response);
                    NostragamusAnalytics.getInstance().trackPowerBank(Constants.AnalyticsActions.COMPLETED);
                    onPowerupTransferred(response);
                }
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


    private void setUserBalancePowerUp(TransferPowerUpFromBankResponse response) {
        /*UserInfo userInfo = NostragamusDataHandler.getInstance().getUserInfo();
        if (userInfo != null && response != null && response.getPowerUps() != null) {
            userInfo.getInfoDetails().setPowerUps(challengeUserInfo.getPowerUps());
        }
        NostragamusDataHandler.getInstance().setUserInfo(userInfo);*/

        if (response != null && response.getBalancePowerUp() != null) {
            mApiResponse.setUserBalance(response.getBalancePowerUp());
            mApiResponseForReset.setUserBalance(response.getBalancePowerUp());
        }
    }

    private void onPowerupTransferred(TransferPowerUpFromBankResponse response) {
        broadcastPowerUpUpdated(response);
        updatePowerUpDetailsOnUi();
        if (mFragmentListener != null) {
            mFragmentListener.finishActivity();
        }
    }

    private void broadcastPowerUpUpdated(TransferPowerUpFromBankResponse response) {
        Intent intent = new Intent(Constants.IntentActions.ACTION_POWERUPS_UPDATED);
        if (response != null && response.getBalancePowerUp() != null) {
            Bundle args = new Bundle();
            args.putParcelable(Constants.BundleKeys.POWERUPS, Parcels.wrap(response.getBalancePowerUp()));
            intent.putExtras(args);
        }
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
    }

    private void addAudiencePoll() {
        if (getView() != null && mApiResponse != null &&  mApiResponse.getAlreadyTransferred() != null && mApiResponse.getUserBalance() != null) {

            PowerUp alreadyTransferredPowerup = mApiResponse.getAlreadyTransferred();
            PowerUp userBalancePowerup = mApiResponse.getUserBalance();

            if (isPowerupBankBalanceAvailable(userBalancePowerup.getPlayerPoll())) {
                int totalWithdrawDemand = mUserDemandPowerup.getPlayerPoll() + 1;
                if (isWithdrawLimitValid(totalWithdrawDemand, alreadyTransferredPowerup.getPlayerPoll(), mApiResponse.getMaxTransferLimit())) {

                    userBalancePowerup.setPlayerPoll(userBalancePowerup.getPlayerPoll() - 1);
                    mUserDemandPowerup.setPlayerPoll(mUserDemandPowerup.getPlayerPoll() + 1);

                    // Update ui
                    mUserBalAudiencePollTextView.setText(String.valueOf(userBalancePowerup.getPlayerPoll()));
                    mDemandedAudiencePollTextView.setText(String.valueOf(mUserDemandPowerup.getPlayerPoll()));

                    enableResetButton(true);
                    enableTransferToChallengeButton(true);

                    changeAudiencePollToBuy();
                } else {
                    onMaxAudiencePollPowerUpAdded(true);
                }
            } else {
                mAddAudiencePollButton.setEnabled(false);
                changeAudiencePollToBuy();
            }
        }
    }

    private void onMaxAudiencePollPowerUpAdded(boolean isMaxAdded) {
        if (getView() != null) {
            TextView errMsgTextView = (TextView) getView().findViewById(R.id.powerup_bank_audience_add_err_textView);
            if (isMaxAdded) {
                errMsgTextView.setVisibility(View.VISIBLE);
                mAddAudiencePollButton.setEnabled(false);
            } else {
                errMsgTextView.setVisibility(View.INVISIBLE);
                mAddAudiencePollButton.setEnabled(true);
            }
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

            PowerUp alreadyTransferredPowerup = mApiResponse.getAlreadyTransferred();
            PowerUp userBalancePowerup = mApiResponse.getUserBalance();

            if (isPowerupBankBalanceAvailable(userBalancePowerup.getNoNegative())) {
                int totalWithdrawDemand = mUserDemandPowerup.getNoNegative() + 1;
                if (isWithdrawLimitValid(totalWithdrawDemand, alreadyTransferredPowerup.getNoNegative(), mApiResponse.getMaxTransferLimit())) {

                    userBalancePowerup.setNoNegative(userBalancePowerup.getNoNegative() - 1);
                    mUserDemandPowerup.setNoNegative(mUserDemandPowerup.getNoNegative() + 1);

                    // Update ui
                    mUserBalNoNegativeTextView.setText(String.valueOf(userBalancePowerup.getNoNegative()));
                    mDemandedNoNegativeTextView.setText(String.valueOf(mUserDemandPowerup.getNoNegative()));
                    enableResetButton(true);
                    enableTransferToChallengeButton(true);
                    changeNoNegativeToBuy();
                } else {
                    onMaxNoNegativePowerUpAdded(true);
                }
            } else {
                mAddNoNegativeButton.setEnabled(false);
                changeNoNegativeToBuy();
            }
        }
    }

    private void onMaxNoNegativePowerUpAdded(boolean isMaxAdded) {
        if (getView() != null) {
            TextView errMsgTextView = (TextView) getView().findViewById(R.id.powerup_bank_no_neg_add_err_textView);
            if (isMaxAdded) {
                errMsgTextView.setVisibility(View.VISIBLE);
                mAddNoNegativeButton.setEnabled(false);
            } else {
                errMsgTextView.setVisibility(View.INVISIBLE);
                mAddNoNegativeButton.setEnabled(true);
            }
        }
    }

    private void addDoubler() {
        if (getView() != null && mApiResponse != null &&  mApiResponse.getAlreadyTransferred() != null && mApiResponse.getUserBalance() != null) {

            PowerUp alreadyTransferredPowerup = mApiResponse.getAlreadyTransferred();
            PowerUp userBalancePowerup = mApiResponse.getUserBalance();

            if (isPowerupBankBalanceAvailable(userBalancePowerup.getDoubler())) {
                int totalWithdrawDemand = mUserDemandPowerup.getDoubler() + 1;
                if (isWithdrawLimitValid(totalWithdrawDemand, alreadyTransferredPowerup.getDoubler(), mApiResponse.getMaxTransferLimit())) {

                    userBalancePowerup.setDoubler(userBalancePowerup.getDoubler() - 1);
                    mUserDemandPowerup.setDoubler(mUserDemandPowerup.getDoubler() + 1);

                    // Update ui
                    mUserBalDoublerTextView.setText(String.valueOf(userBalancePowerup.getDoubler()));
                    mDemandedDoublerTextView.setText(String.valueOf(mUserDemandPowerup.getDoubler()));

                    enableResetButton(true);
                    enableTransferToChallengeButton(true);
                    changeDoublerToBuy();
                } else {
                    onMaxDoublerPowerUpAdded(true);
                }
            } else {
                mAddDoublerButton.setEnabled(false);
                changeDoublerToBuy();
            }
        }
    }

    private void onMaxDoublerPowerUpAdded(boolean isMaxAdded) {
        if (getView() != null) {
            TextView errMsgTextView = (TextView) getView().findViewById(R.id.powerup_bank_doubler_add_err_textView);
            if (isMaxAdded) {
                errMsgTextView.setVisibility(View.VISIBLE);
                mAddDoublerButton.setEnabled(false);
            } else {
                errMsgTextView.setVisibility(View.INVISIBLE);
                mAddDoublerButton.setEnabled(true);
            }
        }
    }

    private boolean isWithdrawLimitValid(int withdrawDemand, int alreadyWithdrawn, int maxWithdrawLimit) {
        boolean valid = true;       // Default should allow to withdraw
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

    /**
     * After completing store action
     */
    public void onStoreResponse() {
        fetchPowerupBankStatusFromServer();
    }
}
