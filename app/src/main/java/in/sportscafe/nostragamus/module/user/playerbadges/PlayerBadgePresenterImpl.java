package in.sportscafe.nostragamus.module.user.playerbadges;

import android.os.Bundle;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.module.user.badges.BadgeModel;
import in.sportscafe.nostragamus.module.user.badges.BadgeModelImpl;
import in.sportscafe.nostragamus.module.user.badges.BadgePresenter;
import in.sportscafe.nostragamus.module.user.badges.BadgePresenterImpl;
import in.sportscafe.nostragamus.module.user.badges.BadgeView;
import in.sportscafe.nostragamus.module.user.playerprofile.dto.PlayerInfo;

/**
 * Created by deepanshi on 1/5/17.
 */

public class PlayerBadgePresenterImpl implements PlayerBadgePresenter, PlayerBadgeModelImpl.BadgeModelListener {

    private PlayerBadgeView mBadgeView;

    private PlayerBadgeModel mBadgeModel;

    private boolean mFromProfile = false;

    public PlayerBadgePresenterImpl(PlayerBadgeView playerBadgeView) {
        this.mBadgeView = playerBadgeView;
        this.mBadgeModel = PlayerBadgeModelImpl.newInstance(this);
    }

    public static PlayerBadgePresenter newInstance(PlayerBadgeView playerBadgeView) {
        return new PlayerBadgePresenterImpl(playerBadgeView);
    }

    @Override
    public void onCreatePlayerBadgeAdapter(Bundle bundle) {
        PlayerInfo playerInfo = (PlayerInfo) bundle.getSerializable(Constants.BundleKeys.PLAYERINFO);
        mBadgeView.setAdapter(mBadgeModel
                .getPlayerBadgeAdapter(mBadgeView.getContext(),playerInfo));
    }

    @Override
    public void onClickNext() {
        mBadgeView.showProgressbar();
    }

    @Override
    public void onNoInternet() {
        mBadgeView.dismissProgressbar();
        mBadgeView.showMessage(Constants.Alerts.NO_NETWORK_CONNECTION);
    }

    @Override
    public void onFailed(String message) {
        mBadgeView.dismissProgressbar();
        mBadgeView.showMessage(message);
    }

    @Override
    public void onBadgesEmpty() {
        mBadgeView.showBadgesEmpty();
    }
}