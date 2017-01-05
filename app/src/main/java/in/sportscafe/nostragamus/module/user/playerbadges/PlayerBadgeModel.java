package in.sportscafe.nostragamus.module.user.playerbadges;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import in.sportscafe.nostragamus.module.user.playerprofile.dto.PlayerInfo;

/**
 * Created by deepanshi on 1/5/17.
 */

public interface PlayerBadgeModel {

    RecyclerView.Adapter getPlayerBadgeAdapter(Context context, PlayerInfo playerInfo);
}
