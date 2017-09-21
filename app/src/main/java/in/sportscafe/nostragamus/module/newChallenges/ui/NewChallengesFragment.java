package in.sportscafe.nostragamus.module.newChallenges.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeeva.android.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.navigation.wallet.WalletHelper;
import in.sportscafe.nostragamus.module.newChallenges.dataProvider.NewChallengesDataProvider;
import in.sportscafe.nostragamus.module.newChallenges.dataProvider.SportsDataProvider;
import in.sportscafe.nostragamus.module.newChallenges.dto.NewChallengesResponse;
import in.sportscafe.nostragamus.module.newChallenges.dto.SportsTab;
import in.sportscafe.nostragamus.module.newChallenges.helpers.NewChallengesFilterHelper;
import in.sportscafe.nostragamus.module.newChallenges.ui.viewPager.NewChallengesViewPagerAdapter;
import in.sportscafe.nostragamus.module.newChallenges.ui.viewPager.NewChallengesViewPagerFragment;
import in.sportscafe.nostragamus.module.popups.walletpopups.WalletBalancePopupActivity;
import in.sportscafe.nostragamus.module.user.myprofile.edit.EditProfileActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewChallengesFragment extends BaseFragment implements View.OnClickListener {

    private TextView mTvTBarWalletMoney;
    private TextView mTvTBarNumberOfChallenges;

    public NewChallengesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_challenges2, container, false);
        initRootView(rootView);
        return rootView;
    }

    private void initRootView(View rootView) {
        mTvTBarWalletMoney = (TextView) rootView.findViewById(R.id.toolbar_wallet_money);
        mTvTBarNumberOfChallenges = (TextView) rootView.findViewById(R.id.toolbar_heading_two);
        rootView.findViewById(R.id.toolbar_wallet_rl).setOnClickListener(this);
    }

    /**
     * Delivers intent received while newIntent() of activity re-tasking.
     *
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
        setWalletBalance();
        loadData();
    }

    private void setWalletBalance() {
        int amount = (int) WalletHelper.getTotalBalance();
        mTvTBarWalletMoney.setText(String.valueOf(amount));
    }

    private void loadData() {
        showLoadingProgressBar();
        NewChallengesDataProvider dataProvider = new NewChallengesDataProvider();
        dataProvider.getChallenges(getContext().getApplicationContext(), new NewChallengesDataProvider.ChallengesDataProviderListener() {
            @Override
            public void onData(int status, @Nullable List<NewChallengesResponse> newChallengesResponseData) {
                hideLoadingProgressBar();
                onDataReceived(status, newChallengesResponseData);
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

    private void onDataReceived(int status, List<NewChallengesResponse> newChallengesResponseData) {
        switch (status) {
            case Constants.DataStatus.FROM_SERVER_API_SUCCESS:
            case Constants.DataStatus.FROM_DATABASE_AS_NO_INTERNET:
            case Constants.DataStatus.FROM_DATABASE_AS_SERVER_FAILED:
                showDataOnUi(newChallengesResponseData);
                break;

            default:
                handleError(status);
                break;

        }
    }

    private void showDataOnUi(List<NewChallengesResponse> newChallengesResponseData) {
        if (getView() != null && getActivity() != null) {
            if (newChallengesResponseData != null && newChallengesResponseData.size() > 0) {

                TabLayout challengesTabLayout = (TabLayout) getView().findViewById(R.id.challenge_tabs);
                ViewPager challengesViewPager = (ViewPager) getView().findViewById(R.id.challenge_viewPager);
                mTvTBarNumberOfChallenges.setText("(" + String.valueOf(newChallengesResponseData.size()) + ")");

                SportsDataProvider sportsDataProvider = new SportsDataProvider();
                List<SportsTab> sportsTabList = sportsDataProvider.getSportsList();

                ArrayList<NewChallengesViewPagerFragment> fragmentList = new ArrayList<>();
                NewChallengesFilterHelper filterHelper = new NewChallengesFilterHelper();
                NewChallengesViewPagerFragment tabFragment = null;

                for (SportsTab sportsTab : sportsTabList) {
                    tabFragment = new NewChallengesViewPagerFragment();

                    int sportId = sportsTab.getSportsId();
                    List<NewChallengesResponse> challengesFiltered = null;
                    switch (sportId) {
                        case NewChallengesFilterHelper.FILTER_ALL_SPORTS_ID:
                            challengesFiltered = newChallengesResponseData;
                            break;

                        case NewChallengesFilterHelper.FILTER_DAILY_SPORTS_ID:
                            challengesFiltered = getDailySports(newChallengesResponseData);
                            break;

                        case NewChallengesFilterHelper.FILTER_MIXED_SPORTS_ID:
                            challengesFiltered = getMixSports(newChallengesResponseData);
                            break;

                        default:
                            challengesFiltered = filterHelper.getNewChallengesFilteredOn(sportsTab.getSportsId(), newChallengesResponseData);
                            break;
                    }

                    if (challengesFiltered != null) {
                        sportsTab.setChallengeCount(challengesFiltered.size());
                        tabFragment.onChallengeData(challengesFiltered);
                        tabFragment.setTabDetails(sportsTab);
                        fragmentList.add(tabFragment);
                    }
                }

                NewChallengesViewPagerAdapter viewPagerAdapter = new NewChallengesViewPagerAdapter
                        (getActivity().getSupportFragmentManager(), fragmentList);
                challengesViewPager.setAdapter(viewPagerAdapter);

                challengesTabLayout.setupWithViewPager(challengesViewPager);

                for (int temp = 0; temp < challengesTabLayout.getTabCount(); temp++) {
                    TabLayout.Tab tab = challengesTabLayout.getTabAt(temp);
                    if (tab != null) {
                        tab.setCustomView(viewPagerAdapter.getTabView(challengesTabLayout.getContext(), temp));
                    }
                }

            } else {
                // TODO: error page / no items found
            }
        }
    }

    private List<NewChallengesResponse> getDailySports(List<NewChallengesResponse> newChallengesResponseData) {
        List<NewChallengesResponse> dailyChallenge = new ArrayList<>();

        if (newChallengesResponseData != null && newChallengesResponseData.size() > 0) {
            for (NewChallengesResponse newChallenge : newChallengesResponseData) {

            }
        }

        return dailyChallenge;
    }

    private List<NewChallengesResponse> getMixSports(List<NewChallengesResponse> newChallengesResponseData) {
        List<NewChallengesResponse> mixChallenge = new ArrayList<>();

        if (newChallengesResponseData != null && newChallengesResponseData.size() > 0) {
            for (NewChallengesResponse newChallenge : newChallengesResponseData) {

            }
        }

        return mixChallenge;
    }

    private void showLoadingProgressBar() {
        if (getView() != null) {
            getView().findViewById(R.id.newChallengeContentLayout).setVisibility(View.GONE);
            getView().findViewById(R.id.newChallengeProgressBarLayout).setVisibility(View.VISIBLE);
        }
    }

    private void hideLoadingProgressBar() {
        if (getView() != null) {
            getView().findViewById(R.id.newChallengeProgressBarLayout).setVisibility(View.GONE);
            getView().findViewById(R.id.newChallengeContentLayout).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_wallet_rl:
                onWalletClicked();
                break;
        }
    }

    private void onWalletClicked() {
        Intent intent = new Intent(getContext(), WalletBalancePopupActivity.class);
        startActivity(intent);
    }
}
