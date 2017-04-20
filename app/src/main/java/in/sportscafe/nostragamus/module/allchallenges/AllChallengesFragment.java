package in.sportscafe.nostragamus.module.allchallenges;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.Alerts;
import in.sportscafe.nostragamus.Constants.AnalyticsActions;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.allchallenges.challenge.ChallengeFragment;
import in.sportscafe.nostragamus.module.allchallenges.dto.Challenge;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.common.CustomViewPager;
import in.sportscafe.nostragamus.module.common.NostragamusFragment;
import in.sportscafe.nostragamus.module.common.ViewPagerAdapter;

/**
 * Created by Jeeva on 17/02/17.
 */

public class AllChallengesFragment extends NostragamusFragment
        implements AllChallengesApiModelImpl.OnAllChallengesApiModelListener, View.OnClickListener {

    private List<ChallengeFragment> mChallengeFragmentList = new ArrayList<>();

    private ChallengeViewPagerAdapter mViewPagerAdapter;

    private AllChallengesApiModelImpl mAllChallengesApiModel;

    private RelativeLayout mRlSwitch;

    private View mVSwitchSeek;

    private boolean mSwipeViewSelected = true;

    private int SWIPE_VIEW = 0;

    private int LIST_VIEW = 1;

    public static AllChallengesFragment newInstance() {
        return new AllChallengesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_all_challenges, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRlSwitch = (RelativeLayout) findViewById(R.id.challenges_switch_view_rl);
        mRlSwitch.setOnClickListener(this);
        mVSwitchSeek = findViewById(R.id.challenges_v_switch_seek);

        mAllChallengesApiModel = AllChallengesApiModelImpl.newInstance(this);
        mAllChallengesApiModel.getAllChallenges();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.challenges_switch_view_rl:
                moveSeek();
                break;
        }
    }

    private void moveSeek(){

        if (mSwipeViewSelected) {
            for (ChallengeFragment fragment : mChallengeFragmentList) {
                fragment.switchToListView();
            }
            moveSeekToList();
        } else {
            for (ChallengeFragment fragment : mChallengeFragmentList) {
                fragment.switchToSwipeView(-1);
            }
            moveSeekToSwipe();
        }
        mSwipeViewSelected = !mSwipeViewSelected;

        NostragamusAnalytics.getInstance().trackTimeline(AnalyticsActions.SWITCH);

    }

    private void moveSeekToSwipe() {
        mVSwitchSeek.animate().translationXBy(-getResources().getDimensionPixelSize(R.dimen.dp_26)).setDuration(500);
    }

    private void moveSeekToList() {
        mVSwitchSeek.animate().translationXBy(getResources().getDimensionPixelSize(R.dimen.dp_26)).setDuration(500);
    }

    @Override
    public void onSuccessAllChallengesApi() {
        dismissProgressbar();
        createAdapter();
    }

    @Override
    public void onEmpty() {
        dismissProgressbar();
        showInAppMessage(Alerts.EMPTY_CHALLENGES);
    }

    @Override
    public void onFailedAllChallengesApi(String message) {
        showAlertMessage(Alerts.API_FAIL);
    }

    @Override
    public void onNoInternet() {
        showAlertMessage(Alerts.NO_NETWORK_CONNECTION);
    }

    @Override
    public void onApiCallStarted() {
        showProgressbar();
    }

    @Override
    public boolean onApiCallStopped() {
        return dismissProgressbar();
    }

    private void showAlertMessage(String message) {
        dismissProgressbar();
        showMessage(message, "RETRY", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAllChallengesApiModel.getAllChallenges();
            }
        });
    }

    private void createAdapter() {
        mViewPagerAdapter = new ChallengeViewPagerAdapter(getChildFragmentManager(), getContext());

        ChallengeFragment challengeFragment;
        List<Challenge> challenges = mAllChallengesApiModel.getCompletedChallenges();
        int count = 0;
        boolean completedAvailable = false;
        if (challenges.size() > 0) {
            completedAvailable = true;
            mViewPagerAdapter.addFragment(challengeFragment = ChallengeFragment.newInstance(challenges, count++), Constants.ChallengeTabs.COMPLETED);
            mChallengeFragmentList.add(challengeFragment);
        }

        List<Challenge> inPlayChallenges = mAllChallengesApiModel.getInPlayChallenges();
        if (inPlayChallenges.size() > 0) {
            mViewPagerAdapter.addFragment(challengeFragment = ChallengeFragment.newInstance(inPlayChallenges, count++), Constants.ChallengeTabs.IN_PLAY);
            mChallengeFragmentList.add(challengeFragment);
        }

        List<Challenge> newChallenges = mAllChallengesApiModel.getNewChallenges();
        if (newChallenges.size() > 0) {
            mViewPagerAdapter.addFragment(challengeFragment = ChallengeFragment.newInstance(newChallenges, count++), Constants.ChallengeTabs.NEW);
            mChallengeFragmentList.add(challengeFragment);
        }

        CustomViewPager viewPager = (CustomViewPager) findViewById(R.id.tab_challenge_vp);
        viewPager.setPagingEnabled(false);
        viewPager.setAdapter(mViewPagerAdapter);
        viewPager.setOffscreenPageLimit(3);

        setTabLayout(viewPager, inPlayChallenges.size(), newChallenges.size());

        if (count > 0) {
            mRlSwitch.setVisibility(View.VISIBLE);

            if (count > 1 && count == 3 || completedAvailable) {
                viewPager.setCurrentItem(1);
            }
        }
    }

    /**
     * Tab layout created dynamically (Custom tabs), any change in tabs or fragment would need change here.
     * @param viewPager
     * @param inPlayChallengeSize
     * @param newChallengeSize
     */
    private void setTabLayout(CustomViewPager viewPager, int inPlayChallengeSize, int newChallengeSize) {
        try {
            TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_tl);
            tabLayout.setupWithViewPager(viewPager);
            tabLayout.addOnTabSelectedListener(getTabListener());

            if (mViewPagerAdapter != null) {
                // Completed tab (should sync with addFragments in pagerAdapter)
                TabLayout.Tab tab = tabLayout.getTabAt(0);
                if (tab != null) {
                    tab.setCustomView(mViewPagerAdapter.getTabView(0, false, 0));
                }

                // InPlay tab
                TabLayout.Tab inPlayTab = tabLayout.getTabAt(1);
                if (inPlayTab != null) {
                    inPlayTab.setCustomView(mViewPagerAdapter.getTabView(1, true, inPlayChallengeSize));
                }

                // New tab
                TabLayout.Tab newTab = tabLayout.getTabAt(2);
                if (newTab != null) {
                    newTab.setCustomView(mViewPagerAdapter.getTabView(2, true, newChallengeSize));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @NonNull
    private TabLayout.OnTabSelectedListener getTabListener() {
        return new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab != null) {
                    View tabView = tab.getCustomView();

                    if (tabView != null) {
                        TextView tv = (TextView) tabView.findViewById(R.id.text1);
                        TextView msgTextView = (TextView) tabView.findViewById(R.id.msgCount);

                        /* 'NEW' tab will always be in red irrespective of selection */
                        if (tv.getText().toString().equalsIgnoreCase(Constants.ChallengeTabs.NEW)) {
                            tv.setTextColor(ContextCompat.getColor(getContext(), R.color.radical_red));
                            msgTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                            msgTextView.setBackgroundResource(R.drawable.challenge_tab_counter_bg);

                        } else {
                            tv.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                            msgTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                            msgTextView.setBackgroundResource(R.drawable.challenge_tab_counter_unselected);
                        }
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab != null) {
                    View tabView = tab.getCustomView();

                    if (tabView != null) {
                        TextView tv = (TextView) tabView.findViewById(R.id.text1);
                        TextView msgTextView = (TextView) tabView.findViewById(R.id.msgCount);

                        /* 'NEW' tab will always be in red irrespective of selection */
                        if (tv.getText().toString().equalsIgnoreCase(Constants.ChallengeTabs.NEW)) {
                            tv.setTextColor(ContextCompat.getColor(getContext(), R.color.radical_red));
                            msgTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                            msgTextView.setBackgroundResource(R.drawable.challenge_tab_counter_bg);
                        } else {
                            tv.setTextColor(ContextCompat.getColor(getContext(), R.color.white_60));
                            msgTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                            msgTextView.setBackgroundResource(R.drawable.challenge_tab_counter_unselected);
                        }
                    }
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mChallengeItemClickReceiver,
                new IntentFilter(Constants.IntentActions.ACTION_CHALLENGE_CLICK));
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mChallengeItemClickReceiver);
    }

    BroadcastReceiver mChallengeItemClickReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int tagId = intent.getIntExtra(BundleKeys.CHALLENGE_TAG_ID, -1);
            int clickPosition = intent.getIntExtra(BundleKeys.CLICK_POSITION, -1);
            boolean swipeView = intent.getBooleanExtra(BundleKeys.CHALLENGE_SWITCH_POS,false);

            if (swipeView) {
                for (int i = 0; i < mChallengeFragmentList.size(); i++) {
                    mChallengeFragmentList.get(i).switchToSwipeView(i == tagId ? clickPosition : -1);
                }

                moveSeekToSwipe();
                mSwipeViewSelected = true;
            }else {
                moveSeek();
            }
        }
    };
}