package in.sportscafe.nostragamus.module.newChallenges.ui;


import android.content.Context;
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

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostraBaseFragment;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class NewChallengesFragment extends NostraBaseFragment implements View.OnClickListener {

    private NostraHomeActivityListener mFragmentListener;
    private TextView mTvTBarWalletMoney;
    private TextView mTvTBarNumberOfChallenges;
    private Snackbar mSnackBar;

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

        setWalletBalance();
        loadData();
    }

    private void setWalletBalance() {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            WalletApiModelImpl.newInstance(new WalletApiModelImpl.WalletApiListener() {
                @Override
                public void noInternet() {}

                @Override
                public void onApiFailed() {}

                @Override
                public void onSuccessResponse(UserWalletResponse response) {
                    int amount = (int) WalletHelper.getTotalBalance();
                    mTvTBarWalletMoney.setText(String.valueOf(amount));
                }
            }).performApiCall();
        }
    }

    private void loadData() {
        showLoadingProgressBar();
        NewChallengesDataProvider dataProvider = new NewChallengesDataProvider();
        dataProvider.getChallenges(getContext().getApplicationContext(), new NewChallengesDataProvider.ChallengesDataProviderListener() {
            @Override
            public void onData(int status, @Nullable List<NewChallengesResponse> newChallengesResponseData) {
                hideLoadingProgressBar();
                setWalletBalance();
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
                    mSnackBar = Snackbar.make(getView(), Constants.Alerts.NO_INTERNET_CONNECTION, Snackbar.LENGTH_INDEFINITE);

                    break;

                case Constants.DataStatus.FROM_DATABASE_AS_SERVER_FAILED:
                    mSnackBar = Snackbar.make(getView(), Constants.Alerts.COULD_NOT_FETCH_DATA_FROM_SERVER, Snackbar.LENGTH_INDEFINITE);
                    break;

                default:
                    mSnackBar = Snackbar.make(getView(), Constants.Alerts.SOMETHING_WRONG, Snackbar.LENGTH_LONG);
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
                // TODO: error page / no items found
            }
        }
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
                break;
        }
    }

    private void onWalletClicked() {
        Intent intent = new Intent(getContext(), WalletBalancePopupActivity.class);
        startActivity(intent);
    }
}
