package in.sportscafe.nostragamus.module.challengeCompleted.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeeva.android.widgets.HmImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import in.sportscafe.nostragamus.BuildConfig;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.challengeCompleted.dataProvider.CompletedChallengeDataProvider;
import in.sportscafe.nostragamus.module.challengeCompleted.dto.CompletedResponse;
import in.sportscafe.nostragamus.module.challengeCompleted.helper.CompleteChallengeFilterHelper;
import in.sportscafe.nostragamus.module.challengeCompleted.ui.viewPager.ChallengeHistoryViewPagerFragmentListener;
import in.sportscafe.nostragamus.module.challengeCompleted.ui.viewPager.CompleteChallengeViewPagerAdapter;
import in.sportscafe.nostragamus.module.challengeCompleted.ui.viewPager.ChallengeHistoryViewPagerFragment;
import in.sportscafe.nostragamus.module.common.NostraBaseFragment;
import in.sportscafe.nostragamus.module.newChallenges.dataProvider.SportsDataProvider;
import in.sportscafe.nostragamus.module.newChallenges.dto.SportsTab;
import in.sportscafe.nostragamus.module.nostraHome.ui.NostraHomeActivityListener;

/**
 * Created by deepanshi on 9/27/17.
 */

public class CompletedChallengeHistoryFragment extends NostraBaseFragment implements View.OnClickListener {

    /**
     * Need this param to decide page limit
     */
    public static final int PAGE_REQUEST_LIMIT = 20;

    public CompletedChallengeHistoryFragment() {}

    private NostraHomeActivityListener mFragmentListener;
    private List<CompletedResponse> mCompletedChallengeHistoryList;
    private Snackbar mSnackBar;
    private int mStartIndex = 0;
    private boolean mShouldLoadMore = true;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof NostraHomeActivityListener) {
            mFragmentListener = (NostraHomeActivityListener) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_completed_challenge, container, false);
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        Button emptyScreenBrowseChallengeButton = (Button) rootView.findViewById(R.id.empty_history_browse_challenge_button);
        emptyScreenBrowseChallengeButton.setOnClickListener(this);
    }

    /**
     * Supplies intent received from on new-intent of activity
     * @param intent
     */
    public void onNewIntent(Intent intent) {

    }

    public void onInternetConnected() {
        mCompletedChallengeHistoryList = null;  // Clear list
        loadData();
        if (mSnackBar != null && mSnackBar.isShown()) {
            mSnackBar.dismiss();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadData();
    }

    private void loadData() {
        if (getView() != null && !getActivity().isFinishing()) {
            showLoadingProgressBar();
            CompletedChallengeDataProvider dataProvider = new CompletedChallengeDataProvider();

            dataProvider.getCompletedChallenges(getContext().getApplicationContext(),
                    mStartIndex, PAGE_REQUEST_LIMIT,
                    new CompletedChallengeDataProvider.CompletedChallengeDataProviderListener() {
                        @Override
                        public void onData(int status, @Nullable List<CompletedResponse> completedResponseList) {
                            hideLoadingProgressBar();
                            onDataReceived(status, completedResponseList);
                        }

                        @Override
                        public void onError(int status) {
                            hideLoadingProgressBar();
                            handleError(status);
                        }

                        @Override
                        public void onNoMoreListData() {
                            mShouldLoadMore = false;
                        }
                    });
        }
    }

    private void handleError(int status) {
        if (getView() != null && getActivity() != null && !getActivity().isFinishing()) {
            switch (status) {
                case Constants.DataStatus.FROM_DATABASE_AS_NO_INTERNET:
                    mSnackBar = Snackbar.make(getView(), Constants.Alerts.NO_INTERNET_CONNECTION, Snackbar.LENGTH_INDEFINITE);
                    mSnackBar.setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onInternetConnected();
                        }
                    });
                    break;

                case Constants.DataStatus.FROM_DATABASE_AS_SERVER_FAILED:
                    mSnackBar = Snackbar.make(getView(), Constants.Alerts.COULD_NOT_FETCH_DATA_FROM_SERVER, Snackbar.LENGTH_LONG);
                    mSnackBar.setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onInternetConnected();
                        }
                    });
                    break;

                case Constants.DataStatus.NO_MORE_DATA_WHILE_LOAD_MORE:
