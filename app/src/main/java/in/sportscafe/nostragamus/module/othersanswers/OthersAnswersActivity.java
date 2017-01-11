package in.sportscafe.nostragamus.module.othersanswers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.feed.dto.Match;
import in.sportscafe.nostragamus.module.fuzzyplayers.FuzzyPlayersFragment;

import static com.google.android.gms.analytics.internal.zzy.m;
import static com.google.android.gms.analytics.internal.zzy.t;

/**
 * Created by Jeeva on 11/01/17.
 */

public class OthersAnswersActivity extends NostragamusActivity {

    private Match mMatchDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others_answers);

        Bundle bundle = getIntent().getExtras();
        mMatchDetails = (Match) bundle.getSerializable(BundleKeys.MATCH_DETAILS);

        getSupportFragmentManager().beginTransaction().replace(
                R.id.others_answers_fl_fuzzy_holder,
                FuzzyPlayersFragment.newInstance()
        ).commit();

        getSupportFragmentManager().beginTransaction().replace(
                R.id.others_answers_fl_answers_holder,
                OthersAnswersFragment.newInstance(bundle)
        ).commit();
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
}