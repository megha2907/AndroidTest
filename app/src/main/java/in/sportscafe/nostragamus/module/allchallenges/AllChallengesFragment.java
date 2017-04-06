package in.sportscafe.nostragamus.module.allchallenges;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

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

    private ViewPagerAdapter mViewPagerAdapter;

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
        mVSwitchSeek.animate().translationXBy(-getResources().getDimensionPixelSize(R.dimen.dp_30)).setDuration(500);
    }

    private void moveSeekToList() {
        mVSwitchSeek.animate().translationXBy(getResources().getDimensionPixelSize(R.dimen.dp_30)).setDuration(500);
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
        mViewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());

        ChallengeFragment challengeFragment;
        List<Challenge> challenges = mAllChallengesApiModel.getCompletedChallenges();
        int count = 0;
        boolean completedAvailable = false;
        if (challenges.size() > 0) {
            completedAvailable = true;
            mViewPagerAdapter.addFragment(challengeFragment = ChallengeFragment.newInstance(challenges, count++), "Completed");
            mChallengeFragmentList.add(challengeFragment);
        }

        challenges = mAllChallengesApiModel.getInPlayChallenges();
        if (challenges.size() > 0) {
            mViewPagerAdapter.addFragment(challengeFragment = ChallengeFragment.newInstance(challenges, count++), "In Play");
            mChallengeFragmentList.add(challengeFragment);
        }

        challenges = mAllChallengesApiModel.getNewChallenges();
        if (challenges.size() > 0) {
            mViewPagerAdapter.addFragment(challengeFragment = ChallengeFragment.newInstance(challenges, count++), "New");
            mChallengeFragmentList.add(challengeFragment);
        }

        CustomViewPager viewPager = (CustomViewPager) findViewById(R.id.tab_challenge_vp);
        viewPager.setPagingEnabled(false);
        viewPager.setAdapter(mViewPagerAdapter);
        viewPager.setOffscreenPageLimit(3);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_tl);
        tabLayout.setupWithViewPager(viewPager);

        if (count > 0) {
            mRlSwitch.setVisibility(View.VISIBLE);

            if (count > 1 && count == 3 || completedAvailable) {
                viewPager.setCurrentItem(1);
            }
        }
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