//                    mSnackBar = Snackbar.make(getView(), Constants.Alerts.NO_MORE_HISTORY, Snackbar.LENGTH_LONG);
                    break;

                default:
                    mSnackBar = Snackbar.make(getView(), Constants.Alerts.SOMETHING_WRONG, Snackbar.LENGTH_LONG);
                    break;
            }

            mSnackBar.show();
        }
    }

    private void onDataReceived(int status, List<CompletedResponse> completedResponseList) {
        switch (status) {
            case Constants.DataStatus.FROM_SERVER_API_SUCCESS:
                showDataOnUi(completedResponseList);
                break;

            case Constants.DataStatus.FROM_DATABASE_AS_NO_INTERNET:
            case Constants.DataStatus.FROM_DATABASE_AS_SERVER_FAILED:
                mCompletedChallengeHistoryList = null;
                showDataOnUi(completedResponseList);
                handleError(status);
                break;

            default:
                handleError(status);
                break;

        }
    }

    private void showDataOnUi(List<CompletedResponse> completedResponseList) {
        if (getView() != null && getActivity() != null) {
            TabLayout completedTabLayout = (TabLayout) getView().findViewById(R.id.completed_tabs);
            ViewPager completedViewPager = (ViewPager) getView().findViewById(R.id.completed_viewPager);

            if (BuildConfig.IS_ACL_VERSION) {
                completedTabLayout.setTabMode(TabLayout.MODE_FIXED);
            }else {
                completedTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
            }

            SportsDataProvider sportsDataProvider = new SportsDataProvider();
            List<SportsTab> sportsTabList = sportsDataProvider.getSportsList();

            completedResponseList = getMergedHistoryListWithPreviouslyLoadedList(completedResponseList);

            if (completedResponseList != null && completedResponseList.size() > 0) {
                ArrayList<ChallengeHistoryViewPagerFragment> fragmentList = new ArrayList<>();
                CompleteChallengeFilterHelper filterHelper = new CompleteChallengeFilterHelper();
                ChallengeHistoryViewPagerFragment tabFragment = null;

                for (SportsTab sportsTab : sportsTabList) {
                    tabFragment = new ChallengeHistoryViewPagerFragment();
                    tabFragment.setArguments(getArguments());
                    tabFragment.setViewPagerFragmentListener(getPagerFragmentListener());

                    int sportId = sportsTab.getSportsId();
                    List<CompletedResponse> completedFilteredList = null;
                    switch (sportId) {
                        case SportsDataProvider.FILTER_ALL_SPORTS_ID:
                            completedFilteredList = completedResponseList;
                            break;

                        case SportsDataProvider.FILTER_DAILY_SPORTS_ID:
                            completedFilteredList = filterHelper.getDailyCompletedChallenges(completedResponseList);
                            break;

                        case SportsDataProvider.FILTER_MIXED_SPORTS_ID:
                            completedFilteredList = filterHelper.getCompletedMixedSportsChallengesFilteredOn(completedResponseList);
                            break;

                        default:
                            completedFilteredList = filterHelper.getCompletedChallengesFilteredOn(sportsTab.getSportsId(), completedResponseList);
                            break;
                    }

                    if (completedFilteredList != null) {
                        sportsTab.setChallengeCount(completedFilteredList.size());
                        tabFragment.onChallengeData(completedFilteredList, mShouldLoadMore);
                        tabFragment.setTabDetails(sportsTab);
                        fragmentList.add(tabFragment);
                    }
                }

            /* Sort tabs */
                sortTabs(fragmentList);

            /* create adapter */
                setPagerAdapter(completedTabLayout, completedViewPager, fragmentList);
            } else {
                showEmptyScreen(completedViewPager, completedTabLayout, sportsTabList);
            }
        }
    }

    @NonNull
    private ChallengeHistoryViewPagerFragmentListener getPagerFragmentListener() {
        return new ChallengeHistoryViewPagerFragmentListener() {
            @Override
            public void onLoadMoreClicked() {
                mStartIndex = mStartIndex + PAGE_REQUEST_LIMIT;
                loadData();
            }
        };
    }

    private List<CompletedResponse> getMergedHistoryListWithPreviouslyLoadedList(List<CompletedResponse> completedResponseList) {
        if (completedResponseList != null && completedResponseList.size() > 0) {
            if (mCompletedChallengeHistoryList == null) {
                mCompletedChallengeHistoryList = completedResponseList;
            } else {
                mCompletedChallengeHistoryList.addAll(completedResponseList);
            }
        } else {
            /* When list ends,
             * If server succeeds and list is empty/null (in server-response)
             * BUT alreadyFetched list is not empty then show msg that no more records */
            if (mCompletedChallengeHistoryList != null && !mCompletedChallengeHistoryList.isEmpty()) {
//                handleError(Constants.DataStatus.NO_MORE_DATA_WHILE_LOAD_MORE);
                mShouldLoadMore = false;
            }
        }
        return mCompletedChallengeHistoryList;
    }

    private void setPagerAdapter(TabLayout completedTabLayout, ViewPager completedViewPager,
                                 ArrayList<ChallengeHistoryViewPagerFragment> fragmentList) {
        CompleteChallengeViewPagerAdapter viewPagerAdapter = new CompleteChallengeViewPagerAdapter
                (getActivity().getSupportFragmentManager(), fragmentList);
        completedViewPager.setAdapter(viewPagerAdapter);

        completedTabLayout.setupWithViewPager(completedViewPager);

        for (int temp = 0; temp < completedTabLayout.getTabCount(); temp++) {
            TabLayout.Tab tab = completedTabLayout.getTabAt(temp);
            if (tab != null) {
                tab.setCustomView(viewPagerAdapter.getTabView(completedTabLayout.getContext(), temp));
            }
        }
    }

    private void sortTabs(ArrayList<ChallengeHistoryViewPagerFragment> fragmentList) {
        if (!BuildConfig.IS_ACL_VERSION) {
            Collections.sort(fragmentList, new Comparator<ChallengeHistoryViewPagerFragment>() {
                @Override
                public int compare(ChallengeHistoryViewPagerFragment fragment1, ChallengeHistoryViewPagerFragment fragment2) {
                    int sportId = fragment2.getTabDetails().getSportsId();
                    if (sportId == SportsDataProvider.FILTER_ALL_SPORTS_ID ||
                            sportId == SportsDataProvider.FILTER_DAILY_SPORTS_ID) {
                        return 0;
                    } else {

                        if (fragment1.getTabDetails().getChallengeCount() < fragment2.getTabDetails().getChallengeCount()) {
                            return 1;
                        } else if (fragment1.getTabDetails().getChallengeCount() == fragment2.getTabDetails().getChallengeCount()) {
                            return 0;
                        }
                    }
                    return -1;
                }
            });
        }
    }

    private void showEmptyScreen(ViewPager viewPager, TabLayout tabLayout, List<SportsTab> sportsTabList) {
        if (getView() != null) {
            viewPager.setVisibility(View.GONE);
            getView().findViewById(R.id.history_empty_screen).setVisibility(View.VISIBLE);

            for (SportsTab sportsTab : sportsTabList) {
                TabLayout.Tab tab = tabLayout.newTab();
                tab.setCustomView(getEmptyTab(tabLayout.getContext(), sportsTab));
                tabLayout.addTab(tab);
            }
        }
    }

    private View getEmptyTab(Context context, SportsTab sportsTab) {
        LinearLayout parentLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.challenge_tab, null);

        TextView tabTextView = (TextView) parentLayout.findViewById(R.id.tab_name);
        HmImageView tabImageView = (HmImageView) parentLayout.findViewById(R.id.tab_iv);
        tabTextView.setText(sportsTab.getSportsName());
        tabImageView.setBackground(ContextCompat.getDrawable(context, sportsTab.getSportIconUnSelectedDrawable()));

        return parentLayout;
    }

    private void showLoadingProgressBar() {
        if (getView() != null) {
            getView().findViewById(R.id.completedContentLayout).setVisibility(View.GONE);
            getView().findViewById(R.id.completedProgressBarLayout).setVisibility(View.VISIBLE);
        }
    }

    private void hideLoadingProgressBar() {
        if (getView() != null) {
            getView().findViewById(R.id.completedProgressBarLayout).setVisibility(View.GONE);
            getView().findViewById(R.id.completedContentLayout).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.empty_history_browse_challenge_button:
                onEmptyScreenBrowseChallengesClicked();
                break;
        }
    }

    private void onEmptyScreenBrowseChallengesClicked() {
        if (mFragmentListener != null) {
            mFragmentListener.showNewChallenges(null);
        }
    }
}
