package in.sportscafe.nostragamus.module.navigation.wallet.payoutDetails;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.parceler.Parcels;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.navigation.wallet.WalletHelper;
import in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank.AddBankDetailsActivity;
import in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank.AddPaytmDetailsActivity;
import in.sportscafe.nostragamus.module.user.login.dto.UserPaymentInfo;
import in.sportscafe.nostragamus.utils.FragmentHelper;

public class PayoutWalletHomeActivity extends NostragamusActivity implements PayoutWalletHomeFragmentListener {

    private static final String TAG = PayoutWalletHomeActivity.class.getSimpleName();

    private interface ActivityResultRequestCode {
        int ADD_PAYTM = 11;
        int ADD_BANK = 12;
        int EDIT_PAYTM = 13;
        int EDIT_BANK = 14;
    }

    @Override
    public String getScreenName() {
        return Constants.ScreenNames.WALLET_PAYOUT_HOME;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setShouldAnimateActivity(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payout_wallet);

        initToolbar();
        loadPayOutFragment();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.payout_toolbar);
        toolbar.setTitle("Payout Details");
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.back_icon_grey);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
    }

    private void loadPayOutFragment() {
        PayoutWalletHomeFragment fragment = new PayoutWalletHomeFragment();
        FragmentHelper.replaceFragment(this, R.id.fragment_container, fragment);
    }

    @Override
    public void onAddPaytmClicked() {
        Intent intent = new Intent(this, AddPaytmDetailsActivity.class);
        startActivityForResult(intent, ActivityResultRequestCode.ADD_PAYTM);
    }

    @Override
    public void onAddBankClicked() {
        Intent intent = new Intent(this, AddBankDetailsActivity.class);
        startActivityForResult(intent, ActivityResultRequestCode.ADD_BANK);
    }

    @Override
    public void onEditPaytmClicked() {
        Intent intent = new Intent(this, AddPaytmDetailsActivity.class);
        intent.putExtras(getExtras());
        startActivityForResult(intent, ActivityResultRequestCode.EDIT_PAYTM);
    }

    @Override
    public void onEditBankClicked() {
        Intent intent = new Intent(this, AddBankDetailsActivity.class);
        intent.putExtras(getExtras());
        startActivityForResult(intent, ActivityResultRequestCode.EDIT_BANK);
    }

    private Bundle getExtras() {
        Bundle args = new Bundle();
        UserPaymentInfo userPaymentInfo = WalletHelper.getUserPaymentInfo();
        if (userPaymentInfo != null) {
            args.putParcelable(Constants.BundleKeys.USER_PAYMENT_INFO_PARCEL, Parcels.wrap(userPaymentInfo));
        }
        return  args;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK &&
                (requestCode == ActivityResultRequestCode.EDIT_BANK ||
                        requestCode == ActivityResultRequestCode.EDIT_PAYTM ||
                        requestCode == ActivityResultRequestCode.ADD_BANK ||
                        requestCode == ActivityResultRequestCode.ADD_PAYTM) ) {

            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if (fragment != null && fragment instanceof PayoutWalletHomeFragment) {
                ((PayoutWalletHomeFragment) fragment).updateScreenDetails();
            }
        }
    }
}
