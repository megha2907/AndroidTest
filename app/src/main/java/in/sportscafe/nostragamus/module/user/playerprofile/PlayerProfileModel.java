package in.sportscafe.nostragamus.module.user.playerprofile;

import android.os.Bundle;

import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;
import in.sportscafe.nostragamus.module.user.playerprofile.dto.PlayerInfo;

/**
 * Created by deepanshi on 12/22/16.
 */

public interface PlayerProfileModel {

    void getProfileDetails(Bundle bundle);

    PlayerInfo getPlayerInfo();
}
