package in.sportscafe.scgame.module.settings.app;

import in.sportscafe.scgame.ScGame;
import in.sportscafe.scgame.ScGameDataHandler;
import in.sportscafe.scgame.module.settings.app.dto.AppSettingsResponse;
import in.sportscafe.scgame.module.settings.app.dto.Version;
import in.sportscafe.scgame.module.settings.app.dto.VersionDetails;
import in.sportscafe.scgame.webservice.MyWebService;
import in.sportscafe.scgame.webservice.ScGameCallBack;
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
        if(ScGame.getInstance().hasNetworkConnection()) {
            callAppSettingsApi();
        }
    }

    private void callAppSettingsApi() {
        MyWebService.getInstance().getAppSettingsRequest("app_settings").enqueue(
                new ScGameCallBack<AppSettingsResponse>() {
                    @Override
                    public void onResponse(Call<AppSettingsResponse> call, Response<AppSettingsResponse> response) {
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
            ScGameDataHandler dataHandler = ScGameDataHandler.getInstance();

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
        }
    }
}