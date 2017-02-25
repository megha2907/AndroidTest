package in.sportscafe.nostragamus.module.othersanswers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.parceler.Parcels;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.feed.dto.Match;
import in.sportscafe.nostragamus.module.fuzzyplayers.FuzzyPlayerFragment;
import in.sportscafe.nostragamus.module.popups.GetScreenNameListener;

/**
 * Created by Jeeva on 11/01/17.
 */

public class OthersAnswersActivity extends NostragamusActivity {

    private FuzzyPlayerFragment mFuzzyPlayersFragment;

    private Match mMatchDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others_answers);

        Bundle bundle = getIntent().getExtras();

        String userName = "Other";
        if (bundle.containsKey(BundleKeys.PLAYER_USER_NAME)) {
            userName = bundle.getString(BundleKeys.PLAYER_USER_NAME);
        }

        mMatchDetails = Parcels.unwrap(bundle.getParcelable(BundleKeys.MATCH_DETAILS));
        initToolBar(mMatchDetails.getChallengeName());

        if (!bundle.containsKey(BundleKeys.SHOW_ANSWER_PERCENTAGE)) {
            getSupportFragmentManager().beginTransaction().replace(
                    R.id.others_answers_fl_fuzzy_holder,
                    mFuzzyPlayersFragment = FuzzyPlayerFragment.newInstance(mMatchDetails.getId())
            ).commit();
        }

        getSupportFragmentManager().beginTransaction().replace(
                R.id.others_answers_fl_answers_holder,
                OthersAnswersFragment.newInstance(bundle)
        ).commit();
    }

    private void initToolBar(String name) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.others_answers_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView tvTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        TextView matchName = (TextView) toolbar.findViewById(R.id.toolbar_match_stage);
        tvTitle.setText("Average Score");
        matchName.setText(name);

        toolbar.setNavigationIcon(R.drawable.back_icon_grey);
        toolbar.setContentInsetStartWithNavigation(2);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onBackPressed();
                    }
                }
        );
    }

    @Override
    public void onBackPressed() {
        if (null == mFuzzyPlayersFragment || !mFuzzyPlayersFragment.clearList()) {
            super.onBackPressed();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(mFuzzyPlayerClickReceiver,
                new IntentFilter(Constants.IntentActions.ACTION_FUZZY_PLAYER_CLICK));
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mFuzzyPlayerClickReceiver);
    }

    BroadcastReceiver mFuzzyPlayerClickReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            bundle.putInt(BundleKeys.MATCH_ID, mMatchDetails.getId());
            bundle.putBoolean(BundleKeys.SHOW_ANSWER_PERCENTAGE, false);

            Intent playerAnswersIntent = new Intent(OthersAnswersActivity.this, OthersAnswersActivity.class);
            playerAnswersIntent.putExtras(bundle);
            startActivity(playerAnswersIntent);
        }
    };

    @Override
    public String getScreenName() {
        return Constants.ScreenNames.OTHER_ANSWERS;
    }
}