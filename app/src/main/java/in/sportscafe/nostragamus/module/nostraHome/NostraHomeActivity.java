package in.sportscafe.nostragamus.module.nostraHome;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.challengeCompleted.ui.CompletedChallengeFragment;
import in.sportscafe.nostragamus.module.common.NostraBaseActivity;
import in.sportscafe.nostragamus.module.inPlay.ui.InPlayFragment;
import in.sportscafe.nostragamus.module.navigation.NavigationFragment;
import in.sportscafe.nostragamus.module.newChallenges.ui.NewChallengesFragment;
import in.sportscafe.nostragamus.module.user.group.allgroups.AllGroupsFragment;
import in.sportscafe.nostragamus.utils.FragmentHelper;

public class NostraHomeActivity extends NostraBaseActivity implements View.OnClickListener {

    private static final String TAG = NostraHomeActivity.class.getSimpleName();
    public static final int DOUBLE_BACK_PRESSED_DELAY_ALLOWED = 3000;

    public interface LaunchedFrom {
        int SHOW_NEW_CHALLENGES = -111;
        int SHOW_IN_PLAY = -112;
        int SHOW_COMPLETED = -113;
        int SHOW_GROUPS = -114;
        int SHOW_NAVIGATION = -115;
    }

    private LinearLayout mNewChallengesBottomButton;
    private LinearLayout mInPlayBottomButton;
    private LinearLayout mCompletedChallengeBottomButton;
    private LinearLayout mGroupBottomButton;
    private LinearLayout mProfileBottomButton;

