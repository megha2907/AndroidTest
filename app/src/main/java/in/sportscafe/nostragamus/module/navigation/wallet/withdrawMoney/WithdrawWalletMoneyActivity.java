package in.sportscafe.nostragamus.module.navigation.wallet.withdrawMoney;

import android.content.Intent;
import android.os.Bundle;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.navigation.wallet.payoutDetails.PayoutWalletChoiceActivity;
import in.sportscafe.nostragamus.utils.FragmentHelper;

public class WithdrawWalletMoneyActivity extends NostragamusActivity implements WithdrawWalletMoneyFragmentListener {

    @Override
    public String getScreenName() {
        return Constants.ScreenNames.WALLET_WITHDRAW_MONEY;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setShouldAnimateActivity(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_wallet_money);

        loadWithdrawFragment();
    }

    private void loadWithdrawFragment() {
        WithdrawWalletMoneyFragment fragment = new WithdrawWalletMoneyFragment();
        FragmentHelper.replaceFragment(this, R.id.fragment_container, fragment);
    }

    @Override
    public void onBackClicked() {
        finish();
    }

    @Override
    public void onWithdrawButtonClicked(Bundle args) {
        Intent intent = new Intent(this, PayoutWalletChoiceActivity.class);
        if (args != null) {
            intent.putExtras(args);
        }

        startActivity(intent);
    }
}
