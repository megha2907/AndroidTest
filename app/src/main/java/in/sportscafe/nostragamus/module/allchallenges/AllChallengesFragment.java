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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeeva.android.Log;

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

/**
 * Created by Jeeva on 17/02/17.
 */

public class AllChallengesFragment extends NostragamusFragment
        implements AllChallengesApiModelImpl.OnAllChallengesApiModelListener, View.OnClickListener {

    private List<ChallengeFragment> mChallengeFragmentList = new ArrayList<>();

    private ChallengeViewPagerAdapter mViewPagerAdapter;

    private AllChallengesApiModelImpl mAllChallengesApiModel;

    private CustomViewPager mViewPager;


    public static AllChallengesFragment newInstance() {
        AllChallengesFragment fragment = new AllChallengesFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_all_challenges, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewPager = (CustomViewPager) findViewById(R.id.tab_challenge_vp);
        mAllChallengesApiModel = AllChallengesApiModelImpl.newInstance(this);
        mAllChallengesApiModel.getAllChallenges();
    }

    @Override
    public void onClick(View view) {

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
        boolean shouldShowNewTab = false;

        if (challenges.size() > 0) {
            completedAvailable = true;
            challengeFragment = ChallengeFragment.newInstance(challenges, count++, -1, Constants.ChallengeTabs.COMPLETED);
            mViewPagerAdapter.addFragment(challengeFragment, Constants.ChallengeTabs.COMPLETED);
            mChallengeFragmentList.add(challengeFragment);
        }

        List<Challenge> inPlayChallenges = mAllChallengesApiModel.getInPlayChallenges();
        if (inPlayChallenges.size() > 0) {
            challengeFragment = ChallengeFragment.newInstance(inPlayChallenges, count++, -1, Constants.ChallengeTabs.IN_PLAY);
            mViewPagerAdapter.addFragment(challengeFragment, Constants.ChallengeTabs.IN_PLAY);
            mChallengeFragmentList.add(challengeFragment);
        }

        List<Challenge> newChallenges = mAllChallengesApiModel.getNewChallenges();
        if (newChallenges.size() > 0) {

            int newChallengeIdFromNotification = -1;
            Bundle args = getArguments();
            if (args != null) {
                newChallengeIdFromNotification = args.getInt(BundleKeys.NOTIFICATION_CHALLENGE_ID, -1);
                shouldShowNewTab = args.getBoolean(BundleKeys.SHOULD_LAUNCH_NEW_TAB, false);
                Log.d("Temp", "all challenge fragment : id " + newChallengeIdFromNotification +
                        " newTab? : " + shouldShowNewTab);
            }

            challengeFragment = ChallengeFragment.newInstance(newChallenges, count++,
                    newChallengeIdFromNotification, Constants.ChallengeTabs.NEW);
            mViewPagerAdapter.addFragment(challengeFragment, Constants.ChallengeTabs.NEW);
            mChallengeFragmentList.add(challengeFragment);
        }

        mViewPager.setPagingEnabled(false);
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);

        setTabLayout(mViewPager, inPlayChallenges.size(), newChallenges.size());

        if (count > 0) {
            if (count > 1 && count == 3 || completedAvailable) {
                mViewPager.setCurrentItem(1);
            }
        }

        /* If launched from notification, 'NEW' tab should be shown */
        if (shouldShowNewTab) {
            mViewPager.setCurrentItem(2);
        } else {
            Log.d("Temp", "should not launch new tab");
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
}