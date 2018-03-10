package in.sportscafe.nostragamus.module.prediction.powerupBank;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.store.StoreActivity;
import in.sportscafe.nostragamus.utils.FragmentHelper;

public class PowerupBankTransferToPlayActivity extends NostragamusActivity implements
        View.OnClickListener, PowerUpBankTransferFragmentListener {

    private static final int STORE_REQUEST_CODE = 551;

    private PowerupBankTransferInfoFragment mInfoFragment;

    @Override
    public String getScreenName() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setShouldAnimateActivity(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_powerup_bank_transfer);
        setImmersiveFullScreenMode();
        initVisibleScreenLayout();

//        loadInfoFragment();
        loadPowerupFragment();
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setImmersiveFullScreenMode();
    }

    private void initViews() {
        ImageView closeButton = (ImageView) findViewById(R.id.powerup_bank_close_imgView);
        closeButton.setOnClickListener(this);
    }

    private void loadPowerupFragment() {
        PowerupBankTransferFragment fragment = new PowerupBankTransferFragment();
        if (getIntent() != null && getIntent().getExtras() != null) {
            fragment.setArguments(getIntent().getExtras());
        }

        FragmentHelper.replaceFragment(this, R.id.powerup_bank_fragment_container, fragment);
    }

    /*private void loadInfoFragment() {
        mInfoFragment = new PowerupBankTransferInfoFragment();
        FragmentHelper.replaceFragment(this, R.id.powerup_bank_info_fragment_container, mInfoFragment);
    }*/

    private void initVisibleScreenLayout() {
        try {
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getRealMetrics(dm);

            RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) findViewById(R.id.transfer_powerup_bottom_empty_layout).getLayoutParams();
            rlp.height = (int) getResources().getDimension(R.dimen.dim_68); //(dm.heightPixels * 0.1);

            int total = dm.heightPixels - rlp.height;
            rlp = (RelativeLayout.LayoutParams) findViewById(R.id.transfer_powerup_top_layout).getLayoutParams();
            rlp.height = total;

        } catch (Exception ex) {}
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.powerup_bank_close_imgView:
                finishActivity(new Bundle());
                break;
        }
    }

    @Override
    public void updatePowerUpInfoDetails(Bundle args) {
        if (mInfoFragment != null) {
            mInfoFragment.updateInfoDetails(args);
        }
    }

    @Override
    public void finishActivity(Bundle args) {
        setResult(RESULT_OK, new Intent().putExtras(args));
        onBackPressed();
    }

    @Override
    public void launchStore(Bundle args) {
        Intent intent = new Intent(getActivity(), StoreActivity.class);
        if (args != null) {
            intent.putExtras(args);
        }
        startActivityForResult(intent, STORE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == STORE_REQUEST_CODE) {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.powerup_bank_fragment_container);
            if (fragment != null && fragment instanceof PowerupBankTransferFragment) {
                ((PowerupBankTransferFragment) fragment).onStoreResponse();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.powerup_bank_fragment_container);
        if (fragment != null && fragment instanceof PowerupBankTransferFragment) {
            ((PowerupBankTransferFragment) fragment).onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void cancelTransactionAndGoBack() {
        super.onBackPressed();
    }
}
