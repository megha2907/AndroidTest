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
public class PaytmConnectActivity extends NostragamusActivity {

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

    public void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.paytm_connect_toolbar);
        toolbar.setTitle("Add a wallet");
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
}