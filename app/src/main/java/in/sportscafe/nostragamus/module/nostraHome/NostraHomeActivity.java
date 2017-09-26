package in.sportscafe.nostragamus.module.nostraHome;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostraBaseActivity;
import in.sportscafe.nostragamus.module.contest.ui.ContestFragment;
import in.sportscafe.nostragamus.module.inPlay.ui.InPlayFragment;
import in.sportscafe.nostragamus.module.inPlay.ui.InPlayMatchTimelineViewPagerFragment;
import in.sportscafe.nostragamus.module.navigation.NavigationFragment;
import in.sportscafe.nostragamus.module.newChallenges.ui.NewChallengesFragment;
import in.sportscafe.nostragamus.utils.FragmentHelper;

public class NostraHomeActivity extends NostraBaseActivity implements View.OnClickListener {

    private static final String TAG = NostraHomeActivity.class.getSimpleName();
    public static final int DOUBLE_BACK_PRESSED_DELAY_ALLOWED = 3000;

    /**
     * Keep a single instance of all the fragments ready always
     * Do not create them again & again, so maintain reference
     */
    private NewChallengesFragment mNewChallengeFragment;
    private InPlayFragment mInPlayFragment;
    private NavigationFragment mNavigationFragment;

    private LinearLayout mNewChallengesBottomButton;
    private LinearLayout mJoinedBottomButton;
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

    }

    private void initMembers() {
        mNewChallengeFragment = new NewChallengesFragment();
        mInPlayFragment = new InPlayFragment();
        mNavigationFragment = new NavigationFragment();
    }

    private void initViews() {
        mNewChallengesBottomButton = (LinearLayout) findViewById(R.id.home_challenges_tab_layout);
        mJoinedBottomButton = (LinearLayout) findViewById(R.id.home_join_tab_layout);
        mGroupBottomButton = (LinearLayout) findViewById(R.id.home_group_tab_layout);
        mProfileBottomButton = (LinearLayout) findViewById(R.id.home_profile_tab_layout);

        mNewChallengesBottomButton.setOnClickListener(this);
        mJoinedBottomButton.setOnClickListener(this);
        mGroupBottomButton.setOnClickListener(this);
        mProfileBottomButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_challenges_tab_layout:
                onNewChallengesClicked();
                break;

            case R.id.home_join_tab_layout:
                onJoinClicked();
                break;

            case R.id.home_group_tab_layout:
                onGroupClicked();
                break;

            case R.id.home_profile_tab_layout:
                onProfileClicked();
                break;
        }
    }

    protected void setNewChallengesSelected() {
        mNewChallengesBottomButton.setSelected(true);
        mJoinedBottomButton.setSelected(false);
        mGroupBottomButton.setSelected(false);
        mProfileBottomButton.setSelected(false);
    }

    protected void setInPlaySelected() {
        mJoinedBottomButton.setSelected(true);
        mNewChallengesBottomButton.setSelected(false);
        mGroupBottomButton.setSelected(false);
        mProfileBottomButton.setSelected(false);
    }

    protected void setGroupSelected() {
        mGroupBottomButton.setSelected(true);
        mNewChallengesBottomButton.setSelected(false);
        mJoinedBottomButton.setSelected(false);
        mProfileBottomButton.setSelected(false);
    }

    protected void setProfileSelected() {
        mProfileBottomButton.setSelected(true);
        mNewChallengesBottomButton.setSelected(false);
        mJoinedBottomButton.setSelected(false);
        mGroupBottomButton.setSelected(false);
    }

    private void onProfileClicked() {
        setProfileSelected();
        loadNavigationFragment();
    }

    private void onGroupClicked() {
        setGroupSelected();
       /* Intent intent = new Intent(this.getApplicationContext(), GroupActivity.class);
        startActivity(intent);*/
    }

    private void onJoinClicked() {
        setInPlaySelected();
        loadInPlayFragment();
    }

    private void onNewChallengesClicked() {
        setNewChallengesSelected();
        loadNewChallengeFragment();
    }

    private void loadNavigationFragment() {
        if (mNavigationFragment == null) {
            mNavigationFragment = new NavigationFragment();
        }

        if (getIntent() != null && getIntent().getExtras() != null) {
            mNavigationFragment.setArguments(getIntent().getExtras());
        }

        FragmentHelper.replaceFragment(this, R.id.fragment_container, mNavigationFragment);
    }

    private void loadInPlayFragment() {
        mInPlayFragment = new InPlayFragment();
        if (getIntent() != null && getIntent().getExtras() != null) {
            mInPlayFragment.setArguments(getIntent().getExtras());
        }

        FragmentHelper.replaceFragment(this, R.id.fragment_container, mInPlayFragment);

        //    FragmentHelper.replaceFragment(this, R.id.fragment_container, new ContestFragment());


//        Bundle bundle = new Bundle();
//        bundle.putInt(Constants.BundleKeys.CONTEST_ID, 1);
//        RewardsFragment rewardsFragment = new RewardsFragment();
//        rewardsFragment.setArguments(bundle);
//        FragmentHelper.replaceFragment(this, R.id.fragment_container, rewardsFragment);

        //FragmentHelper.replaceFragment(this, R.id.fragment_container, new ContestEntriesViewPagerFragment());
    }

    private void loadNewChallengeFragment() {
//        if (mNewChallengeFragment == null) {
        mNewChallengeFragment = new NewChallengesFragment();
//        }

        if (getIntent() != null && getIntent().getExtras() != null) {
            mNewChallengeFragment.setArguments(getIntent().getExtras());
        }

        FragmentHelper.replaceFragment(this, R.id.fragment_container, mNewChallengeFragment);
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
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setTitle("Internet Connected");
            dialogBuilder.setMessage("Reload data");
            dialogBuilder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
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
            });
            dialogBuilder.show();
        }
    }

}
