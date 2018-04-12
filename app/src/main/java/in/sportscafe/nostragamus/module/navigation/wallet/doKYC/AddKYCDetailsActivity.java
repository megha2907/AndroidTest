package in.sportscafe.nostragamus.module.navigation.wallet.doKYC;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.addphoto.AddPhotoActivity;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.customViews.CustomSnackBar;
import in.sportscafe.nostragamus.module.navigation.wallet.WalletHomeFragment;
import in.sportscafe.nostragamus.module.permission.PermissionsActivity;
import in.sportscafe.nostragamus.utils.FragmentHelper;

/**
 * Created by deepanshi on 3/27/18.
 */

public class AddKYCDetailsActivity extends NostragamusActivity implements AddKYCDetailsFragmentListener {


    @Override
    public String getScreenName() {
        return Constants.Notifications.SCREEN_KYC_DETAILS;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setShouldAnimateActivity(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_kyc_details);

        initToolbar();
        loadKYCFragment();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);

       // notifyWalletHomeFragment();
    }

    private void notifyWalletHomeFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment != null && fragment instanceof WalletHomeFragment) {
            ((WalletHomeFragment) fragment).refreshWalletDetails();
        }
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.kyc_toolbar);
        TextView tvToolbar = (TextView) findViewById(R.id.kyc_toolbar_tv);
        tvToolbar.setText("Update your KYC");
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

    private void loadKYCFragment() {
        Bundle args = null;
        if (getIntent() != null) {
            args = getIntent().getExtras();
        }

        AddKYCDetailsFragment addKYCDetailsFragment = new AddKYCDetailsFragment();
        addKYCDetailsFragment.setArguments(args);
        FragmentHelper.replaceFragment(this, R.id.fragment_container, addKYCDetailsFragment);
    }

    @Override
    public void navigateToAddPhotoActivity(int requestCode) {
        startActivityForResult(new Intent(this, AddPhotoActivity.class), requestCode);
    }

    @Override
    public void startPermissionActivity() {
        PermissionsActivity.startActivityForResult(this, Constants.RequestCodes.STORAGE_PERMISSION, Constants.AppPermissions.STORAGE);
    }

    @Override
    public void handleError(String msg, int status) {
        if (!getActivity().isFinishing()) {
            View view = findViewById(R.id.add_kyc_root_layout);

            if (view != null) {
                if (!TextUtils.isEmpty(msg)) {
                    CustomSnackBar.make(view, msg, CustomSnackBar.DURATION_SECS_5).show();
                } else {
                    switch (status) {
                        case Constants.DataStatus.NO_INTERNET:
                            CustomSnackBar.make(view, Constants.Alerts.NO_NETWORK_CONNECTION, CustomSnackBar.DURATION_SECS_5).show();
                            break;

                        case Constants.DataStatus.COULD_NOT_LOAD_IMAGE:
                            CustomSnackBar.make(view, Constants.Alerts.COULD_NOT_LOAD_IMAGE, CustomSnackBar.DURATION_SECS_5).show();
                            break;

                        default:
                            CustomSnackBar.make(view, Constants.Alerts.SOMETHING_WRONG, CustomSnackBar.DURATION_SECS_5).show();
                            break;
                    }
                }
            }
        }
    }

    @Override
    public void finishThisAndGotoWalletScreen() {
        setResult(RESULT_OK);
        finish();
    }
}
