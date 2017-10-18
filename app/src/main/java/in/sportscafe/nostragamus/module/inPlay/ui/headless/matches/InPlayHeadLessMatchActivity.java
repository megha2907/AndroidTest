package in.sportscafe.nostragamus.module.inPlay.ui.headless.matches;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostraBaseActivity;
import in.sportscafe.nostragamus.module.contest.ui.ContestsActivity;
import in.sportscafe.nostragamus.module.popups.walletpopups.WalletBalancePopupActivity;
import in.sportscafe.nostragamus.utils.FragmentHelper;

public class InPlayHeadLessMatchActivity extends NostraBaseActivity implements InPlayHeadLessMatchFragmentListener {

    @Override
    public String getScreenName() {
        return Constants.Notifications.SCREEN_IN_PLAY_HEADLESS_GAMES;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setShouldAnimateActivity(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inplay_headless_matches);

        loadMatchesTimelineFragment();
    }

    private void loadMatchesTimelineFragment() {
        Bundle args = null;
        if (getIntent() != null) {
            args = getIntent().getExtras();
        }
        InPlayHeadLessMatchesFragment inPlayHeadLessMatchesFragment = new InPlayHeadLessMatchesFragment();
        inPlayHeadLessMatchesFragment.setArguments(args);
        FragmentHelper.replaceFragment(this, R.id.fragment_container, inPlayHeadLessMatchesFragment);
    }

    @Override
    public void onBackClicked() {
        onBackPressed();
    }

    @Override
    public void launchContestActivity(int launchedFrom, Bundle args) {
        Intent intent = new Intent(this, ContestsActivity.class);
        if (args != null) {
            intent.putExtras(args);
        }
        startActivity(intent);
    }

    @Override
    public void onWalletClicked() {
        Intent intent = new Intent(this, WalletBalancePopupActivity.class);
        startActivity(intent);
    }
}
