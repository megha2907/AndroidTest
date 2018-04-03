package in.sportscafe.nostragamus.module.recentActivity.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeeva.android.Log;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostraBaseFragment;
import in.sportscafe.nostragamus.module.customViews.CustomSnackBar;
import in.sportscafe.nostragamus.module.nostraHome.ui.NostraHomeActivityListener;
import in.sportscafe.nostragamus.module.notifications.NostraNotification;
import in.sportscafe.nostragamus.module.notifications.NotificationHelper;
import in.sportscafe.nostragamus.module.recentActivity.adapter.RecentActivityAdapterListener;
import in.sportscafe.nostragamus.module.recentActivity.adapter.RecentActivityRecyclerAdapter;
import in.sportscafe.nostragamus.module.recentActivity.dataProvider.RecentActivityDataProvider;
import in.sportscafe.nostragamus.module.recentActivity.dto.RecentActivity;

/**
 * Created by deepanshi on 3/22/18.
 */

public class RecentActivityFragment extends NostraBaseFragment {

    private static final String TAG = RecentActivityFragment.class.getSimpleName();
    private CustomSnackBar mSnackBar;
    private NostraHomeActivityListener mFragmentListener;
    RecentActivityRecyclerAdapter mRecentActivityRecyclerAdapter;
    private int mSelectedIndex = 0;

