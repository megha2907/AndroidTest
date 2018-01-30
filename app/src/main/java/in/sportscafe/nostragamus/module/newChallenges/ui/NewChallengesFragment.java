package in.sportscafe.nostragamus.module.newChallenges.ui;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeeva.android.widgets.HmImageView;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import in.sportscafe.nostragamus.BuildConfig;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.common.NostraBaseFragment;
import in.sportscafe.nostragamus.module.customViews.CustomSnackBar;
import in.sportscafe.nostragamus.module.navigation.wallet.WalletApiModelImpl;
import in.sportscafe.nostragamus.module.navigation.wallet.WalletHelper;
import in.sportscafe.nostragamus.module.navigation.wallet.dto.UserWalletResponse;
import in.sportscafe.nostragamus.module.newChallenges.dataProvider.NewChallengesDataProvider;
import in.sportscafe.nostragamus.module.newChallenges.dataProvider.SportsDataProvider;
import in.sportscafe.nostragamus.module.newChallenges.dto.NewChallengesResponse;
import in.sportscafe.nostragamus.module.newChallenges.dto.SportsTab;
import in.sportscafe.nostragamus.module.newChallenges.helpers.NewChallengesFilterHelper;
import in.sportscafe.nostragamus.module.newChallenges.ui.viewPager.NewChallengesViewPagerAdapter;
import in.sportscafe.nostragamus.module.newChallenges.ui.viewPager.NewChallengesViewPagerFragment;
import in.sportscafe.nostragamus.module.nostraHome.ui.NostraHomeActivityListener;
import in.sportscafe.nostragamus.module.notifications.NostraNotification;
import in.sportscafe.nostragamus.module.popups.walletpopups.WalletBalancePopupActivity;
import in.sportscafe.nostragamus.utils.CodeSnippet;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewChallengesFragment extends NostraBaseFragment implements View.OnClickListener {

    private NostraHomeActivityListener mFragmentListener;
    private TextView mTvTBarWalletMoney;
    private TextView mTvTBarNumberOfChallenges;
    private CustomSnackBar mSnackBar;

    public NewChallengesFragment() {
    }

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

    /**
     * When internet gets Turn ON
     */
    public void onInternetConnected() {
        loadData();
        if (mSnackBar != null && mSnackBar.isShown()) {
            mSnackBar.dismiss();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        fetchWalletBalFromServer();
        loadData();
    }

    @Override
    public void onResume() {
        super.onResume();
        setWalletBalanceAmt();
    }

    private void fetchWalletBalFromServer() {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            WalletApiModelImpl.newInstance(new WalletApiModelImpl.WalletApiListener() {
                @Override
                public void noInternet() {}

                @Override
                public void onApiFailed() {}

                @Override
                public void onSuccessResponse(UserWalletResponse response) {
                    setWalletBalanceAmt();
                }
            }).performApiCall();
        }
    }

    private void setWalletBalanceAmt() {
        if (getView() != null && getActivity() != null && !getActivity().isFinishing()) {
            int amount = (int) WalletHelper.getTotalBalance();
            mTvTBarWalletMoney.setText(CodeSnippet.getFormattedAmount(amount));
        }
    }

    private void loadData() {
        showLoadingProgressBar();
        NewChallengesDataProvider dataProvider = new NewChallengesDataProvider();
        dataProvider.getChallenges(getContext().getApplicationContext(), new NewChallengesDataProvider.ChallengesDataProviderListener() {
            @Override
            public void onData(int status, @Nullable List<NewChallengesResponse> newChallengesResponseData) {
                hideLoadingProgressBar();
                fetchWalletBalFromServer();
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
            switch (status) {
                case Constants.DataStatus.FROM_DATABASE_AS_NO_INTERNET:
                    mSnackBar = CustomSnackBar.make(getView(), Constants.Alerts.NO_INTERNET_CONNECTION, CustomSnackBar.DURATION_INFINITE);
                    break;

                case Constants.DataStatus.FROM_DATABASE_AS_SERVER_FAILED:
                    mSnackBar = CustomSnackBar.make(getView(), Constants.Alerts.COULD_NOT_FETCH_DATA_FROM_SERVER, CustomSnackBar.DURATION_INFINITE);
                    break;

                default:
                    mSnackBar = CustomSnackBar.make(getView(), Constants.Alerts.SOMETHING_WRONG, CustomSnackBar.DURATION_LONG);
                    break;
            }

            mSnackBar.setAction("Retry", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onInternetConnected();
                }
            });
            mSnackBar.show();
        }
    }

    private void onDataReceived(int status, List<NewChallengesResponse> newChallengesResponseData) {
        switch (status) {
            case Constants.DataStatus.FROM_SERVER_API_SUCCESS:
                showDataOnUi(newChallengesResponseData);
                break;

            case Constants.DataStatus.FROM_DATABASE_AS_NO_INTERNET:
            case Constants.DataStatus.FROM_DATABASE_AS_SERVER_FAILED:
                showDataOnUi(newChallengesResponseData);
                handleError(status);
                break;

            default:
                handleError(status);
                break;

        }
    }

    private void showDataOnUi(List<NewChallengesResponse> newChallengesResponseData) {
        if (getView() != null && getActivity() != null) {

            TabLayout challengesTabLayout = (TabLayout) getView().findViewById(R.id.challenge_tabs);
            ViewPager challengesViewPager = (ViewPager) getView().findViewById(R.id.challenge_viewPager);

            if (BuildConfig.IS_ACL_VERSION) {
                challengesTabLayout.setTabMode(TabLayout.MODE_FIXED);
            }else {
                challengesTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
            }

            SportsDataProvider sportsDataProvider = new SportsDataProvider();
            List<SportsTab> sportsTabList = sportsDataProvider.getSportsList();

            if (newChallengesResponseData != null && newChallengesResponseData.size() > 0) {
                mTvTBarNumberOfChallenges.setText("(" + String.valueOf(newChallengesResponseData.size()) + ")");

                ArrayList<NewChallengesViewPagerFragment> fragmentList = new ArrayList<>();
                NewChallengesFilterHelper filterHelper = new NewChallengesFilterHelper();
                NewChallengesViewPagerFragment tabFragment = null;

                for (SportsTab sportsTab : sportsTabList) {
                    tabFragment = new NewChallengesViewPagerFragment();
                    tabFragment.setArguments(getArguments());

                    int sportId = sportsTab.getSportsId();
                    List<NewChallengesResponse> challengesFiltered = null;
                    switch (sportId) {
                        case SportsDataProvider.FILTER_ALL_SPORTS_ID:
                            challengesFiltered = newChallengesResponseData;
                            break;

                        case SportsDataProvider.FILTER_DAILY_SPORTS_ID:
                            challengesFiltered = filterHelper.getDailySports(newChallengesResponseData);
                            break;

                        case SportsDataProvider.FILTER_MIXED_SPORTS_ID:
                            challengesFiltered = filterHelper.getMixSports(newChallengesResponseData);
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

                /* Sort tabs */
                if (!BuildConfig.IS_ACL_VERSION) {
                    Collections.sort(fragmentList, new Comparator<NewChallengesViewPagerFragment>() {
                        @Override
                        public int compare(NewChallengesViewPagerFragment fragment1, NewChallengesViewPagerFragment fragment2) {
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

                /* create adapter */
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

                /* If launched from notification, the handle further flow */
                handleNotification(challengesViewPager, fragmentList);

            } else {
                showEmptyScreen(challengesViewPager, challengesTabLayout, sportsTabList);
            }
        }
    }

    private void showEmptyScreen(ViewPager viewPager, TabLayout tabLayout, List<SportsTab> sportsTabList) {
        if (getView() != null) {
            viewPager.setVisibility(View.GONE);
            getView().findViewById(R.id.empty_new_challenges_list_textView).setVisibility(View.VISIBLE);
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

    private void handleNotification(ViewPager viewPager, ArrayList<NewChallengesViewPagerFragment> viewPagerFragmentList) {
        Bundle args = getArguments();
        if (args != null && args.containsKey(Constants.Notifications.IS_LAUNCHED_FROM_NOTIFICATION)) {
            boolean isFromNotification = args.getBoolean(Constants.Notifications.IS_LAUNCHED_FROM_NOTIFICATION, false);
            NostraNotification nostraNotification = Parcels.unwrap(args.getParcelable(Constants.Notifications.NOSTRA_NOTIFICATION));

            if (isFromNotification && nostraNotification != null && nostraNotification.getData() != null && viewPagerFragmentList != null) {
                int sportId = nostraNotification.getData().getSportId();

                for (int pos = 0; pos < viewPagerFragmentList.size(); pos++) {
                    NewChallengesViewPagerFragment fragment = viewPagerFragmentList.get(pos);
                    if (fragment.getTabDetails() != null && fragment.getTabDetails().getSportsId() == sportId ) {
                        viewPager.setCurrentItem(pos);
                        break;
                    }
                }
            }
        }
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
                NostragamusAnalytics.getInstance().trackClickEvent(Constants.AnalyticsCategory.NEW_CHALLENGES, Constants.AnalyticsClickLabels.WALLET);
                break;
        }
    }

    private void onWalletClicked() {
        Intent intent = new Intent(getContext(), WalletBalancePopupActivity.class);
        startActivity(intent);
    }
}
