package in.sportscafe.nostragamus.module.navigation.settings.about;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.utils.FragmentHelper;

public class AboutActivity extends NostragamusActivity implements AboutFragmentListener {

    @Override
    public String getScreenName() {
        return Constants.ScreenNames.ABOUT;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setShouldAnimateActivity(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        initialize();
        loadAboutFragment();
    }

    private void initialize() {
        initToolbar();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.about_toolbar);
        toolbar.setTitle("About");
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

    private void loadAboutFragment() {
        AboutFragment fragment = new AboutFragment();
        FragmentHelper.replaceFragment(this, R.id.fragment_container, fragment);
    }

    @Override
    public void onTermsOfServiceClicked() {
        navigateToWebView(Constants.WebPageUrls.TERMS_SERVICE, "Terms of Service");
    }

    @Override
    public void onAboutNostragamusClicked() {
        navigateToWebView(Constants.WebPageUrls.ABOUT_NOSTRAGAMUS, "About NostraGamus");
    }

    @Override
    public void onPrivacyClicked() {
        navigateToWebView(Constants.WebPageUrls.PRIVACY, "Privacy Policy");
    }

}
