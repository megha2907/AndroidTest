package in.sportscafe.nostragamus.module.navigation.wallet.payoutDetails;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank.AddPaymentBankActivity;
import in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank.AddPaytmDetailsActivity;
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
        return null; //Constants.ScreenNames.WALLET_PAYOUT_DETAILS;
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
        Intent intent = new Intent(this, AddPaymentBankActivity.class);
        startActivityForResult(intent, ActivityResultRequestCode.ADD_BANK);
    }

    @Override
    public void onEditPaytmClicked() {
        Intent intent = new Intent(this, AddPaytmDetailsActivity.class);
        // TODO : add userPaymentInfo into bundle... to make it edit
        startActivityForResult(intent, ActivityResultRequestCode.EDIT_PAYTM);
    }

    @Override
    public void onEditBankClicked() {
        Intent intent = new Intent(this, AddPaymentBankActivity.class);
        // TODO : add userPaymentInfo into bundle... to make it edit
        startActivityForResult(intent, ActivityResultRequestCode.EDIT_BANK);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_OK) {
            // Refresh screen
        }
    }
}