    public RecentActivityFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_recent_activity, container, false);
        return rootView;
    }

    /**
     * Supplies intent received from on new-intent of activity
     *
     * @param intent
     */
    public void onNewIntent(Intent intent) {

    }

    public void onInternetConnected() {
        loadData();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadData();

    }

    private void loadData() {
        showLoadingProgressBar();
        RecentActivityDataProvider dataProvider = new RecentActivityDataProvider();
        dataProvider.getRecentActivity(getContext().getApplicationContext(), new RecentActivityDataProvider.RecentActivityDataProviderListener() {

            @Override
            public void onData(int status, @Nullable List<RecentActivity> recentActivityList) {
                hideLoadingProgressBar();
                onDataReceived(status, recentActivityList);
            }

            @Override
            public void onError(int status) {
                hideLoadingProgressBar();
                hideActivityTypeFilter();
                handleError(status);
            }
        });
    }

    private void handleError(int status) {
        if (getView() != null && getActivity() != null && !getActivity().isFinishing()) {
            switch (status) {
                case Constants.DataStatus.FROM_DATABASE_AS_NO_INTERNET:
                    mSnackBar = CustomSnackBar.make(getView(), Constants.Alerts.NO_INTERNET_CONNECTION, CustomSnackBar.DURATION_LONG);
                    break;

                case Constants.DataStatus.FROM_DATABASE_AS_SERVER_FAILED:
                    mSnackBar = CustomSnackBar.make(getView(), Constants.Alerts.COULD_NOT_FETCH_DATA_FROM_SERVER, CustomSnackBar.DURATION_LONG);
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

    private void hideActivityTypeFilter() {
        if (getView() != null && getActivity() != null && !getActivity().isFinishing()) {
            final AppCompatSpinner spinner = (AppCompatSpinner) getView().findViewById(R.id.recent_activity_sp_activityType);
            spinner.setVisibility(View.GONE);
        }
    }

    private void onDataReceived(int status, List<RecentActivity> recentActivityList) {
        if (mFragmentListener != null) {
            mFragmentListener.updateInplayCounter();
        }

        switch (status) {
            case Constants.DataStatus.FROM_SERVER_API_SUCCESS:
                showDataOnUi(recentActivityList, true);
                break;

            case Constants.DataStatus.FROM_DATABASE_AS_NO_INTERNET:
            case Constants.DataStatus.FROM_DATABASE_ERROR:
            case Constants.DataStatus.FROM_DATABASE_AS_SERVER_FAILED:
                showDataOnUi(recentActivityList, false);
                handleError(status);
                break;

            default:
                handleError(status);
                break;

        }
    }

    private void showDataOnUi(List<RecentActivity> recentActivityList, boolean isServerResponse) {

        if (recentActivityList != null && recentActivityList.size() > 0 && getView() != null) {
            RecyclerView mRcvHorizontal = (RecyclerView) getView().findViewById(R.id.recent_activity_recyclerView);
            mRcvHorizontal.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

            mRecentActivityRecyclerAdapter = new RecentActivityRecyclerAdapter(recentActivityList, getContext(), getRecentActivityAdapterListener());
            mRcvHorizontal.setAdapter(mRecentActivityRecyclerAdapter);

            initFilterSpinner();

        } else {
            showEmptyScreen();
        }
    }

    private void initFilterSpinner() {

        if (getActivity() != null && getView() != null && getContext() != null) {

            final List categories = new ArrayList();
            categories.add(Constants.RecentActivityTypes.ALL);
            categories.add(Constants.RecentActivityTypes.ANNOUNCEMENT);
            categories.add(Constants.RecentActivityTypes.RESULT);
            categories.add(Constants.RecentActivityTypes.PROMOTION);


            ArrayAdapter dataAdapter = new ArrayAdapter(getContext(), R.layout.simple_spinner_list, categories) {

                public View getView(int position, View convertView, ViewGroup parent) {
                    return setExternalFont(super.getView(position, convertView, parent));
                }

                public View getDropDownView(int position, View convertView, ViewGroup parent) {

                    if (convertView == null) {
                        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        convertView = inflater.inflate(R.layout.simple_spinner_dropdown_item, null);

                    }
                    TextView textView = (TextView) convertView.findViewById(R.id.spinnerTextView);
                    if (categories != null && !categories.isEmpty()) {
                        textView.setText(categories.get(position).toString());
                    }
                    // Set the text color of drop down items
                    textView.setTextColor(Color.WHITE);
                    textView.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/lato/Lato-Regular.ttf"));


                    ImageView imageView = (ImageView) convertView.findViewById(R.id.spinnerImages);

                    // If this item is selected item
                    if (position == mSelectedIndex) {
                        textView.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/lato/Lato-Bold.ttf"));
                        imageView.setVisibility(View.VISIBLE);
                    }

                    return convertView;
                }
            };


            dataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);

            final AppCompatSpinner spinner = (AppCompatSpinner) getView().findViewById(R.id.recent_activity_sp_activityType);
            spinner.setVisibility(View.VISIBLE);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                    mSelectedIndex = arg2;
                    String activityType = arg0.getSelectedItem().toString();
                    filterRecentActivityListByActivityType(activityType);

                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    filterRecentActivityListByActivityType(Constants.RecentActivityTypes.ALL);
                }
            });

            spinner.setAdapter(dataAdapter);

        }

    }


    private void filterRecentActivityListByActivityType(String activityType) {

        if (mRecentActivityRecyclerAdapter != null) {
            mRecentActivityRecyclerAdapter.getFilter().filter(activityType);
        } else {
            showEmptyScreen();
        }
    }

    private View setExternalFont(View spinnerView) {
        ((TextView) spinnerView).setTypeface(Typeface.createFromAsset(
                getContext().getAssets(),
                "fonts/lato/Lato-Regular.ttf"
        ));
        return spinnerView;
    }

    private RecentActivityAdapterListener getRecentActivityAdapterListener() {
        return new RecentActivityAdapterListener() {

            @Override
            public void handleItemOnClick(Bundle args) {
                goToDifferentScreens(args);
            }

            @Override
            public void showEmptyListView(String activityType) {
                showEmptyListMsg(activityType);
            }

            @Override
            public void showFilteredList() {
                showList();
            }
        };
    }


    private void goToDifferentScreens(Bundle args) {

        if (args != null) {

            /* Received Recent Activity data when activity Type is clicked */
            if (args.containsKey(Constants.BundleKeys.RECENT_ACTIVITY)) {
                final RecentActivity recentActivity = Parcels.unwrap(args.getParcelable(Constants.BundleKeys.RECENT_ACTIVITY));

                NotificationHelper notificationHelper = new NotificationHelper();
                NostraNotification notification = recentActivity.getNostraRecentActivityInfo();

                if (notification != null && !TextUtils.isEmpty(notification.getScreenName())) {
                    if (NostragamusDataHandler.getInstance().isLoggedInUser()) {
                        String screenName = notification.getScreenName();
                        Log.d("Banner", "ScreenName : " + screenName);

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

                        } else if (screenName.equalsIgnoreCase(Constants.Notifications.SCREEN_WHATS_NEW)) {
                            startActivity(notificationHelper.getWhatsNewScreenIntent(getContext(), notification));

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

                        } else if (screenName.equalsIgnoreCase(Constants.Notifications.SCREEN_CHALLENGE_HISTORY_LEADERBOARDS)) {
                            startActivity(notificationHelper.getChallengeHistoryLeaderBoardsScreenIntent(getContext(), notification));

                        } else if (screenName.equalsIgnoreCase(Constants.Notifications.SCREEN_WEB_VIEW)) {
                            startActivity(notificationHelper.getWebViewScreenIntent(getContext(), notification));
                        } else if (screenName.equalsIgnoreCase(Constants.Notifications.SCREEN_SLIDES)) {
                            startActivity(notificationHelper.getSlidesScreenIntent(getContext(), notification));
                        } else if (screenName.equalsIgnoreCase(Constants.Notifications.SCREEN_ANNOUNCEMENT)) {
                            startActivity(notificationHelper.getAnnouncementScreenIntent(getContext(), notification));
                        } else if (screenName.equalsIgnoreCase(Constants.Notifications.SCREEN_WALLET_HOME)) {
                            startActivity(notificationHelper.getWalletHomeScreenIntent(getContext(), notification));
                        } else if (screenName.equalsIgnoreCase(Constants.Notifications.SCREEN_KYC_DETAILS)) {
                            startActivity(notificationHelper.getKYCScreenIntent(getContext(), notification));
                        } else if (screenName.equalsIgnoreCase(Constants.Notifications.NONE)) {
                            /* NO CLICK EVENT SHOULD HAPPEN */
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
            mFragmentListener.showInPlayChallenges(args);
        }
    }

    private void onHistoryClicked(Bundle args) {
        if (mFragmentListener != null) {
            mFragmentListener.showHistoryChallenges(args);
        }
    }


    private void showEmptyScreen() {
        if (getView() != null) {
            getView().findViewById(R.id.recent_activity_recyclerView).setVisibility(View.GONE);
            getView().findViewById(R.id.recent_activity_empty_screen).setVisibility(View.VISIBLE);
        }
    }

    private void showEmptyListMsg(String activityType) {
        if (getView() != null) {
            getView().findViewById(R.id.recent_activity_recyclerView).setVisibility(View.GONE);
            TextView tvEmptyList = (TextView) getView().findViewById(R.id.empty_recent_activity_list_textView);
            tvEmptyList.setVisibility(View.VISIBLE);
            tvEmptyList.setText("There are no " + activityType.toLowerCase() + "s" + " to show right now");

        }
    }

    private void showList() {
        if (getView() != null) {
            getView().findViewById(R.id.recent_activity_recyclerView).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.empty_recent_activity_list_textView).setVisibility(View.GONE);
        }
    }

    private void showLoadingProgressBar() {
        if (getView() != null) {
            getView().findViewById(R.id.recentActivityContentLayout).setVisibility(View.GONE);
            getView().findViewById(R.id.recentActivityProgressBarLayout).setVisibility(View.VISIBLE);
        }
    }

    private void hideLoadingProgressBar() {
        if (getView() != null) {
            getView().findViewById(R.id.recentActivityProgressBarLayout).setVisibility(View.GONE);
            getView().findViewById(R.id.recentActivityContentLayout).setVisibility(View.VISIBLE);
        }
    }


}
