package in.sportscafe.nostragamus.module.challengeCompleted.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.challengeCompleted.dataProvider.CompletedChallengeDataProvider;
import in.sportscafe.nostragamus.module.challengeCompleted.dto.CompletedResponse;
import in.sportscafe.nostragamus.module.challengeCompleted.helper.CompleteChallengeFilterHelper;
import in.sportscafe.nostragamus.module.challengeCompleted.ui.viewPager.CompleteChallengeViewPagerAdapter;
import in.sportscafe.nostragamus.module.challengeCompleted.ui.viewPager.CompleteChallengeViewPagerFragment;
import in.sportscafe.nostragamus.module.common.NostraBaseFragment;
import in.sportscafe.nostragamus.module.inPlay.dataProvider.InPlayDataProvider;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayResponse;
import in.sportscafe.nostragamus.module.inPlay.helper.InPlayFilterHelper;
import in.sportscafe.nostragamus.module.inPlay.ui.viewPager.InPlayViewPagerAdapter;
import in.sportscafe.nostragamus.module.inPlay.ui.viewPager.InPlayViewPagerFragment;
import in.sportscafe.nostragamus.module.newChallenges.dataProvider.SportsDataProvider;
import in.sportscafe.nostragamus.module.newChallenges.dto.SportsTab;

/**
 * Created by deepanshi on 9/27/17.
 */

public class CompletedChallengeFragment extends NostraBaseFragment {

    public CompletedChallengeFragment() {}

    private Snackbar mSnackBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_completed_challenge, container, false);
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {

    }

    /**
     * Supplies intent received from on new-intent of activity
     * @param intent
     */
    public void onNewIntent(Intent intent) {

    }

    public void onInternetConnected() {
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
        showLoadingProgressBar();
        CompletedChallengeDataProvider dataProvider = new CompletedChallengeDataProvider();
        dataProvider.getCompletedChallenges(getContext().getApplicationContext(),
                0, 10, // TODO : pagination
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
        });
    }

    private void handleError(int status) {
        if (getView() != null && getActivity() != null && !getActivity().isFinishing()) {
            switch (status) {
                case Constants.DataStatus.FROM_DATABASE_AS_NO_INTERNET:
                    mSnackBar = Snackbar.make(getView(), Constants.Alerts.NO_NETWORK_CONNECTION, Snackbar.LENGTH_INDEFINITE);
                    mSnackBar.show();
                    break;

                case Constants.DataStatus.FROM_DATABASE_AS_SERVER_FAILED:
                    mSnackBar = Snackbar.make(getView(), "Server Error!", Snackbar.LENGTH_LONG);
                    mSnackBar.show();
                    break;

                default:
                    Snackbar.make(getView(), Constants.Alerts.SOMETHING_WRONG, Snackbar.LENGTH_LONG);
                    mSnackBar.show();
                    break;
            }
        }
    }

    private void onDataReceived(int status, List<CompletedResponse> completedResponseList) {
        switch (status) {
            case Constants.DataStatus.FROM_SERVER_API_SUCCESS:
                showDataOnUi(completedResponseList);
                break;

            case Constants.DataStatus.FROM_DATABASE_AS_NO_INTERNET:
            case Constants.DataStatus.FROM_DATABASE_AS_SERVER_FAILED:
                showDataOnUi(completedResponseList);
                handleError(status);
                break;

            default:
                handleError(status);
                break;

        }
    }

    private void showDataOnUi(List<CompletedResponse> completedResponseList) {
        if (getView() != null && getActivity() != null && completedResponseList != null && completedResponseList.size() > 0) {
            TabLayout completedTabLayout = (TabLayout) getView().findViewById(R.id.completed_tabs);
            ViewPager completedViewPager = (ViewPager) getView().findViewById(R.id.completed_viewPager);

            SportsDataProvider sportsDataProvider = new SportsDataProvider();
            List<SportsTab> sportsTabList = sportsDataProvider.getInPlaySportsList();

            ArrayList<CompleteChallengeViewPagerFragment> fragmentList = new ArrayList<>();
            CompleteChallengeFilterHelper filterHelper = new CompleteChallengeFilterHelper();
            CompleteChallengeViewPagerFragment tabFragment = null;

            for (SportsTab sportsTab : sportsTabList) {
                tabFragment = new CompleteChallengeViewPagerFragment();
                tabFragment.setArguments(getArguments());

                int sportId = sportsTab.getSportsId();
                List<CompletedResponse> completedFilteredList = null;
                switch (sportId) {
                    case InPlayFilterHelper.FILTER_ALL_SPORTS_ID:
                        completedFilteredList = completedResponseList;
                        break;

                    case InPlayFilterHelper.FILTER_DAILY_SPORTS_ID:
                        completedFilteredList = filterHelper.getDailyCompletedChallenges(completedResponseList);
                        break;

                    case InPlayFilterHelper.FILTER_MIX_SPORTS_ID:
                        completedFilteredList = filterHelper.getCompletedMixedSportsChallengesFilteredOn(completedResponseList);
                        break;

                    default:
                        completedFilteredList = filterHelper.getCompletedChallengesFilteredOn(sportsTab.getSportsId(), completedResponseList);
                        break;
                }

                if (completedFilteredList != null) {
                    sportsTab.setChallengeCount(completedFilteredList.size());
                    tabFragment.onChallengeData(completedFilteredList);
                    tabFragment.setTabDetails(sportsTab);
                    fragmentList.add(tabFragment);
                }
            }

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

        } else {
            // TODO: error page / no items found
        }
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

}
