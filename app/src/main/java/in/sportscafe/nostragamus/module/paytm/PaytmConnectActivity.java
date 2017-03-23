package in.sportscafe.nostragamus.module.paytm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import in.sportscafe.nostragamus.Constants.ScreenNames;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;

/**
 * Created by Jeeva on 23/03/17.
 */
public class PaytmConnectActivity extends NostragamusActivity {

    @Override
    public String getScreenName() {
        return ScreenNames.PAYTM_CONNECT;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paytm_connect);
    }

    public void onClickConnect(View view) {
        navigateToAddPaytmDetail();
    }

    public void onClickSkip(View view) {
        onBackPressed();
    }

    private void navigateToAddPaytmDetail() {
        startActivity(new Intent(this, PaytmAddDetailActivity.class));
        onBackPressed();
    }
}