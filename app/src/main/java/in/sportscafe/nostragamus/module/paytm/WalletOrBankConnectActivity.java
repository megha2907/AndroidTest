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
        startActivity(new Intent(this, AddPaymentBankActivity.class));
    }

    private void navigateToAddPaytmDetail() {
        startActivity(new Intent(this, AddPaytmDetailsActivity.class));
    }

    public void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.paytm_connect_toolbar);
        toolbar.setTitle("Add a wallet");
        setSupportActionBar(toolbar);
    }
}