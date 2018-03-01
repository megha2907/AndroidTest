package in.sportscafe.nostragamus.module.navigation;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeeva.android.BaseFragment;
import com.jeeva.android.Log;
import com.jeeva.android.widgets.HmImageView;

import java.util.Set;

import in.sportscafe.nostragamus.BuildConfig;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.common.NostraBaseFragment;
import in.sportscafe.nostragamus.module.navigation.appupdate.AppUpdateActivity;
import in.sportscafe.nostragamus.module.navigation.help.HelpActivity;
import in.sportscafe.nostragamus.module.navigation.powerupbank.PowerUpBankActivity;
import in.sportscafe.nostragamus.module.navigation.referfriends.ReferFriendActivity;
import in.sportscafe.nostragamus.module.navigation.settings.SettingsActivity;
import in.sportscafe.nostragamus.module.navigation.submitquestion.tourlist.TourListActivity;
import in.sportscafe.nostragamus.module.navigation.wallet.WalletApiModelImpl;
import in.sportscafe.nostragamus.module.navigation.wallet.WalletHelper;
import in.sportscafe.nostragamus.module.navigation.wallet.WalletHomeActivity;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.addByCashFree.CashFreeSampleActivity;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.selectPaymentMode.SelectPaymentModeActivity;
import in.sportscafe.nostragamus.module.navigation.wallet.dto.UserWalletResponse;
import in.sportscafe.nostragamus.module.popups.challengepopups.ContestDetailsPopupActivity;
import in.sportscafe.nostragamus.module.settings.app.dto.AppSettingsResponse;
import in.sportscafe.nostragamus.module.settings.app.dto.AppUpdateInfo;
import in.sportscafe.nostragamus.module.store.StoreActivity;
import in.sportscafe.nostragamus.module.user.login.UserInfoModelImpl;
import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;
import in.sportscafe.nostragamus.module.user.myprofile.UserProfileActivity;

public class NavigationFragment extends NostraBaseFragment implements View.OnClickListener {

    private static final String TAG = NavigationFragment.class.getSimpleName();

