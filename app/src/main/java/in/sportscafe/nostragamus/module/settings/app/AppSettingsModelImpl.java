package in.sportscafe.nostragamus.module.settings.app;

import com.jeeva.android.Log;

import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.settings.app.dto.AppSettingsResponse;
import in.sportscafe.nostragamus.module.settings.app.dto.Version;
import in.sportscafe.nostragamus.module.settings.app.dto.VersionDetails;
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
        if(Nostragamus.getInstance().hasNetworkConnection()) {
            callAppSettingsApi();
        }
    }

    private void callAppSettingsApi() {
        MyWebService.getInstance().getAppSettingsRequest("nostragamus_settings").enqueue(
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
            if(null != appSettings) {
                handleVersion(appSettings.getVersion());
            }
        }
    }

    private void handleVersion(VersionDetails versionDetails) {
        if(null != versionDetails) {
            Log.d("Version", versionDetails.toString());

            NostragamusDataHandler dataHandler = NostragamusDataHandler.getInstance();

            dataHandler.setFeedBack(versionDetails.getFeedbackText());
            dataHandler.setDownloadPaidApp(versionDetails.getDownloadPaidText());
            dataHandler.setAskFriendText(versionDetails.getAskFriendText());
            dataHandler.setDisclaimerText(versionDetails.getDisclaimerText());

            int oldNormalUpdateVersion = dataHandler.getNormalUpdateVersion();

            Version version = versionDetails.getForceUpdateVersion();
            dataHandler.setForceUpdateVersion(version.getVersion());
            dataHandler.setForceUpdateMessage(version.getMessage());

            version = versionDetails.getNormalUpdateVersion();
            dataHandler.setNormalUpdateVersion(version.getVersion());
            dataHandler.setNormalUpdateMessage(version.getMessage());

            if (oldNormalUpdateVersion != version.getVersion()) {
                dataHandler.setNormalUpdateEnabled(true);
            }

            /*  Paid version */
            int oldPaidNormalUpdateVersion = dataHandler.getNormalPaidUpdateVersion();

            Version versionPaid = versionDetails.getPaidForceUpdateVersion();
            if (versionPaid != null) {
                dataHandler.setForcePaidUpdateVersion(versionPaid.getVersion());
                dataHandler.setForcePaidUpdateMessage(versionPaid.getMessage());
                dataHandler.setPaidForceApkLink(versionPaid.getApkLink());
            }

            versionPaid = versionDetails.getPaidNormalUpdateVersion();
            if (versionPaid != null) {
                dataHandler.setNormalPaidUpdateVersion(versionPaid.getVersion());
                dataHandler.setNormalPaidUpdateMessage(versionPaid.getMessage());
                dataHandler.setPaidNormalApkLink(versionPaid.getApkLink());
            }

            if (versionPaid != null && oldPaidNormalUpdateVersion != versionPaid.getVersion()) {
                dataHandler.setNormalPaidUpdateEnabled(true);
            }

        }
    }
}