package in.sportscafe.nostragamus.module.navigation.wallet;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeeva.android.BaseFragment;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.navigation.wallet.dto.UserWalletResponse;
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

    public WalletHomeFragment() {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof WalletHomeFragmentListener) {
            mFragmentListener = (WalletHomeFragmentListener) context;
        } else {
            throw  new RuntimeException("Activity must implement " +
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /* Values already fetch once before even launching this screen (at Navigation screen) */
        updateWalletDetailsOnUi();

        /* Fetch again the values and update later */
        fetchUserWalletFromServer();
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


    private void updateWalletDetailsOnUi() {
        View rootView = getView();
        if (rootView != null && getActivity() != null) {
            TextView totalWalletBalanceTextView = (TextView) rootView.findViewById(R.id.wallet_home_card_total_amount_textView);
            TextView depositTextView = (TextView) rootView.findViewById(R.id.wallet_home_card_deposit_textView);
            TextView promoBalanceTextView = (TextView) rootView.findViewById(R.id.wallet_home_card_promo_amount_textView);
            TextView winningsTextView = (TextView) rootView.findViewById(R.id.wallet_home_card_winning_textView);

            double depositAmount = WalletHelper.getDepositAmount();
            double promoAmount = WalletHelper.getPromoAmount();
            double winningAmount = WalletHelper.getWinningAmount();

            if (depositAmount > 0) {
                depositTextView.setText(WalletHelper.getFormattedStringOfAmount(depositAmount));
            }
            if (promoAmount > 0) {
                promoBalanceTextView.setText(WalletHelper.getFormattedStringOfAmount(promoAmount));
            }
            if (winningAmount > 0) {
                winningsTextView.setText(WalletHelper.getFormattedStringOfAmount(winningAmount));
            }

            double total = WalletHelper.getTotalBalance();
            if (total > 0) {
                totalWalletBalanceTextView.setText(WalletHelper.getFormattedStringOfAmount(total));
            }

            // Withdraw in progress
            int withdrawInProgress = WalletHelper.getWithdrawalsInProgress();
            if (withdrawInProgress > 0) {
                TextView withdrawProgressTextView = (TextView) rootView.findViewById(R.id.wallet_home_withdraw_progress_textView);
                if (withdrawInProgress > 1) {
                    withdrawProgressTextView.setText(withdrawInProgress + " withdrawals in progress");
                }else {
                    withdrawProgressTextView.setText(withdrawInProgress + " withdrawal in progress");
                }
            }

            // Payout accounts
            int payoutAccount = WalletHelper.getPayoutAccountsAdded();
            if (payoutAccount > 0) {
                TextView payoutAccountsTextView = (TextView) rootView.findViewById(R.id.wallet_home_account_added_textView);
                if (payoutAccount > 1) {
                    payoutAccountsTextView.setText(payoutAccount + " accounts added");
                }else {
                    payoutAccountsTextView.setText(payoutAccount + " account added");
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.wallet_home_earn_more_button:
                onEarnMoreClicked();
                break;

            case R.id.wallet_home_add_money_button:
                onAddMoneyClicked();
                break;

            case R.id.wallet_home_withdraw_button:
                onWithdrawMoneyClicked();
                break;

            case R.id.wallet_transaction_history_layout:
                onTransactionHistoryClicked();
                break;

            case R.id.wallet_payout_detail_layout:
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
        if (mFragmentListener != null) {
            mFragmentListener.onWithdrawMoneyClicked();
        }
    }

    private void onAddMoneyClicked() {
        if (mFragmentListener != null) {
            mFragmentListener.onAddMoneyClicked();
        }
    }
}
