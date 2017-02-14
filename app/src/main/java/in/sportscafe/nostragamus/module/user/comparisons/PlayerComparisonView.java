package in.sportscafe.nostragamus.module.user.comparisons;

import android.os.Bundle;

import com.jeeva.android.InAppView;

import in.sportscafe.nostragamus.module.user.playerprofile.dto.PlayerInfo;

/**
 * Created by deepanshi on 2/11/17.
 */

public interface PlayerComparisonView extends InAppView {

    void setName(String userName, String playerName);

    void setProfileImage(String userImageUrl, String playerImageUrl);

    void initMyPosition(Bundle bundle);
}
