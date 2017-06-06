package in.sportscafe.nostragamus.module.navigation.help;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.navigation.help.dummygame.DummyGameActivity;
import in.sportscafe.nostragamus.utils.FragmentHelper;

public class HelpActivity extends NostragamusActivity implements HelpFragmentListener {

    @Override
    public String getScreenName() {
        return Constants.ScreenNames.HELP;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setShouldAnimateActivity(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        initialize();
        loadHelpFragment();
    }

    private void initialize() {
        initToolbar();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.help_toolbar);
        toolbar.setTitle("Help");
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

    private void loadHelpFragment() {
        HelpFragment fragment = new HelpFragment();
        FragmentHelper.replaceFragment(this, R.id.fragment_container, fragment);
    }

    @Override
    public void onRulesClicked() {
        navigateToWebView(Constants.WebPageUrls.RULES, "Rules");
    }

    @Override
    public void onGamePlayClicked() {
        navigateToWebView(Constants.WebPageUrls.GAME_PLAY, "Gameplay");
    }

    @Override
    public void onFaqClicked() {
        navigateToWebView(Constants.WebPageUrls.FAQ, "FAQ");
    }

    @Override
    public void onPlaySampleGameClicked() {
        Intent intent = new Intent(this, DummyGameActivity.class);
        startActivity(intent);
    }
}
