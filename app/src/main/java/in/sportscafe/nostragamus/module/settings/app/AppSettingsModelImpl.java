package in.sportscafe.nostragamus.module.settings.app;

import com.jeeva.android.Log;

import in.sportscafe.nostragamus.BuildConfig;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.settings.app.dto.AppSettingsResponse;
import in.sportscafe.nostragamus.module.settings.app.dto.AppUpdateInfo;
import in.sportscafe.nostragamus.module.settings.app.dto.SettingsDetails;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Jeeva on 23/8/16.
 */
public class AppSettingsModelImpl implements AppSettingsModel {

    private AppSettingsModelImpl() {
    }

    public static AppSettingsModel newInstance() {
        return new AppSettingsModelImpl();
    }

    @Override
    public void getAppSettings() {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            callAppSettingsApi();
        }
    }

    private void callAppSettingsApi() {

        String flavor;
        if (BuildConfig.IS_PAID_VERSION) {
            flavor = "FULL";
        } else {
            flavor = "PLAYSTORE";
        }

        MyWebService.getInstance().getAppSettingsRequest("nostragamus_settings", flavor).enqueue(
                new NostragamusCallBack<AppSettingsResponse>() {
                    @Override
                    public void onResponse(Call<AppSettingsResponse> call, Response<AppSettingsResponse> response) {
                        super.onResponse(call, response);
                        handleAppSettingsResponse(response);
                    }
                }
        );
    }

    private void handleAppSettingsResponse(Response<AppSettingsResponse> response) {
        if (null != response && response.isSuccessful()) {
            AppSettingsResponse appSettings = response.body();
            if (null != appSettings) {
                handleVersion(appSettings.getSettingsDetails(), appSettings.getAppUpdateInfo());
            }
        }
    }

    private void handleVersion(SettingsDetails settingsDetails, AppUpdateInfo appUpdateInfo) {
        if (null != settingsDetails) {
            Log.d("Settings--", settingsDetails.toString());

            NostragamusDataHandler dataHandler = NostragamusDataHandler.getInstance();

            dataHandler.setFeedBack(settingsDetails.getFeedbackText());
            dataHandler.setProFeedBack(settingsDetails.getFeedbackProText());
            dataHandler.setDownloadPaidApp(settingsDetails.getDownloadPaidText());
            dataHandler.setAskFriendText(settingsDetails.getAskFriendText());
            dataHandler.setDisclaimerText(settingsDetails.getDisclaimerText());


            /* New App Update Code */

            int lastShownAppVersion = NostragamusDataHandler.getInstance().getLastShownAppVersionCode();
            int reqAppVersion = appUpdateInfo.getReqVersion();
            dataHandler.setReqUpdateVersion(appUpdateInfo.getReqVersion());

            /* Check if current Ver is not equal to Required App Version */
            if (lastShownAppVersion != reqAppVersion) {
                Log.d("inside", "currentAppVersion!=reqAppVersion");
                dataHandler.setAppUpdateType(appUpdateInfo.getUpdateType());
            }

        }
    }
}