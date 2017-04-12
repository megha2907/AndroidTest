package in.sportscafe.nostragamus.module.settings;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.WebViewActivity;
import in.sportscafe.nostragamus.module.paytm.WalletOrBankConnectActivity;
import in.sportscafe.nostragamus.module.play.dummygame.DummyGameActivity;
import in.sportscafe.nostragamus.module.question.tourlist.TourListActivity;
import in.sportscafe.nostragamus.module.user.myprofile.edit.EditProfileActivity;
import in.sportscafe.nostragamus.module.user.referral.ReferralActivity;

/**
 * Created by deepanshi on 2/10/17.
 */
public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout mLlActionLayout;

    private Toolbar mtoolbar;

    private static final int EDIT_PROFILE_CODE = 35;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_screen);

        initToolBar();
        getAppVersion();

        mLlActionLayout = (LinearLayout) findViewById(R.id.settings_ll_action_layout);
    }

    public void initToolBar() {
        mtoolbar = (Toolbar) findViewById(R.id.settings_toolbar);
        mtoolbar.setTitle("Settings");
        setSupportActionBar(mtoolbar);
        mtoolbar.setNavigationIcon(R.drawable.back_icon_grey);
        mtoolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.settings_btn_edit_profile:
                navigateToEditProfile();
                break;
            case R.id.settings_btn_logout:
                onClickLogout();
                break;
            case R.id.settings_btn_refer_friend:
                navigateToReferFriend();
                break;
            case R.id.settings_btn_dummy_game:
                navigateToDummyGame();
                break;
            case R.id.settings_btn_submit_question:
                navigateToTourList();
                break;
            case R.id.settings_btn_paytm:
                navigateToPaytm();
                break;
            case R.id.settings_btn_about_us:
                navigateToWebView("https://sportscafe.in/aboutus", "About Sportscafe");
                break;
            case R.id.settings_btn_terms_service:
                navigateToWebView("https://sportscafe.in/termsandconditions", "Terms of Service");
                break;
            case R.id.settings_btn_privacy:
                navigateToWebView("https://sportscafe.in/privacy", "Privacy Policy");
                break;
        }
    }

    private void navigateToTourList() {
        Intent intent = new Intent(this, TourListActivity.class);
        startActivity(intent);
    }

    private void navigateToPaytm() {
        Intent intent = new Intent(this, WalletOrBankConnectActivity.class);
        startActivity(intent);
    }

    private void getAppVersion() {
        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String versionName = pInfo.versionName;
        ((TextView) findViewById(R.id.settings_tv_version))
                .setText(versionName);
    }

    private void navigateToEditProfile() {
        Intent intent = new Intent(this, EditProfileActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("screen", Constants.BundleKeys.HOME_SCREEN);
        intent.putExtras(bundle);
        startActivityForResult(intent, EDIT_PROFILE_CODE);
    }

    private void onClickLogout() {
        Nostragamus.getInstance().logout();
    }

    private void navigateToWebView(String url, String heading) {
        Intent intent = new Intent(SettingActivity.this, WebViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        bundle.putString("heading", heading);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void navigateToDummyGame() {
        Intent intent = new Intent(this, DummyGameActivity.class);
        startActivity(intent);
    }

    private void navigateToReferFriend() {
        Intent intent = new Intent(this, ReferralActivity.class);
        startActivity(intent);
    }
}