package in.sportscafe.nostragamus.module.navigation.powerupbank.powerupbanktransaction;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.parceler.Parcels;

import java.util.HashMap;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.navigation.referfriends.referralcredits.ReferralCreditFragment;
import in.sportscafe.nostragamus.module.navigation.referfriends.referralcredits.ReferralCreditFragmentListener;
import in.sportscafe.nostragamus.module.user.powerups.PowerUp;
import in.sportscafe.nostragamus.utils.FragmentHelper;
import in.sportscafe.nostragamus.webservice.UserReferralInfo;

/**
 * Created by deepanshi on 7/13/17.
 */

public class PBTransactionActivity extends NostragamusActivity implements PBTransactionFragmentListener {

    @Override
    public String getScreenName() {
        return Constants.ScreenNames.REFERRAL_CREDIT;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setShouldAnimateActivity(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pb_transaction);

        initialize();
        loadPBTransactionFragment();
    }

    private void initialize() {
        initToolbar();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.pb_transaction_toolbar);
        TextView tvToolbar = (TextView) findViewById(R.id.pb_transaction_toolbar_tv);
        tvToolbar.setText("Transaction History");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

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

    private void loadPBTransactionFragment() {
        Bundle args = null;
        if (getIntent() != null) {
            args = getIntent().getExtras();
        }
        PBTransactionFragment pbTransactionFragment = new PBTransactionFragment();
        pbTransactionFragment.setArguments(args);
        FragmentHelper.replaceFragment(this, R.id.fragment_container, pbTransactionFragment);
    }

}
