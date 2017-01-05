package in.sportscafe.nostragamus.module.user.playerbadges;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.user.badges.BadgeAdapter;
import in.sportscafe.nostragamus.module.user.badges.BadgeModel;
import in.sportscafe.nostragamus.module.user.badges.BadgeModelImpl;
import in.sportscafe.nostragamus.module.user.playerprofile.dto.PlayerInfo;

/**
 * Created by deepanshi on 1/5/17.
 */

public class PlayerBadgeModelImpl  implements PlayerBadgeModel {

    private NostragamusDataHandler mNostragamusDataHandler;

    private BadgeAdapter mBadgeAdapter;

    private PlayerBadgeModelImpl.BadgeModelListener mBadgeModelListener;

    protected PlayerBadgeModelImpl(PlayerBadgeModelImpl.BadgeModelListener modelListener) {
        this.mBadgeModelListener = modelListener;
        this.mNostragamusDataHandler = NostragamusDataHandler.getInstance();
    }

    public static PlayerBadgeModel newInstance(PlayerBadgeModelImpl.BadgeModelListener modelListener) {
        return new PlayerBadgeModelImpl(modelListener);
    }

    @Override
    public RecyclerView.Adapter getPlayerBadgeAdapter(Context context, PlayerInfo playerInfo) {

        if(playerInfo.getBadges().isEmpty()){

            mBadgeModelListener.onBadgesEmpty();
        }


        mBadgeAdapter = new BadgeAdapter(context,
                playerInfo.getBadges());
        return mBadgeAdapter;
    }


    public interface BadgeModelListener {

        void onNoInternet();

        void onFailed(String message);

        void onBadgesEmpty();
    }
}