package in.sportscafe.nostragamus;

import in.sportscafe.nostragamus.module.navigation.wallet.dto.UserWalletResponse;
import in.sportscafe.nostragamus.module.settings.app.dto.AppSettingsResponse;
import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;

/**
 * Created by sandip on 24/04/17.
 */

public class ServerDataManager {

    private UserWalletResponse userWalletResponse;
    private UserInfo userInfo;
    private AppSettingsResponse appSettingsResponse;

    public UserWalletResponse getUserWalletResponse() {
        return userWalletResponse;
    }

    public void setUserWalletResponse(UserWalletResponse userWalletResponse) {
        this.userWalletResponse = userWalletResponse;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public AppSettingsResponse getAppSettingsResponse() {
        return appSettingsResponse;
    }

    public void setAppSettingsResponse(AppSettingsResponse appSettingsResponse) {
        this.appSettingsResponse = appSettingsResponse;
    }
}
