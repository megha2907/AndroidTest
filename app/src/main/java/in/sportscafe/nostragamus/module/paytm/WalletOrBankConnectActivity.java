package in.sportscafe.nostragamus.module.paytm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import in.sportscafe.nostragamus.Constants.ScreenNames;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;

/**
 * Created by Jeeva on 23/03/17.
 */
public class WalletOrBankConnectActivity extends NostragamusActivity {

    private static final int BANK_REQUEST_CODE = 1001;
    private static final int PAYTM_REQUEST_CODE = 1002;

    @Override
    public String getScreenName() {
        return ScreenNames.PAYTM_CONNECT;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paytm_connect);

        initToolBar();
    }

    /**
     * called from xml layout
     * @param view
     */
    public void onClickConnect(View view) {
        navigateToAddPaytmDetail();
    }

    /**
     * Invoked from xml directly
     * @param view
     */
    public void onClickNoPaytmAccount(View view) {
        navigateToAddBankDetails();
    }

    /**
     * Invoked from xml
     * @param view
     */
    public void onClickSkip(View view) {
        onBackPressed();
    }

    private void navigateToAddBankDetails() {
        startActivityForResult(new Intent(this, AddPaymentBankActivity.class), BANK_REQUEST_CODE);
    }

    private void navigateToAddPaytmDetail() {
        startActivityForResult(new Intent(this, AddPaytmDetailsActivity.class), PAYTM_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && (requestCode == BANK_REQUEST_CODE || requestCode == PAYTM_REQUEST_CODE)) {
            onBackPressed();
        }
    }

    public void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.paytm_connect_toolbar);
        toolbar.setTitle("Add a wallet");
        setSupportActionBar(toolbar);
    }
}