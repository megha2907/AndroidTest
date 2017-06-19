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

import in.sportscafe.nostragamus.BuildConfig;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.navigation.appupdate.AppUpdateActivity;
import in.sportscafe.nostragamus.module.common.WebViewActivity;
import in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank.WalletOrBankConnectActivity;
import in.sportscafe.nostragamus.module.navigation.help.dummygame.DummyGameActivity;
import in.sportscafe.nostragamus.module.user.myprofile.edit.EditProfileActivity;
import in.sportscafe.nostragamus.module.user.referral.ReferralActivity;

/**
 * Created by deepanshi on 2/10/17.
 */
@Deprecated
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

        TextView feedback = (TextView) findViewById(R.id.settings_tv_app_feedback);

        if (BuildConfig.IS_PAID_VERSION) {
            feedback.setText(NostragamusDataHandler.getInstance().getProFeedBack());
        } else {
            feedback.setText(NostragamusDataHandler.getInstance().getFeedBack());
        }

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
            case R.id.settings_btn_game_play:
                navigateToWebView("http://nostragamus.in/gameplayapp.html", "Gameplay");
                break;
            case R.id.settings_btn_rules:
                navigateToWebView("http://nostragamus.in/rulesapp.html", "Rules");
                break;
            case R.id.settings_btn_faq:
                navigateToWebView("http://nostragamus.in/faqapp.html", "FAQ");
                break;
            case R.id.settings_btn_about_us:
                navigateToWebView("http://nostragamus.in/about.html", "About NostraGamus");
                break;
            case R.id.settings_btn_terms_service:
                navigateToWebView("http://nostragamus.in/terms.html", "Terms of Service");
                break;
            case R.id.settings_btn_privacy:
                navigateToWebView("http://nostragamus.in/privacy.html", "Privacy Policy");
                break;
        }
    }

    private void navigateToTourList() {
        Intent intent = new Intent(this, AppUpdateActivity.class);
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
        intent.putExtra(Constants.BundleKeys.EDIT_PROFILE_LAUNCHED_FROM, EditProfileActivity.ILaunchedFrom.SETTINGS_ACTIVITY);
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