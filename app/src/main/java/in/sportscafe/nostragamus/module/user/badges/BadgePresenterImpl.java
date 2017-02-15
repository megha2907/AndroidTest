package in.sportscafe.nostragamus.module.user.badges;

import android.os.Bundle;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.Alerts;

/**
 * Created by Deepanshi on 27/5/16.
 */
public class BadgePresenterImpl implements BadgePresenter, BadgeModelImpl.BadgeModelListener {

    private BadgeView mBadgeView;

    private BadgeModel mBadgeModel;

    public BadgePresenterImpl(BadgeView BadgeView) {
        this.mBadgeView = BadgeView;
        this.mBadgeModel = BadgeModelImpl.newInstance(this);
    }

    public static BadgePresenter newInstance(BadgeView BadgeView) {
        return new BadgePresenterImpl(BadgeView);
    }

    @Override
    public void onCreateBadges(Bundle bundle) {
        mBadgeModel.createAdapter(mBadgeView.getContext(), bundle);
    }

    @Override
    public void onAdapterCreated(BadgeAdapter badgeAdapter) {
        mBadgeView.setAdapter(badgeAdapter);
    }

    @Override
    public void onBadgesEmpty() {
        mBadgeView.showInAppMessage(Alerts.NO_BADGES);
    }
}