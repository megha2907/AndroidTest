package in.sportscafe.nostragamus.module.newChallenges.ui.matches;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostraBaseActivity;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.utils.FragmentHelper;

/**
 * Created by deepanshi on 9/1/17.
 */

public class MatchesTimelineActivity extends NostraBaseActivity implements MatchesTimelineFragmentListener {

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.matches_timeline_toolbar);
        TextView tvToolbar = (TextView) findViewById(R.id.matches_timeline_toolbar_tv);
        tvToolbar.setText("Matches");
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

    private void loadMatchesTimelineFragment() {
        Bundle args = null;
        if (getIntent() != null) {
            args = getIntent().getExtras();
        }
        MatchesTimelineFragment matchesTimelineFragment = new MatchesTimelineFragment();
        matchesTimelineFragment.setArguments(args);
        FragmentHelper.replaceFragment(this, R.id.fragment_container, matchesTimelineFragment);
    }
}