    public NavigationFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation, container, false);
        initRootView(view);
        return view;
    }

    private void initRootView(View view) {
        setListener(view);
    }

    private void setListener(View view) {
        view.findViewById(R.id.navigation_profile_layout).setOnClickListener(this);
        view.findViewById(R.id.navigation_wallet_layout).setOnClickListener(this);
        view.findViewById(R.id.navigation_powerup_bank_layout).setOnClickListener(this);
        view.findViewById(R.id.navigation_store_layout).setOnClickListener(this);
        view.findViewById(R.id.navigation_whats_new_layout).setOnClickListener(this);
//        view.findViewById(R.id.navigation_submit_question_layout).setOnClickListener(this);
        view.findViewById(R.id.navigation_help_layout).setOnClickListener(this);
        view.findViewById(R.id.navigation_settings_layout).setOnClickListener(this);
        view.findViewById(R.id.navigation_app_update_layout).setOnClickListener(this);
        view.findViewById(R.id.navigation_refer_layout).setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();
    }

    private void initialize() {
        showOrUpdateWalletAmount();
        fetchServerDetails();
        showOrHideContentBasedOnAppType();
        showVersionOrAppUpdateMsg();
        initProfileDetails();
    }

    private void fetchServerDetails() {
        /* Make silent API calls
         * 1. Wallet details api (onResume as wallet values need to be updated)
         * 2. User Info api */
        fetchUserInfo();
    }

    private void showOrHideContentBasedOnAppType() {
        if (getView() != null) {
        /* Hide wallet section for free (ps - play store) app */
            if (!BuildConfig.IS_PAID_VERSION) {
                if (getView() != null) {
                    getView().findViewById(R.id.navigation_wallet_layout).setVisibility(View.GONE);
                    getView().findViewById(R.id.navigation_refer_layout).setVisibility(View.GONE);
                    getView().findViewById(R.id.navigation_store_layout).setVisibility(View.GONE);
                    getView().findViewById(R.id.navigation_refer_separator).setVisibility(View.GONE);
                    getView().findViewById(R.id.navigation_powerup_bank_separator).setVisibility(View.GONE);
                    getView().findViewById(R.id.navigation_store_separator).setVisibility(View.GONE);

                /*change Earn More Money text to Earn More Powerups in playstore app */
                    TextView navEarnMore = (TextView) getView().findViewById(R.id.navigation_earn_more_tv_two);
                    navEarnMore.setText(" powerups!");
                    ImageView navEarnMoreIcon = (ImageView) getView().findViewById(R.id.navigation_earn_more_iv);
                    navEarnMoreIcon.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.navigation_powerup_icon));

                }
            } else {
                ImageView navEarnMoreIcon = (ImageView) getView().findViewById(R.id.navigation_earn_more_iv);
                navEarnMoreIcon.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.navigation_earn_more_icon));
            }
        }
    }

    private void showVersionOrAppUpdateMsg() {
        View view = getView();
        if (view != null) {
            if (isNewVersionAvailable()) {
                view.findViewById(R.id.navigation_app_update_layout).setOnClickListener(this);
                view.findViewById(R.id.navigation_app_update_available_imageView).setVisibility(View.VISIBLE);
                view.findViewById(R.id.navigation_whats_new_layout).setVisibility(View.GONE);
                view.findViewById(R.id.navigation_whats_new_separator).setVisibility(View.GONE);

                TextView versionNameTextView = (TextView) view.findViewById(R.id.navigation_version_textView);
                versionNameTextView.setText("New Version Available");

                TextView updateTextView = (TextView) view.findViewById(R.id.navigation_update_str_textView);
                updateTextView.setTextColor(ContextCompat.getColor(updateTextView.getContext(), R.color.white));
                updateTextView.setText("Update the App");

                ImageView updateApp = (ImageView) view.findViewById(R.id.navigation_app_update_app_iv);
                updateApp.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.navigation_update_app_icn));

            } else {
                view.findViewById(R.id.navigation_app_update_layout).setOnClickListener(null);
                view.findViewById(R.id.navigation_app_update_available_imageView).setVisibility(View.GONE);

                ImageView updateApp = (ImageView) view.findViewById(R.id.navigation_app_update_app_iv);
                TextView versionNameTextView = (TextView) view.findViewById(R.id.navigation_version_textView);
                versionNameTextView.setText("Ver - " + Nostragamus.getInstance().getAppVersionName());
                versionNameTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.scrollbar_indicator));
                updateApp.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.navigation_update_app_dark_icon));

                TextView updateTextView = (TextView) view.findViewById(R.id.navigation_update_str_textView);
                updateTextView.setTextColor(ContextCompat.getColor(updateTextView.getContext(), R.color.scrollbar_indicator));
                updateTextView.setText("App is up to date");
            }
        }
    }

    private void initProfileDetails() {
        View rootView = getView();
        UserInfo userInfo = Nostragamus.getInstance().getServerDataManager().getUserInfo();

        if (userInfo != null && rootView != null) {
            String userNickName = userInfo.getUserNickName();
            String userPhoto = userInfo.getPhoto();

            if (!TextUtils.isEmpty(userNickName)) {
                String userNameStr = userNickName.substring(0,1).toUpperCase() + userNickName.substring(1);
                TextView profileTextView = (TextView) rootView.findViewById(R.id.profile_name_textView);
                profileTextView.setText(userNameStr);
            }

            if (!TextUtils.isEmpty(userPhoto)) {
                HmImageView profileImageView = (HmImageView) rootView.findViewById(R.id.profile_imageView);
                profileImageView.setImageUrl(userPhoto);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.navigation_profile_layout:
                NostragamusAnalytics.getInstance().trackClickEvent(Constants.AnalyticsCategory.NAVIGATION_SCREEN, Constants.AnalyticsClickLabels.PROFILE);
                onProfileClicked();
                break;

            case R.id.navigation_wallet_layout:
                NostragamusAnalytics.getInstance().trackClickEvent(Constants.AnalyticsCategory.NAVIGATION_SCREEN, Constants.AnalyticsClickLabels.WALLET);
                onWalletClicked();
                break;

            case R.id.navigation_powerup_bank_layout:
                NostragamusAnalytics.getInstance().trackClickEvent(Constants.AnalyticsCategory.NAVIGATION_SCREEN, Constants.AnalyticsClickLabels.POWER_UP_BANK);
                onPowerUpsClicked();
                break;

            case R.id.navigation_store_layout:
                NostragamusAnalytics.getInstance().trackClickEvent(Constants.AnalyticsCategory.NAVIGATION_SCREEN, Constants.AnalyticsClickLabels.STORE);
                onStoreClicked();
                break;

            case R.id.navigation_whats_new_layout:
                NostragamusAnalytics.getInstance().trackClickEvent(Constants.AnalyticsCategory.NAVIGATION_SCREEN, Constants.AnalyticsClickLabels.WHATS_NEW);
                onWhatsNewClicked();
                break;

            case R.id.navigation_refer_layout:
                NostragamusAnalytics.getInstance().trackClickEvent(Constants.AnalyticsCategory.NAVIGATION_SCREEN, Constants.AnalyticsClickLabels.REFER_FRIEND);
                onReferFriendClicked();
                break;

            /*case R.id.navigation_submit_question_layout:
                NostragamusAnalytics.getInstance().trackClickEvent(Constants.AnalyticsCategory.NAVIGATION_SCREEN, Constants.AnalyticsClickLabels.SUBMIT_QUESTION);
                onSubmitQuestionClicked();
                break;*/

            case R.id.navigation_help_layout:
                NostragamusAnalytics.getInstance().trackClickEvent(Constants.AnalyticsCategory.NAVIGATION_SCREEN, Constants.AnalyticsClickLabels.HELP);
                onHelpClicked();
                break;

            case R.id.navigation_settings_layout:
                NostragamusAnalytics.getInstance().trackClickEvent(Constants.AnalyticsCategory.NAVIGATION_SCREEN, Constants.AnalyticsClickLabels.SETTINGS);
                onSettingsClicked();
                break;

            case R.id.navigation_app_update_layout:
                NostragamusAnalytics.getInstance().trackClickEvent(Constants.AnalyticsCategory.NAVIGATION_SCREEN, Constants.AnalyticsClickLabels.APP_UPDATE);
                onUpdateAppClicked();
                break;

        }
    }

    private void onStoreClicked() {
        if (getActivity() != null) {
            Intent intent = new Intent(getActivity(), StoreActivity.class);
            startActivity(intent);
        }
    }

    private void onReferFriendClicked() {
        if (getActivity() != null) {
            Intent intent = new Intent(getActivity(), ReferFriendActivity.class);
            startActivity(intent);
        }
    }

    /**
     * The method has been only called when update is available, else listener is not registered for callback.
     */
    private void onUpdateAppClicked() {
        if (getActivity() != null) {
            Intent intent = new Intent(getActivity(), AppUpdateActivity.class);
            startActivity(intent);
        }
    }

    private void onSettingsClicked() {
        if (getActivity() != null) {
            Intent intent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(intent);
        }
    }

    private void onHelpClicked() {
        if (getActivity() != null) {
            Intent intent = new Intent(getActivity(), HelpActivity.class);
            startActivity(intent);
        }
    }

    private void onSubmitQuestionClicked() {
        if (getActivity() != null) {
            Intent intent = new Intent(getActivity(), TourListActivity.class);
            startActivity(intent);
        }
    }

    private void onWhatsNewClicked() {
        if (getActivity() != null) {
            Intent intent = new Intent(getActivity(), AppUpdateActivity.class);
            intent.putExtra(Constants.BundleKeys.SCREEN, Constants.ScreenNames.WHATS_NEW);
            startActivity(intent);
        }
    }

    private void onPowerUpsClicked() {
        if (getActivity() != null) {
            Intent intent = new Intent(getActivity(), PowerUpBankActivity.class);
            startActivity(intent);
        }
    }

    private void onWalletClicked() {
        if (getActivity() != null) {
            Intent intent = new Intent(getActivity(), WalletHomeActivity.class);
            startActivity(intent);
        }
    }

    private void onProfileClicked() {
        if (getActivity() != null) {
            Bundle args = getArguments();

            Intent intent = new Intent(getActivity(), UserProfileActivity.class);
            if (args != null) {
                intent.putExtras(getArguments());
            }
            startActivity(intent);
        }
    }

    /**
     * @return true if newer app version available
     */
    private boolean isNewVersionAvailable() {
        boolean isNewVersion = false;

        int appRequestVer = -1;
        AppSettingsResponse appSettingsResponse = Nostragamus.getInstance().getServerDataManager().getAppSettingsResponse();
        if (appSettingsResponse != null && appSettingsResponse.getAppUpdateInfo() != null) {
            AppUpdateInfo appUpdateInfo = appSettingsResponse.getAppUpdateInfo();
            appRequestVer = appUpdateInfo.getUpdateRequestVersion();
        }
        int currentAppVersion = Nostragamus.getInstance().getAppVersionCode();
        
        if (appRequestVer > 0 && currentAppVersion < appRequestVer) {
            isNewVersion = true;
        }
        return isNewVersion;
    }

    private void fetchUserWalletFromServer() {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            WalletApiModelImpl.newInstance(new WalletApiModelImpl.WalletApiListener() {
                @Override
                public void noInternet() {
                    Log.d(TAG, Constants.Alerts.NO_NETWORK_CONNECTION);
                }

                @Override
                public void onApiFailed() {
                    Log.d(TAG, Constants.Alerts.API_FAIL);
                    fetchUserInfo();
                }

                @Override
                public void onSuccessResponse(UserWalletResponse response) {
                    showOrUpdateWalletAmount();

                }
            }).performApiCall();
        }
    }

    private void fetchUserInfo() {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            UserInfoModelImpl.newInstance(new UserInfoModelImpl.OnGetUserInfoModelListener() {
                @Override
                public void onSuccessGetUpdatedUserInfo(UserInfo updatedUserInfo) {}

                @Override
                public void onFailedGetUpdateUserInfo(String message) {}

                @Override
                public void onNoInternet() {}
            }).getUserInfo();
        } else {
            Log.i(TAG, "No internet");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchUserWalletFromServer();
    }

    private void showOrUpdateWalletAmount() {
        double amount = WalletHelper.getTotalBalance();
        if (getView() != null) {
            TextView amountTextView = (TextView) getView().findViewById(R.id.navigation_wallet_amount_textView);
            amountTextView.setText(WalletHelper.getFormattedStringOfAmount(amount));
        }
    }
}
