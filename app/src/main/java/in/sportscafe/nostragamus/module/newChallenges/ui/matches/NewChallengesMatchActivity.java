package in.sportscafe.nostragamus.module.newChallenges.ui.matches;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.parceler.Parcels;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostraBaseActivity;
import in.sportscafe.nostragamus.module.contest.ui.ContestsActivity;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayMatch;
import in.sportscafe.nostragamus.module.navigation.wallet.WalletHelper;
import in.sportscafe.nostragamus.utils.FragmentHelper;

/**
 * Created by deepanshi on 9/1/17.
 */

public class NewChallengesMatchActivity extends NostraBaseActivity implements NewChallengeMatchFragmentListener, View.OnClickListener {

    /*@Override
    public String getScreenName() {
        return Constants.ScreenNames.MATCHES_TIMELINE;
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setShouldAnimateActivity(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches_timeline);

        initialize();
        loadMatchesTimelineFragment();
    }

    private void initialize() {
        initToolbar();
    }

    private void initToolbar() {
        TextView toolbarHeadingOne = (TextView) findViewById(R.id.toolbar_heading_one);
        TextView toolbarHeadingTwo = (TextView) findViewById(R.id.toolbar_heading_two);
        TextView toolbarWalletMoney = (TextView) findViewById(R.id.toolbar_wallet_money);
        ImageView mBackBtn = (ImageView)findViewById(R.id.newchallenge_matches_back_btn);
        mBackBtn.setOnClickListener(this);

        toolbarHeadingOne.setText("Games");
        int amount = (int) WalletHelper.getTotalBalance();
        toolbarWalletMoney.setText(String.valueOf(amount));
    }

    private void loadMatchesTimelineFragment() {
        Bundle args = null;
        if (getIntent() != null) {
            args = getIntent().getExtras();
        }
        NewChallengesMatchesFragment newChallengesMatchesFragment = new NewChallengesMatchesFragment();
        newChallengesMatchesFragment.setArguments(args);
        FragmentHelper.replaceFragment(this, R.id.fragment_container, newChallengesMatchesFragment);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.newchallenge_matches_back_btn:
                onBackClicked();
                break;
        }
    }
}
