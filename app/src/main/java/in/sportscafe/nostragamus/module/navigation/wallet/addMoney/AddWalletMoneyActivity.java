package in.sportscafe.nostragamus.module.navigation.wallet.addMoney;

import android.os.Bundle;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.utils.FragmentHelper;

public class AddWalletMoneyActivity extends NostragamusActivity implements AddWalletMoneyFragmentListener {

    @Override
    public String getScreenName() {
        return Constants.ScreenNames.WALLET_ADD_MONEY;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setShouldAnimateActivity(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wallet_money);

        loadAddMoneyFragment();
    }

    private void loadAddMoneyFragment() {
        AddWalletMoneyFragment fragment = new AddWalletMoneyFragment();
        FragmentHelper.replaceFragment(this, R.id.fragment_container, fragment);
    }

    @Override
    public void onBackClicked() {
        finish();
    }
}
