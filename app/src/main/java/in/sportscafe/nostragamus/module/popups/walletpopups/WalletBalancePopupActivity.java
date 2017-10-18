package in.sportscafe.nostragamus.module.popups.walletpopups;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.jeeva.android.BaseActivity;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.navigation.wallet.WalletHelper;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.AddWalletMoneyActivity;

/**
 * Created by deepanshi on 8/25/17.
 */

public class WalletBalancePopupActivity extends BaseActivity implements View.OnClickListener {

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
    }

    private void setWalletInfo() {
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.popup_cross_btn:
                onBackPressed();
                break;

            case R.id.wallet_balance_add_money_btn:
                onAddMoneyClicked();
                break;
        }
    }

    private void onAddMoneyClicked() {
        Intent intent = new Intent(this, AddWalletMoneyActivity.class);
        startActivity(intent);
    }
}
