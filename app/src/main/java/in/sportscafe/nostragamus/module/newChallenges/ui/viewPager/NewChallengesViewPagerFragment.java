package in.sportscafe.nostragamus.module.newChallenges.ui.viewPager;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jeeva.android.BaseFragment;
import com.jeeva.android.Log;
import com.jeeva.android.widgets.recyclerviewpager.RecyclerViewPager;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.contest.adapter.ContestAdapterListener;
import in.sportscafe.nostragamus.module.contest.dto.Contest;
import in.sportscafe.nostragamus.module.contest.dto.JoinContestData;
import in.sportscafe.nostragamus.module.contest.ui.DetailScreensLaunchRequest;
import in.sportscafe.nostragamus.module.customViews.CustomSnackBar;
import in.sportscafe.nostragamus.module.newChallenges.adapter.BannerAdapterListener;
import in.sportscafe.nostragamus.module.newChallenges.adapter.BannerRecyclerAdapter;
import in.sportscafe.nostragamus.module.newChallenges.adapter.NewChallengeAdapterListener;
import in.sportscafe.nostragamus.module.newChallenges.adapter.NewChallengesRecyclerAdapter;
import in.sportscafe.nostragamus.module.newChallenges.dataProvider.BannerDataProvider;
import in.sportscafe.nostragamus.module.newChallenges.dataProvider.SportsDataProvider;
import in.sportscafe.nostragamus.module.newChallenges.dto.BannerResponseData;
import in.sportscafe.nostragamus.module.newChallenges.dto.NewChallengesResponse;
import in.sportscafe.nostragamus.module.newChallenges.dto.SportsTab;
import in.sportscafe.nostragamus.module.newChallenges.ui.matches.NewChallengesMatchActivity;
import in.sportscafe.nostragamus.module.nostraHome.ui.NostraHomeActivityListener;
import in.sportscafe.nostragamus.module.notifications.NostraNotification;
import in.sportscafe.nostragamus.module.notifications.NotificationHelper;
import in.sportscafe.nostragamus.webservice.BannerResponse;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewChallengesViewPagerFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = NewChallengesViewPagerFragment.class.getSimpleName();

    private NostraHomeActivityListener mFragmentListener;

    private RecyclerView mRecyclerView;
    private SportsTab mSportsTab;
    private List<NewChallengesResponse> mFilteredChallenges;

    BannerRecyclerAdapter bannerRecyclerAdapter;

    public NewChallengesViewPagerFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_challenge_view_pager2, container, false);
        initRootView(rootView);
        return rootView;
    }

    private void initRootView(View rootView) {
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.challenge_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadData();
        loadBannerData();
    }

    private void loadData() {
        if (mFilteredChallenges != null && !mFilteredChallenges.isEmpty()) {
            mRecyclerView.setAdapter(new NewChallengesRecyclerAdapter(mRecyclerView.getContext(), mFilteredChallenges, getChallengeAdapterListener()));
        } else {
            showEmptyListMsg();
        }

        if (mSportsTab != null) {
            switch (mSportsTab.getSportsId()) {
                case SportsDataProvider.FILTER_ALL_SPORTS_ID:
                    scrollToChallenge();
                    break;
            }
        }
    }


    private void loadBannerData() {
        BannerDataProvider dataProvider = new BannerDataProvider();
        dataProvider.getBanners(getContext().getApplicationContext(), new BannerDataProvider.BannerDataProviderListener() {
            @Override
            public void onData(int status, @Nullable List<BannerResponseData> bannerResponseDataList) {
                onBannerDataReceived(status, bannerResponseDataList);
            }

            @Override
            public void onError(int status) {
                handleBannerError(status);
            }
        });
    }

    private void onBannerDataReceived(int status, List<BannerResponseData> bannerResponseDataList) {
        switch (status) {
            case Constants.DataStatus.FROM_SERVER_API_SUCCESS:
                setBannersOnUi(bannerResponseDataList);
                break;

            case Constants.DataStatus.FROM_DATABASE_AS_NO_INTERNET:
            case Constants.DataStatus.FROM_DATABASE_AS_SERVER_FAILED:
                setBannersOnUi(bannerResponseDataList);
                handleBannerError(status);
                break;

            default:
                handleBannerError(status);
                break;

        }
    }

    private void setBannersOnUi(List<BannerResponseData> bannerResponseDataList) {

        if (bannerResponseDataList != null && bannerResponseDataList.size() > 0 && getView() != null) {
            RecyclerView mRcvHorizontal = (RecyclerView) getView().findViewById(R.id.challenges_rcv_horizontal);
            mRcvHorizontal.setVisibility(View.VISIBLE);
            mRcvHorizontal.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            mRcvHorizontal.setNestedScrollingEnabled(false);
            bannerRecyclerAdapter = new BannerRecyclerAdapter(bannerResponseDataList, getContext(), getBannerAdapterListener());
            mRcvHorizontal.setAdapter(bannerRecyclerAdapter);
        } else {
            hideBanners();
        }

    }

    private void handleBannerError(int status) {
        hideBanners();
    }

    public void showAdSection() {
        if (getView() != null) {
            LinearLayout horizontalLayout = (LinearLayout) getView().findViewById(R.id.challenges_ads_layout);
            horizontalLayout.setVisibility(View.VISIBLE);
        }
    }

    private void hideBanners() {
        if (getView() != null) {
            LinearLayout horizontalLayout = (LinearLayout) getView().findViewById(R.id.challenges_ads_layout);
            horizontalLayout.setVisibility(View.GONE);
        }
    }


    private BannerAdapterListener getBannerAdapterListener() {
        return new BannerAdapterListener() {

            @Override
            public void handleBannerOnClick(Bundle args) {
                goToDifferentScreens(args);
            }
        };
    }

    private void goToDifferentScreens(Bundle args) {

        if (args != null) {

            /* Received BannerData when banner is clicked */
            if (args.containsKey(Constants.BundleKeys.BANNER)) {
                final BannerResponseData bannerResponseData = Parcels.unwrap(args.getParcelable(Constants.BundleKeys.BANNER));

                NotificationHelper notificationHelper = new NotificationHelper();
                NostraNotification notification = bannerResponseData.getNostraBannerInfo();

                if (notification != null && !TextUtils.isEmpty(notification.getScreenName())) {
                    if (NostragamusDataHandler.getInstance().isLoggedInUser()) {
                        String screenName = notification.getScreenName();
                        Log.d("Notification", "ScreenName : " + screenName);

                        if (screenName.equalsIgnoreCase(Constants.Notifications.SCREEN_NEW_CHALLENGE)) {
                            Bundle bundle = new Bundle();
                            if (notification != null) {
                                bundle.putBoolean(Constants.Notifications.IS_LAUNCHED_FROM_NOTIFICATION, true);
                                bundle.putParcelable(Constants.Notifications.NOSTRA_NOTIFICATION, Parcels.wrap(notification));
                            }
                            onNewChallengesClicked(bundle);

                        } else if (screenName.equalsIgnoreCase(Constants.Notifications.SCREEN_NEW_CHALLENGE_SPORT)) {
                            Bundle bundle = new Bundle();
                            if (notification != null) {
                                bundle.putBoolean(Constants.Notifications.IS_LAUNCHED_FROM_NOTIFICATION, true);
                                bundle.putParcelable(Constants.Notifications.NOSTRA_NOTIFICATION, Parcels.wrap(notification));
                            }
                            onNewChallengesClicked(bundle);

                        } else if (screenName.equalsIgnoreCase(Constants.Notifications.SCREEN_NEW_CHALLENGES_GAMES)) {
                            startActivity(notificationHelper.getNewChallengeMatchesScreenIntent(getContext(), notification));

                        } else if (screenName.equalsIgnoreCase(Constants.Notifications.SCREEN_IN_PLAY_MATCHES)) {
                            Bundle bundle = new Bundle();
                            if (notification != null) {
                                bundle.putBoolean(Constants.Notifications.IS_LAUNCHED_FROM_NOTIFICATION, true);
                                bundle.putParcelable(Constants.Notifications.NOSTRA_NOTIFICATION, Parcels.wrap(notification));
                            }
                            onInPlayClicked(bundle);

                        } else if (screenName.equalsIgnoreCase(Constants.Notifications.SCREEN_RESULTS)) {
                            startActivity(notificationHelper.getResultsScreenIntent(getContext(), notification));

                        } else if (screenName.equalsIgnoreCase(Constants.Notifications.SCREEN_CHALLENGE_HISTORY_WINNINGS)) {
                            startActivity(notificationHelper.getChallengeHistoryWinningsScreenIntent(getContext(), notification));

                        } else if (screenName.equalsIgnoreCase(Constants.Notifications.SCREEN_REFER_FRIEND)) {
                            startActivity(notificationHelper.getReferFriendScreenIntent(getContext(), notification));

                        } else if (screenName.equalsIgnoreCase(Constants.Notifications.SCREEN_REFERRAL_CREDITS)) {
                            startActivity(notificationHelper.getReferralCreditsScreenIntent(getContext(), notification));

                        } else if (screenName.equalsIgnoreCase(Constants.Notifications.SCREEN_STORE)) {
                            startActivity(notificationHelper.getStoreScreenIntent(getContext(), notification));

                        } else if (screenName.equalsIgnoreCase(Constants.Notifications.SCREEN_APP_UPDATE)) {
                            startActivity(notificationHelper.getAppUpdateScreenIntent(getContext(), notification));

                        } else if (screenName.equalsIgnoreCase(Constants.Notifications.SCREEN_WALLET_HISTORY)) {
                            startActivity(notificationHelper.getWalletHistoryScreenIntent(getContext(), notification));

                        } else if (screenName.equalsIgnoreCase(Constants.Notifications.SCREEN_WALLET_ADD_MONEY)) {
                            startActivity(notificationHelper.getAddWalletMoneyScreenIntent(getContext(), notification));

                        } else if (screenName.equalsIgnoreCase(Constants.Notifications.SCREEN_CHALLENGE_HISTORY)) {
                            Bundle bundle = new Bundle();
                            if (notification != null) {
                                bundle.putBoolean(Constants.Notifications.IS_LAUNCHED_FROM_NOTIFICATION, true);
                                bundle.putParcelable(Constants.Notifications.NOSTRA_NOTIFICATION, Parcels.wrap(notification));
                            }
                            onHistoryClicked(bundle);

                        } else if (screenName.equalsIgnoreCase(Constants.Notifications.SCREEN_CHALLENGE_HISTORY_GAMES)) {
                            startActivity(notificationHelper.getChallengeHistoryMatchesScreenIntent(getContext(), notification));

                        } else if (screenName.equalsIgnoreCase(Constants.Notifications.SCREEN_WEB_VIEW)) {
                            startActivity(notificationHelper.getWebViewScreenIntent(getContext(), notification));
                        }

                    }
                }
            } else {
                Log.d("Notification", "User Logged out, can not launch Home!");
            }
        }
    }

    private void onNewChallengesClicked(Bundle args) {
        if (mFragmentListener != null) {
            mFragmentListener.showNewChallenges(args);
        }
    }

    private void onInPlayClicked(Bundle args) {
        if (mFragmentListener != null) {
            mFragmentListener.showNewChallenges(args);
        }
    }

    private void onHistoryClicked(Bundle args) {
        if (mFragmentListener != null) {
            mFragmentListener.showNewChallenges(args);
        }
    }

    private void showEmptyListMsg() {
        if (getView() != null) {
            mRecyclerView.setVisibility(View.GONE);
            getView().findViewById(R.id.empty_list_textView).setVisibility(View.VISIBLE);
        }
    }

    private void scrollToChallenge() {

    }

    @NonNull
    private NewChallengeAdapterListener getChallengeAdapterListener() {
        return new NewChallengeAdapterListener() {
            @Override
            public void onChallengeClicked(Bundle args) {
                launchNewChallengesMatchesActivity(args);
            }

            @Override
            public void onChallengeJoinClicked(Bundle args) {

            }
        };
    }

    private void launchNewChallengesMatchesActivity(Bundle args) {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            if (getActivity() != null && !getActivity().isFinishing()) {
                Intent intent = new Intent(getActivity(), NewChallengesMatchActivity.class);
                intent.putExtras(args);
                getActivity().startActivity(intent);
            }
        } else {
            handleError(Constants.DataStatus.NO_INTERNET);
        }
    }

    private void handleError(int status) {
        if (getView() != null && getActivity() != null && !getActivity().isFinishing()) {
            switch (status) {
                case Constants.DataStatus.NO_INTERNET:
                    CustomSnackBar.make(getView(), Constants.Alerts.NO_INTERNET_CONNECTION, CustomSnackBar.DURATION_LONG).show();
                    break;

                default:
                    CustomSnackBar.make(getView(), Constants.Alerts.SOMETHING_WRONG, CustomSnackBar.DURATION_LONG).show();
                    break;
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.challenges_ads_webView:
                Log.d(TAG, "Webview Ad clicked");
                break;


        }
    }

    public void setTabDetails(SportsTab sportsTab) {
        mSportsTab = sportsTab;
    }

    public SportsTab getTabDetails() {
        return mSportsTab;
    }

    public void onChallengeData(List<NewChallengesResponse> challengesFiltered) {
        mFilteredChallenges = challengesFiltered;

        if (mRecyclerView != null && mRecyclerView.getAdapter() != null) {
            mRecyclerView.getAdapter().notifyDataSetChanged();
        }
    }


}
