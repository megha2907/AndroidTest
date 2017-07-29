package in.sportscafe.nostragamus.module.navigation.powerupbank.earnmorepowerups;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.navigation.powerupbank.PowerUpBankFragment;
import in.sportscafe.nostragamus.module.navigation.referfriends.ReferFriendActivity;
import in.sportscafe.nostragamus.module.store.StoreActivity;
import in.sportscafe.nostragamus.utils.FragmentHelper;

/**
 * Created by deepanshi on 7/29/17.
 */

public class EarnMorePowerUpActivity extends NostragamusActivity implements EarnMorePowerUpFragmentListener {

    @Override
    public String getScreenName() {
        return Constants.ScreenNames.EARN_MORE_POWERUPS;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setShouldAnimateActivity(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earn_more_powerup);

        initialize();
        loadEarnMorePowerUpFragment();
    }

    private void initialize() {
        initToolbar();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.em_powerup_toolbar);
        TextView tvToolbar = (TextView) findViewById(R.id.em_powerup_toolbar_tv);
        tvToolbar.setText("Earn More Powerups");

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

    private void loadEarnMorePowerUpFragment() {
        EarnMorePowerUpFragment fragment = new EarnMorePowerUpFragment();
        FragmentHelper.replaceFragment(this, R.id.fragment_container, fragment);
    }

    @Override
    public void onStoreClicked() {
        if (getActivity() != null) {
            Intent intent = new Intent(getActivity(), StoreActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onReferAFriendClicked() {
        if (getActivity() != null) {
            Intent intent = new Intent(getActivity(), ReferFriendActivity.class);
            startActivity(intent);
        }
    }

}
