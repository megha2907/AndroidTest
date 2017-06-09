package in.sportscafe.nostragamus.module.navigation.wallet.walletHistory;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.utils.FragmentHelper;

public class WalletHistoryActivity extends NostragamusActivity implements WalletHistoryFragmentListener {

    @Override
    public String getScreenName() {
        return Constants.ScreenNames.WALLET_HISTORY;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setShouldAnimateActivity(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_history);

        loadHistoryFragment();
    }

    private void loadHistoryFragment() {
        WalletHistoryFragment fragment = WalletHistoryFragment.newInstance();
        FragmentHelper.replaceFragment(this, R.id.fragment_container, fragment);
    }

    @Override
    public void onBackClicked() {
        finish();
    }
}