    private boolean mIsFirstBackPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nostra_home);

        initMembers();
        initViews();

        onNewChallengesClicked();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);

        if (intent != null) {
            int launchFrom = intent.getIntExtra(Constants.BundleKeys.SCREEN_LAUNCH_REQUEST, -1);
            switch (launchFrom) {
                case LaunchedFrom.SHOW_NEW_CHALLENGES:
                    onNewChallengesClicked();
                    break;

                case LaunchedFrom.SHOW_IN_PLAY:
                    onInPlayClicked();
                    break;

                case LaunchedFrom.SHOW_COMPLETED:
                    onCompletedClicked();
                    break;

                case LaunchedFrom.SHOW_GROUPS:
                    onGroupClicked();
                    break;

                case LaunchedFrom.SHOW_NAVIGATION:
                    onNavigationClicked();
                    break;
            }
        }
    }

    private void initMembers() {
    }

    private void initViews() {
        mNewChallengesBottomButton = (LinearLayout) findViewById(R.id.home_challenges_tab_layout);
        mInPlayBottomButton = (LinearLayout) findViewById(R.id.home_inPlay_tab_layout);
        mCompletedChallengeBottomButton = (LinearLayout) findViewById(R.id.home_completed_tab_layout);
        mGroupBottomButton = (LinearLayout) findViewById(R.id.home_group_tab_layout);
        mProfileBottomButton = (LinearLayout) findViewById(R.id.home_profile_tab_layout);

        mNewChallengesBottomButton.setOnClickListener(this);
        mInPlayBottomButton.setOnClickListener(this);
        mCompletedChallengeBottomButton.setOnClickListener(this);
        mGroupBottomButton.setOnClickListener(this);
        mProfileBottomButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_challenges_tab_layout:
                onNewChallengesClicked();
                break;

            case R.id.home_inPlay_tab_layout:
                onInPlayClicked();
                break;

            case R.id.home_completed_tab_layout:
                onCompletedClicked();
                break;

            case R.id.home_group_tab_layout:
                onGroupClicked();
                break;

            case R.id.home_profile_tab_layout:
                onNavigationClicked();
                break;
        }
    }

    protected void setNewChallengesSelected() {
        mNewChallengesBottomButton.setSelected(true);
        mInPlayBottomButton.setSelected(false);
        mCompletedChallengeBottomButton.setSelected(false);
        mGroupBottomButton.setSelected(false);
        mProfileBottomButton.setSelected(false);
    }

    protected void setInPlaySelected() {
        mInPlayBottomButton.setSelected(true);
        mNewChallengesBottomButton.setSelected(false);
        mCompletedChallengeBottomButton.setSelected(false);
        mGroupBottomButton.setSelected(false);
        mProfileBottomButton.setSelected(false);
    }

    protected void setCompletedSelected() {
        mInPlayBottomButton.setSelected(false);
        mNewChallengesBottomButton.setSelected(false);
        mCompletedChallengeBottomButton.setSelected(true);
        mGroupBottomButton.setSelected(false);
        mProfileBottomButton.setSelected(false);
    }

    protected void setGroupSelected() {
        mGroupBottomButton.setSelected(true);
        mNewChallengesBottomButton.setSelected(false);
        mCompletedChallengeBottomButton.setSelected(false);
        mInPlayBottomButton.setSelected(false);
        mProfileBottomButton.setSelected(false);
    }

    protected void setProfileSelected() {
        mProfileBottomButton.setSelected(true);
        mNewChallengesBottomButton.setSelected(false);
        mCompletedChallengeBottomButton.setSelected(false);
        mInPlayBottomButton.setSelected(false);
        mGroupBottomButton.setSelected(false);
    }

    private void onNavigationClicked() {
        setProfileSelected();
        loadNavigationFragment();
    }

    private void onGroupClicked() {
        setGroupSelected();
        loadGroupFragment();
    }

    private void onInPlayClicked() {
        setInPlaySelected();
        loadInPlayFragment();
    }

    private void onCompletedClicked() {
        setCompletedSelected();
        loadCompletedFragment();
    }

    private void onNewChallengesClicked() {
        setNewChallengesSelected();
        loadNewChallengeFragment();
    }

    private void loadNavigationFragment() {
        NavigationFragment navigationFragment = new NavigationFragment();
        if (getIntent() != null && getIntent().getExtras() != null) {
            navigationFragment.setArguments(getIntent().getExtras());
        }
        FragmentHelper.replaceFragment(this, R.id.fragment_container, navigationFragment);
    }

    private void loadInPlayFragment() {
        InPlayFragment inPlayFragment = new InPlayFragment();
        if (getIntent() != null && getIntent().getExtras() != null) {
            inPlayFragment.setArguments(getIntent().getExtras());
        }
        FragmentHelper.replaceFragment(this, R.id.fragment_container, inPlayFragment);

    }

    private void loadNewChallengeFragment() {
        NewChallengesFragment newChallengesFragment = new NewChallengesFragment();
        if (getIntent() != null && getIntent().getExtras() != null) {
            newChallengesFragment.setArguments(getIntent().getExtras());
        }
        FragmentHelper.replaceFragment(this, R.id.fragment_container, newChallengesFragment);
    }

    private void loadCompletedFragment() {
        CompletedChallengeFragment completedChallengeFragment = new CompletedChallengeFragment();
        if (getIntent() != null && getIntent().getExtras() != null) {
            completedChallengeFragment.setArguments(getIntent().getExtras());
        }
        FragmentHelper.replaceFragment(this, R.id.fragment_container, completedChallengeFragment);
    }

    private void loadGroupFragment() {
        AllGroupsFragment allGroupsFragment = new AllGroupsFragment().newInstance();
        if (getIntent() != null && getIntent().getExtras() != null) {
            allGroupsFragment.setArguments(getIntent().getExtras());
        }
        FragmentHelper.replaceFragment(this, R.id.fragment_container, allGroupsFragment);
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment != null && !(fragment instanceof NewChallengesFragment)) {
            onNewChallengesClicked();
        } else {
            handleDoubleBackPressToExitApp();
        }
    }

    /**
     * Application exit happens only when user clicks back button twice within specified time interval
     */
    private void handleDoubleBackPressToExitApp() {
        if (mIsFirstBackPressed) {
            super.onBackPressed();
        } else {

            Toast.makeText(this, getString(R.string.double_back_press_msg), Toast.LENGTH_SHORT).show();
            mIsFirstBackPressed = true;

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mIsFirstBackPressed = false;
                }
            }, DOUBLE_BACK_PRESSED_DELAY_ALLOWED);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(mNetworkStateChangedReceiver,
                new IntentFilter(Constants.IntentActions.ACTION_INTERNET_STATE_CHANGED));
    }

    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mNetworkStateChangedReceiver);
        super.onStop();
    }

    /* Local broadcast receiver when Internet connected, if app is in Foreground, will trigger action */
    BroadcastReceiver mNetworkStateChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            onInternetConnected();
        }
    };

    private void onInternetConnected() {
        if (!this.isFinishing()) {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if (fragment != null) {
                if (fragment instanceof NewChallengesFragment) {
                    ((NewChallengesFragment) fragment).onInternetConnected();
                }
                if (fragment instanceof InPlayFragment) {
                    ((InPlayFragment) fragment).onInternetConnected();
                }
            }
        }
    }

}
