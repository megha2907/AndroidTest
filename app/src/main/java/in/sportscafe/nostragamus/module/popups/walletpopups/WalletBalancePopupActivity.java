package in.sportscafe.nostragamus.module.popups.walletpopups;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeeva.android.BaseActivity;
import com.jeeva.android.Log;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.navigation.wallet.WalletApiModelImpl;
import in.sportscafe.nostragamus.module.navigation.wallet.WalletHelper;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.AddWalletMoneyActivity;
import in.sportscafe.nostragamus.module.navigation.wallet.dto.UserWalletResponse;
import in.sportscafe.nostragamus.module.popups.PopUpDialogActivity;
import in.sportscafe.nostragamus.utils.AnimationHelper;

/**
 * Created by deepanshi on 8/25/17.
 */

public class WalletBalancePopupActivity extends PopUpDialogActivity implements View.OnClickListener {

    private static final String TAG = WalletBalancePopupActivity.class.getSimpleName();
    private LinearLayout mWalletMoneyInfoLayout;
    private LinearLayout mPromoMoneyInfoLayout;
    private LinearLayout mWinningInfoLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_balance_popup);
        initView();
        setWalletInfo();
    }

    private void initView() {
        (findViewById(R.id.popup_cross_btn)).setOnClickListener(this);
        (findViewById(R.id.wallet_balance_add_money_btn)).setOnClickListener(this);
        (findViewById(R.id.popup_bg)).setOnClickListener(this);
    }

    private void setWalletInfo() {
        mWalletMoneyInfoLayout = (LinearLayout)findViewById(R.id.wallet_deposit_info_layout);
        mPromoMoneyInfoLayout = (LinearLayout)findViewById(R.id.wallet_promo_info_layout);
        mWinningInfoLayout = (LinearLayout) findViewById(R.id.wallet_winning_info_layout);
        mWalletMoneyInfoLayout.setOnClickListener(this);
        mPromoMoneyInfoLayout.setOnClickListener(this);
        mWalletMoneyInfoLayout.setOnClickListener(this);
        findViewById(R.id.wallet_popup_card_deposit_layout).setOnClickListener(this);
        findViewById(R.id.wallet_popup_card_promo_layout).setOnClickListener(this);
        findViewById(R.id.wallet_popup_card_winnings_layout).setOnClickListener(this);

        populateWalletDetails();
    }

    private void populateWalletDetails() {
        if (!isFinishing()) {
            TextView tvBalanceAmount = (TextView) findViewById(R.id.header_textView_wallet_amount);
            TextView depositTextView = (TextView) findViewById(R.id.wallet_balance_deposit_tv);
            TextView promoBalanceTextView = (TextView) findViewById(R.id.wallet_balance_promo_tv);
            TextView winningsTextView = (TextView) findViewById(R.id.wallet_balance_winnings_tv);

            double walletBalAmount = WalletHelper.getTotalBalance();
            tvBalanceAmount.setText(WalletHelper.getFormattedStringOfAmount(walletBalAmount));
            depositTextView.setText(WalletHelper.getFormattedStringOfAmount(WalletHelper.getDepositAmount()));
            promoBalanceTextView.setText(WalletHelper.getFormattedStringOfAmount(WalletHelper.getPromoAmount()));
            winningsTextView.setText(WalletHelper.getFormattedStringOfAmount(WalletHelper.getWinningAmount()));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.popup_cross_btn:
                onBackPressed();
                break;

            case R.id.wallet_balance_add_money_btn:
                onAddMoneyClicked();
                break;

            case R.id.wallet_popup_card_deposit_layout:
                onMoneyInfoLayoutClicked();
                break;

            case R.id.wallet_popup_card_promo_layout:
                onPayoutInfoLayoutClicked();
                break;

            case R.id.wallet_popup_card_winnings_layout:
                onWinningInfoLayoutClicked();
                break;

            case R.id.popup_bg:
                onBackPressed();
                break;
        }
    }

    private void onAddMoneyClicked() {
        Intent intent = new Intent(this, AddWalletMoneyActivity.class);
        startActivity(intent);
        NostragamusAnalytics.getInstance().trackClickEvent(Constants.AnalyticsCategory.WALLET_POPUP, Constants.AnalyticsClickLabels.DEPOSIT_MONEY);

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

    @Override
    protected void onResume() {
        super.onResume();
        fetchUserWalletFromServer();
    }

    private void fetchUserWalletFromServer() {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            WalletApiModelImpl.newInstance(new WalletApiModelImpl.WalletApiListener() {
                @Override
                public void noInternet() {
                    Log.d(TAG, Constants.Alerts.NO_NETWORK_CONNECTION);
                }

                @Override
                public void onApiFailed() {
                    Log.d(TAG, Constants.Alerts.API_FAIL);
                }

                @Override
                public void onSuccessResponse(UserWalletResponse response) {
                    populateWalletDetails();
                }
            }).performApiCall();
        }
    }


}
