package in.sportscafe.nostragamus.module.navigation.wallet.addMoney.lowBalance;

import android.content.Intent;
import android.os.Bundle;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.utils.FragmentHelper;

public class AddMoneyOnLowBalanceActivity extends NostragamusActivity implements AddMoneyOnLowBalanceFragmentListener {

    @Override
    public String getScreenName() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setShouldAnimateActivity(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_money_on_low_balance);

        loadAddMoneyOnLowBalFragment();
    }

    private void loadAddMoneyOnLowBalFragment() {
        AddMoneyOnLowBalanceFragment fragment = new AddMoneyOnLowBalanceFragment();
        if (getIntent() != null) {
            fragment.setArguments(getIntent().getExtras());
        }
        FragmentHelper.replaceFragment(this, R.id.fragment_container, fragment);
    }

    @Override
    public void onBackClicked() {
        finish();
    }

    @Override
    public void onSuccess(Bundle args) {
        Intent intent = new Intent();
        if (args != null) {
            intent.putExtras(args);
        }

        setResult(RESULT_OK, intent);
        finish();
    }
}