package in.sportscafe.nostragamus.module.navigation.wallet;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeeva.android.BaseFragment;
import com.jeeva.android.Log;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.navigation.wallet.dto.UserWalletResponse;
import in.sportscafe.nostragamus.module.user.login.UserInfoModelImpl;
import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;
import in.sportscafe.nostragamus.utils.AnimationHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class WalletHomeFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = WalletHomeFragment.class.getSimpleName();

    private WalletHomeFragmentListener mFragmentListener;
    private LinearLayout mWalletMoneyInfoLayout;
    private LinearLayout mPromoMoneyInfoLayout;
    private LinearLayout mWinningInfoLayout;

    public WalletHomeFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof WalletHomeFragmentListener) {
            mFragmentListener = (WalletHomeFragmentListener) context;
        } else {
            throw new RuntimeException("Activity must implement " +
                    WalletHomeFragmentListener.class.getSimpleName());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_wallet_home, container, false);
        initRootView(rootView);
        return rootView;
    }

    private void initRootView(View rootView) {
        rootView.findViewById(R.id.wallet_home_earn_more_button).setOnClickListener(this);
        rootView.findViewById(R.id.wallet_home_add_money_button).setOnClickListener(this);
        rootView.findViewById(R.id.wallet_home_withdraw_button).setOnClickListener(this);
        rootView.findViewById(R.id.wallet_transaction_history_layout).setOnClickListener(this);
        rootView.findViewById(R.id.wallet_payout_detail_layout).setOnClickListener(this);
        rootView.findViewById(R.id.wallet_home_card_money_layout).setOnClickListener(this);
        rootView.findViewById(R.id.wallet_home_card_promo_layout).setOnClickListener(this);
        rootView.findViewById(R.id.wallet_home_card_winning_layout).setOnClickListener(this);
        rootView.findViewById(R.id.wallet_kyc_layout).setOnClickListener(this);

        mWalletMoneyInfoLayout = (LinearLayout) rootView.findViewById(R.id.wallet_deposit_info_layout);
        mPromoMoneyInfoLayout = (LinearLayout) rootView.findViewById(R.id.wallet_promo_info_layout);
        mWinningInfoLayout = (LinearLayout) rootView.findViewById(R.id.wallet_winning_info_layout);
        mWalletMoneyInfoLayout.setOnClickListener(this);
        mPromoMoneyInfoLayout.setOnClickListener(this);
        mWalletMoneyInfoLayout.setOnClickListener(this);
    }

    /**
     * Whenever userWallet api to be fetched/refreshed; this method is invoked
     */
    public void refreshWalletDetails() {
        fetchUserWalletFromServer();
    }

    public void refreshKYCStatus() {
        /* Fetch User Info from server to update kyc status */
        fetchUserInfo();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /* Values already fetch once before even launching this screen (at Navigation screen) */
        updateWalletDetailsOnUi();

        /* Fetch again the values and update later */
        fetchUserWalletFromServer();

        /* Fetch User Info from server to update kyc status */
        fetchUserInfo();

    }

    private void fetchUserWalletFromServer() {
        showProgressbar();
        WalletApiModelImpl.newInstance(new WalletApiModelImpl.WalletApiListener() {
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
            public void onSuccessResponse(UserWalletResponse response) {
                dismissProgressbar();
                updateWalletDetailsOnUi();
            }
        }).performApiCall();
    }

    private void fetchUserInfo() {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            UserInfoModelImpl.newInstance(new UserInfoModelImpl.OnGetUserInfoModelListener() {
                @Override
                public void onSuccessGetUpdatedUserInfo(UserInfo updatedUserInfo) {
                    /* check and update kyc status from user info */
                    checkAndUpdateKYCStatus();
                }

                @Override
                public void onFailedGetUpdateUserInfo(String message) {}

                @Override
                public void onNoInternet() {}
            }).getUserInfo();
        } else {
            Log.i(TAG, "No internet");
        }
    }


    private void updateWalletDetailsOnUi() {
        View rootView = getView();
        if (rootView != null && getActivity() != null) {
            TextView totalWalletBalanceTextView = (TextView) rootView.findViewById(R.id.wallet_home_card_total_amount_textView);
            TextView depositTextView = (TextView) rootView.findViewById(R.id.wallet_home_card_deposit_textView);
            TextView promoBalanceTextView = (TextView) rootView.findViewById(R.id.wallet_home_card_promo_amount_textView);
            TextView winningsTextView = (TextView) rootView.findViewById(R.id.wallet_home_card_winning_textView);

            depositTextView.setText(WalletHelper.getFormattedStringOfAmount(WalletHelper.getDepositAmount()));
            promoBalanceTextView.setText(WalletHelper.getFormattedStringOfAmount(WalletHelper.getPromoAmount()));
            winningsTextView.setText(WalletHelper.getFormattedStringOfAmount(WalletHelper.getWinningAmount()));
            totalWalletBalanceTextView.setText(WalletHelper.getFormattedStringOfAmount(WalletHelper.getTotalBalance()));

            // Withdraw in progress
            int withdrawInProgress = WalletHelper.getWithdrawalsInProgress();
            if (withdrawInProgress > 0) {
                TextView withdrawProgressTextView = (TextView) rootView.findViewById(R.id.wallet_home_withdraw_progress_textView);
                if (withdrawInProgress > 1) {
                    withdrawProgressTextView.setText(withdrawInProgress + " withdrawals in progress");
                } else {
                    withdrawProgressTextView.setText(withdrawInProgress + " withdrawal in progress");
                }
            }

            // Payout accounts
            int payoutAccount = WalletHelper.getPayoutAccountsAdded();
            if (payoutAccount > 0) {
                TextView payoutAccountsTextView = (TextView) rootView.findViewById(R.id.wallet_home_account_added_textView);
                if (payoutAccount > 1) {
                    payoutAccountsTextView.setText(payoutAccount + " accounts added");
                } else {
                    payoutAccountsTextView.setText(payoutAccount + " account added");
                }
            }
        }
    }

    private void checkAndUpdateKYCStatus() {
        if (getView() != null && getActivity() != null) {
            TextView kycStatusTv = (TextView) getView().findViewById(R.id.wallet_kyc_status_textView);
            TextView kycTv = (TextView) getView().findViewById(R.id.wallet_kyc_textView);
            ImageView tickIcon = (ImageView) getView().findViewById(R.id.wallet_tick_icon);
            ImageView kycIcon = (ImageView) getView().findViewById(R.id.wallet_kyc_status_icon);

            UserInfo userInfo = Nostragamus.getInstance().getServerDataManager().getUserInfo();
            if (userInfo != null && userInfo.getInfoDetails() != null && !TextUtils.isEmpty(userInfo.getInfoDetails().getKycStatus())) {
                updateKYCStatus(userInfo.getInfoDetails().getKycStatus(), kycStatusTv, kycTv, kycIcon, tickIcon);
            }
        }
    }

    private void updateKYCStatus(String kycStatusFromServer, TextView kycStatusTv, TextView kycTv, ImageView kycIcon, ImageView tickIcon) {

        String kycStatus = "";

        switch (kycStatusFromServer) {
            case Constants.KYCStatus.NOT_REQUIRED:
                kycStatus = "Get free powerups and unlimited withdrawals!";
                kycStatusTv.setTextColor(ContextCompat.getColor(getContext(), R.color.white_dim));
                break;

            case Constants.KYCStatus.REQUIRED:
                kycStatus = "Get free powerups and unlimited withdrawals!";
                kycStatusTv.setTextColor(ContextCompat.getColor(getContext(), R.color.blue_008ae1));
                break;

            case Constants.KYCStatus.UPLOADED:
                kycStatus = "Verification in Progress";
                kycStatusTv.setTextColor(ContextCompat.getColor(getContext(), R.color.blue_008ae1));
                break;

            case Constants.KYCStatus.VERIFIED:
                kycTv.setText("KYC Verified!");
                kycTv.setTextColor(ContextCompat.getColor(getContext(), R.color.paid_entry_tv_color));
                kycIcon.setVisibility(View.VISIBLE);
                tickIcon.setVisibility(View.GONE);
                kycStatusTv.setVisibility(View.GONE);
                break;

            case Constants.KYCStatus.FAILED:
                kycStatus = "KYC Rejected";
                kycStatusTv.setTextColor(ContextCompat.getColor(getContext(), R.color.radical_red));
                break;

            case Constants.KYCStatus.BLOCKED:
                kycStatus = "KYC Rejected, Please contact customer care";
                kycStatusTv.setTextColor(ContextCompat.getColor(getContext(), R.color.radical_red));
                break;

        }

        kycStatusTv.setText(kycStatus);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.wallet_home_earn_more_button:
                NostragamusAnalytics.getInstance().trackClickEvent(Constants.AnalyticsCategory.WALLET, Constants.AnalyticsClickLabels.EARN_MORE);
                onEarnMoreClicked();
                break;

            case R.id.wallet_home_add_money_button:
                NostragamusAnalytics.getInstance().trackClickEvent(Constants.AnalyticsCategory.WALLET, Constants.AnalyticsClickLabels.ADD_MONEY);
                onAddMoneyClicked();
                break;

            case R.id.wallet_home_withdraw_button:
                NostragamusAnalytics.getInstance().trackClickEvent(Constants.AnalyticsCategory.WALLET, Constants.AnalyticsClickLabels.WITHDRAW);
                onWithdrawMoneyClicked();
                break;

            case R.id.wallet_transaction_history_layout:
                NostragamusAnalytics.getInstance().trackClickEvent(Constants.AnalyticsCategory.WALLET, Constants.AnalyticsClickLabels.TRANSACTION_HISTORY);
                onTransactionHistoryClicked();
                break;

            case R.id.wallet_payout_detail_layout:
                NostragamusAnalytics.getInstance().trackClickEvent(Constants.AnalyticsCategory.WALLET, Constants.AnalyticsClickLabels.ADD_EDIT_WITHDRAWAL_DETAILS);
                onPayoutDetailsClicked();
                break;

            case R.id.wallet_home_card_money_layout:
                onMoneyInfoLayoutClicked();
                break;

            case R.id.wallet_home_card_promo_layout:
                onPayoutInfoLayoutClicked();
                break;

            case R.id.wallet_home_card_winning_layout:
                onWinningInfoLayoutClicked();
                break;

            case R.id.wallet_kyc_layout:
                onKYCClicked();
                break;
        }
    }

    private void onWinningInfoLayoutClicked() {
        if (mWinningInfoLayout.getVisibility() == View.VISIBLE) {
            AnimationHelper.collapse(mWinningInfoLayout);
        } else {
            AnimationHelper.expand(mWinningInfoLayout);
        }
    }

    private void onPayoutInfoLayoutClicked() {
        if (mPromoMoneyInfoLayout.getVisibility() == View.VISIBLE) {
            AnimationHelper.collapse(mPromoMoneyInfoLayout);
        } else {
            AnimationHelper.expand(mPromoMoneyInfoLayout);
        }
    }

    private void onMoneyInfoLayoutClicked() {
        if (mWalletMoneyInfoLayout.getVisibility() == View.VISIBLE) {
            AnimationHelper.collapse(mWalletMoneyInfoLayout);
        } else {
            AnimationHelper.expand(mWalletMoneyInfoLayout);
        }
    }

    private void onPayoutDetailsClicked() {
        if (mFragmentListener != null) {
            mFragmentListener.onPayoutDetailsClicked();
        }
    }

    private void onTransactionHistoryClicked() {
        if (mFragmentListener != null) {
            mFragmentListener.onTransactionHistoryClicked();
        }
    }

    private void onEarnMoreClicked() {
        if (mFragmentListener != null) {
            mFragmentListener.onEarnMoreClicked();
        }
    }

    private void onWithdrawMoneyClicked() {
        checkKYCStatus();
    }

    private void onAddMoneyClicked() {
        if (mFragmentListener != null) {
            mFragmentListener.onAddMoneyClicked();
        }
    }

    private void onKYCClicked() {

        String kycStatusFromServer = "";

        UserInfo userInfo = Nostragamus.getInstance().getServerDataManager().getUserInfo();
        if (userInfo != null && userInfo.getInfoDetails() != null && !TextUtils.isEmpty(userInfo.getInfoDetails().getKycStatus())) {
            kycStatusFromServer = userInfo.getInfoDetails().getKycStatus();
        }

        switch (kycStatusFromServer) {
            case Constants.KYCStatus.NOT_REQUIRED:
                if (mFragmentListener != null) {
                    mFragmentListener.onKYCClicked();
                }
                break;

            case Constants.KYCStatus.REQUIRED:
                if (mFragmentListener != null) {
                    mFragmentListener.onKYCClicked();
                }
                break;

            case Constants.KYCStatus.UPLOADED:
                if (mFragmentListener != null) {
                    mFragmentListener.onOpenKYCRequiredPopup();
                }
                break;

            case Constants.KYCStatus.VERIFIED:
                break;

            case Constants.KYCStatus.FAILED:
                if (mFragmentListener != null) {
                    mFragmentListener.onKYCClicked();
                }
                break;

            case Constants.KYCStatus.BLOCKED:
                if (mFragmentListener != null) {
                    mFragmentListener.onOpenKYCBlockedPopup();
                }
                break;

        }
    }

    private void checkKYCStatus() {

        String kycStatusFromServer = "";

        UserInfo userInfo = Nostragamus.getInstance().getServerDataManager().getUserInfo();
        if (userInfo != null && userInfo.getInfoDetails() != null && !TextUtils.isEmpty(userInfo.getInfoDetails().getKycStatus())) {
            kycStatusFromServer = userInfo.getInfoDetails().getKycStatus();
        }

        switch (kycStatusFromServer) {
            case Constants.KYCStatus.NOT_REQUIRED:
                if (mFragmentListener != null) {
                    mFragmentListener.onAddMoneyClicked();
                }
                break;

            case Constants.KYCStatus.REQUIRED:
                if (mFragmentListener != null) {
                    mFragmentListener.onKYCClicked();
                }
                break;

            case Constants.KYCStatus.UPLOADED:
                if (mFragmentListener != null) {
                    mFragmentListener.onOpenKYCRequiredPopup();
                }
                break;

            case Constants.KYCStatus.VERIFIED:
                if (mFragmentListener != null) {
                    mFragmentListener.onAddMoneyClicked();
                }
                break;

            case Constants.KYCStatus.FAILED:
                if (mFragmentListener != null) {
                    mFragmentListener.onKYCClicked();
                }
                break;

            case Constants.KYCStatus.BLOCKED:
                if (mFragmentListener != null) {
                    mFragmentListener.onOpenKYCBlockedPopup();
                }
                break;

        }

    }

}
