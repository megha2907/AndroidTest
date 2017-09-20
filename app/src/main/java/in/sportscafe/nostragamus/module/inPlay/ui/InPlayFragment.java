package in.sportscafe.nostragamus.module.inPlay.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeeva.android.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostraBaseFragment;
import in.sportscafe.nostragamus.module.inPlay.adapter.InPlayRecyclerAdapter;
import in.sportscafe.nostragamus.module.inPlay.dataProvider.InPlayDataProvider;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayResponse;
import in.sportscafe.nostragamus.module.inPlay.helper.InPlayFilterHelper;
import in.sportscafe.nostragamus.module.inPlay.ui.viewPager.InPlayViewPagerAdapter;
import in.sportscafe.nostragamus.module.inPlay.ui.viewPager.InPlayViewPagerFragment;
import in.sportscafe.nostragamus.module.newChallenges.dataProvider.SportsDataProvider;
import in.sportscafe.nostragamus.module.newChallenges.dto.SportsTab;

/**
 * A simple {@link Fragment} subclass.
 */
public class InPlayFragment extends NostraBaseFragment {

    public InPlayFragment() {}

    private RecyclerView mRcvInPlay;

    private InPlayRecyclerAdapter inPlayRecyclerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_in_play, container, false);
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
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            loadData();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadData();

    }

    private void loadData() {
        showLoadingProgressBar();
        InPlayDataProvider dataProvider = new InPlayDataProvider();
        dataProvider.getInPlayChallenges(getContext().getApplicationContext(), new InPlayDataProvider.InPlayDataProviderListener() {
            @Override
            public void onData(int status, @Nullable List<InPlayResponse> inPlayResponseData) {
                hideLoadingProgressBar();
                onDataReceived(status, inPlayResponseData);
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
            Snackbar.make(getView(), Constants.Alerts.SOMETHING_WRONG, Snackbar.LENGTH_SHORT);
        }
    }

    private void onDataReceived(int status, List<InPlayResponse> inPlayResponseData) {
        switch (status) {
            case Constants.DataStatus.FROM_SERVER_API_SUCCESS:
            case Constants.DataStatus.FROM_DATABASE_AS_NO_INTERNET:
            case Constants.DataStatus.FROM_DATABASE_AS_SERVER_FAILED:
                showDataOnUi(inPlayResponseData);
                break;

            default:
                handleError(status);
                break;

        }
    }

    private void showDataOnUi(List<InPlayResponse> inPlayResponseList) {
        if (getView() != null && getActivity() != null && inPlayResponseList != null && inPlayResponseList.size() > 0) {
            TabLayout inPlayTabLayout = (TabLayout) getView().findViewById(R.id.inplay_tabs);
            ViewPager inPlayViewPager = (ViewPager) getView().findViewById(R.id.inplay_viewPager);

            SportsDataProvider sportsDataProvider = new SportsDataProvider();
            List<SportsTab> sportsTabList = sportsDataProvider.getInPlaySportsList();

            ArrayList<InPlayViewPagerFragment> fragmentList = new ArrayList<>();
            InPlayFilterHelper filterHelper = new InPlayFilterHelper();
            InPlayViewPagerFragment tabFragment = null;

            for (SportsTab sportsTab : sportsTabList) {
                tabFragment = new InPlayViewPagerFragment();

                int sportId = sportsTab.getSportsId();
                List<InPlayResponse> inPlayFilteredList = null;
                switch (sportId) {
                    case InPlayFilterHelper.FILTER_ALL_SPORTS_ID:
                        inPlayFilteredList = inPlayResponseList;
                        break;

                    case InPlayFilterHelper.FILTER_DAILY_SPORTS_ID:
                        inPlayFilteredList = filterHelper.getDailyInPlayChallenges(inPlayResponseList);
                        break;

                    case InPlayFilterHelper.FILTER_MIX_SPORTS_ID:
                        inPlayFilteredList = filterHelper.getInPlayMixedSportsChallengesFilteredOn(inPlayResponseList);
                        break;

                    default:
                        inPlayFilteredList = filterHelper.getInPlayChallengesFilteredOn(sportsTab.getSportsId(), inPlayResponseList);
                        break;
                }

                if (inPlayFilteredList != null) {
                    sportsTab.setChallengeCount(inPlayFilteredList.size());
                    tabFragment.onChallengeData(inPlayFilteredList);
                    tabFragment.setTabDetails(sportsTab);
                    fragmentList.add(tabFragment);
                }
            }

            InPlayViewPagerAdapter viewPagerAdapter = new InPlayViewPagerAdapter
                    (getActivity().getSupportFragmentManager(), fragmentList);
            inPlayViewPager.setAdapter(viewPagerAdapter);

            inPlayTabLayout.setupWithViewPager(inPlayViewPager);

            for (int temp = 0; temp < inPlayTabLayout.getTabCount(); temp++) {
                TabLayout.Tab tab = inPlayTabLayout.getTabAt(temp);
                if (tab != null) {
                    tab.setCustomView(viewPagerAdapter.getTabView(inPlayTabLayout.getContext(), temp));
                }
            }

        } else {
            // TODO: error page / no items found
        }
    }

    private void showLoadingProgressBar() {
        if (getView() != null) {
            getView().findViewById(R.id.inplayContentLayout).setVisibility(View.GONE);
            getView().findViewById(R.id.inPlayProgressBarLayout).setVisibility(View.VISIBLE);
        }
    }

    private void hideLoadingProgressBar() {
        if (getView() != null) {
            getView().findViewById(R.id.inPlayProgressBarLayout).setVisibility(View.GONE);
            getView().findViewById(R.id.inplayContentLayout).setVisibility(View.VISIBLE);
        }
    }

}
