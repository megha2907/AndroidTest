package in.sportscafe.nostragamus.module.navigation.wallet.addMoney.selectPaymentMode;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.parceler.Parcels;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.contest.dto.JoinContestData;
import in.sportscafe.nostragamus.module.contest.ui.ContestsActivity;
import in.sportscafe.nostragamus.module.navigation.wallet.WalletHomeActivity;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.addByCashFree.AddMoneyByCashFreeHelper;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.addByPaytm.AddMoneyPaytmFragmentLaunchedFrom;
import in.sportscafe.nostragamus.utils.FragmentHelper;

/**
 * Created by deepanshi on 2/23/18.
 */

public class SelectPaymentModeActivity extends NostragamusActivity implements SelectPaymentModeFragmentListener{

    @Override
    public String getScreenName() {
        return Constants.ScreenNames.SELECT_PAYMENT_MODE;
    }

    private SelectPaymentModeFragment mSelectPaymentModeFragment;


    public interface LaunchedFrom {
        int ADD_MONEY_INTO_WALLET = 300;
        int ADD_MONEY_LOW_BALANCE = 301;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setShouldAnimateActivity(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_payment_mode);

        initialize();
        loadSelectPaymentModeFragment();
    }

    private void initialize() {
        initToolbar();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.select_payment_toolbar);
        TextView tvToolbar = (TextView) findViewById(R.id.select_payment_toolbar_tv);
        tvToolbar.setText("Select Payment Mode");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationIcon(R.drawable.back_icon_grey);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                }
        );
    }

    private void loadSelectPaymentModeFragment() {
        Bundle args = null;
        if (getIntent() != null && getIntent().getExtras() != null) {
            args = getIntent().getExtras();
        }

        mSelectPaymentModeFragment = new SelectPaymentModeFragment();
        if (args != null) {
            mSelectPaymentModeFragment.setArguments(args);
        }

        FragmentHelper.replaceFragment(this, R.id.fragment_container, mSelectPaymentModeFragment);
    }

    @Override
    public void onBackClicked() {
        onBackPressed();
    }

    @Override
    public void onMoneyAddedToWalletSuccess() {

        if (getActivity() != null && getActivity().getIntent() != null
                && getActivity().getIntent().getExtras() != null) {

            int launchFrom = getIntent().getIntExtra(Constants.BundleKeys.SCREEN_LAUNCHED_FROM_PARENT, -1);
            switch (launchFrom) {
                case LaunchedFrom.ADD_MONEY_LOW_BALANCE:
                    navigateToContests(getIntent().getExtras());
                    break;
                case LaunchedFrom.ADD_MONEY_INTO_WALLET:
                    navigateToWallet();
                    break;
            }

        } else {
            navigateToWallet();
        }

    }

    private void navigateToContests(Bundle args) {
        if (getActivity() != null && args != null && args.containsKey(Constants.BundleKeys.JOIN_CONTEST_DATA)) {
            JoinContestData joinContestData = Parcels.unwrap(args.getParcelable(Constants.BundleKeys.JOIN_CONTEST_DATA));

            Bundle bundle = new Bundle();
            bundle.putParcelable(Constants.BundleKeys.JOIN_CONTEST_DATA, Parcels.wrap(joinContestData));

            Intent intent = new Intent(this, ContestsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtras(bundle);
            intent.putExtra(Constants.BundleKeys.SCREEN_LAUNCH_REQUEST, ContestsActivity.LaunchedFrom.SELECT_PAYMENT_MODE);
            startActivity(intent);
        }
    }

    private void navigateToWallet() {
        if (getActivity() != null) {
            Intent intent = new Intent(this, WalletHomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}
