package in.sportscafe.nostragamus.module.user.playerbadges;

import android.os.Bundle;

import org.parceler.Parcels;

import in.sportscafe.nostragamus.Constants.Alerts;
import in.sportscafe.nostragamus.Constants.BundleKeys;
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
        PlayerInfo playerInfo = Parcels.unwrap(bundle.getParcelable(BundleKeys.PLAYERINFO));
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
        mBadgeView.showMessage(Alerts.NO_NETWORK_CONNECTION);
    }

    @Override
    public void onFailed(String message) {
        mBadgeView.dismissProgressbar();
        mBadgeView.showMessage(message);
    }

    @Override
    public void onBadgesEmpty() {
        mBadgeView.dismissProgressbar();
        mBadgeView.showInAppMessage(Alerts.NO_BADGES);
    }
}