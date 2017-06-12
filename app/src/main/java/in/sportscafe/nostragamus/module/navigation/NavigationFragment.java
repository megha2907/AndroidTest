package in.sportscafe.nostragamus.module.navigation;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeeva.android.BaseFragment;
import com.jeeva.android.widgets.HmImageView;

import in.sportscafe.nostragamus.BuildConfig;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.appupdate.AppUpdateActivity;
import in.sportscafe.nostragamus.module.navigation.help.HelpActivity;
import in.sportscafe.nostragamus.module.navigation.settings.SettingsActivity;
import in.sportscafe.nostragamus.module.navigation.submitquestion.tourlist.TourListActivity;
import in.sportscafe.nostragamus.module.navigation.wallet.WalletHomeActivity;
import in.sportscafe.nostragamus.module.permission.PermissionsActivity;
import in.sportscafe.nostragamus.module.permission.PermissionsChecker;
import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;
import in.sportscafe.nostragamus.module.user.myprofile.UserProfileActivity;
import in.sportscafe.nostragamus.module.user.myprofile.edit.EditProfileActivity;
import in.sportscafe.nostragamus.service.NostraFileDownloadService;
import in.sportscafe.nostragamus.utils.StorageUtility;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

public class NavigationFragment extends BaseFragment implements View.OnClickListener{


    public NavigationFragment() {}

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
        view.findViewById(R.id.navigation_whats_new_layout).setOnClickListener(this);
        view.findViewById(R.id.navigation_submit_question_layout).setOnClickListener(this);
        view.findViewById(R.id.navigation_help_layout).setOnClickListener(this);
        view.findViewById(R.id.navigation_settings_layout).setOnClickListener(this);
        view.findViewById(R.id.navigation_app_update_layout).setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();
    }

    private void initialize() {
        showOrHideContentBasedOnAppType();
        showVersionOrAppUpdateMsg();
        initProfileDetails();
    }

    private void showOrHideContentBasedOnAppType() {
        /* Hide wallet section for free (ps - play store) app */
        /*if (!BuildConfig.IS_PAID_VERSION) {
            if (getView() != null) {
                getView().findViewById(R.id.navigation_wallet_layout).setVisibility(View.GONE);
            }
        }*/
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

            } else {
                view.findViewById(R.id.navigation_app_update_layout).setOnClickListener(null);
                view.findViewById(R.id.navigation_app_update_available_imageView).setVisibility(View.GONE);

                TextView versionNameTextView = (TextView) view.findViewById(R.id.navigation_version_textView);
                versionNameTextView.setText(Nostragamus.getInstance().getAppVersionName());

                TextView updateTextView = (TextView) view.findViewById(R.id.navigation_update_str_textView);
                updateTextView.setTextColor(ContextCompat.getColor(updateTextView.getContext(), R.color.white_60));
            }
        }
    }

    private void initProfileDetails() {
        View rootView = getView();
        UserInfo userInfo = NostragamusDataHandler.getInstance().getUserInfo();

        if (userInfo != null && rootView != null) {
            String userName = userInfo.getUserName();
            String userPhoto = userInfo.getPhoto();

            if (!TextUtils.isEmpty(userName)) {
                TextView profileTextView = (TextView) rootView.findViewById(R.id.profile_name_textView);
                profileTextView.setText(userName);
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
                onProfileClicked();
                break;

            case R.id.navigation_wallet_layout:
                onWalletClicked();
                break;

            case R.id.navigation_powerup_bank_layout:
                onPowerUpsClicked();
                break;

            case R.id.navigation_whats_new_layout:
                onWhatsNewClicked();
                NostragamusAnalytics.getInstance().trackWhatsNew(Constants.AnalyticsActions.OPENED);
                break;

            case R.id.navigation_submit_question_layout:
                onSubmitQuestionClicked();
                break;

            case R.id.navigation_help_layout:
                onHelpClicked();
                break;

            case R.id.navigation_settings_layout:
                onSettingsClicked();
                break;

            case R.id.navigation_app_update_layout:
                onUpdateAppClicked();
                NostragamusAnalytics.getInstance().trackUpdateApp(Constants.AnalyticsActions.OPENED);
                break;

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
            intent.putExtra(Constants.BundleKeys.SCREEN,Constants.ScreenNames.WHATS_NEW);
            startActivity(intent);
        }
    }

    private void onPowerUpsClicked() {
        downloadAndInstallApp();
    }

    private void downloadAndInstallApp() {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            MyWebService.getInstance().getLatestApk().enqueue(new NostragamusCallBack<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    super.onResponse(call, response);

                    if (response != null && response.isSuccessful()) {
                        String url = response.body();
                        if (!TextUtils.isEmpty(url)) {

                            Intent intent = new Intent(getContext().getApplicationContext(), NostraFileDownloadService.class);
                            intent.putExtra(NostraFileDownloadService.FILE_DOWNLOAD_URL, url);
                            intent.putExtra(NostraFileDownloadService.FILE_NAME_WITH_EXTENSION, StorageUtility.getFileNameWithSuffix(url));

                            getContext().startService(intent);

                        } else {
                            showMessage(Constants.Alerts.API_FAIL);
                        }
                    } else {
                        showMessage(Constants.Alerts.API_FAIL);
                    }
                }
            });
        } else {
            showMessage("No internet");
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
            Intent intent = new Intent(getActivity(), UserProfileActivity.class);
            intent.putExtras(getArguments());
            startActivity(intent);
        }
    }

    /**
     *
     * @return true if newer app version available
     */
    private boolean isNewVersionAvailable() {
        boolean isNewVersion = false;

        NostragamusDataHandler dataHandler = NostragamusDataHandler.getInstance();
        int currentAppVersion = Nostragamus.getInstance().getAppVersionCode();

        if ((currentAppVersion < dataHandler.getReqUpdateVersion())) {
            isNewVersion = true;
        }
        return isNewVersion;
    }
}
