package in.sportscafe.scgame.module.user.badges;

import com.jeeva.android.Log;

import in.sportscafe.scgame.Constants;

/**
 * Created by Deepanshi on 27/5/16.
 */
public class BadgePresenterImpl implements BadgePresenter, BadgeModelImpl.BadgeModelListener {

    private BadgeView mBadgeView;

    private BadgeModel mBadgeModel;

    private boolean mFromProfile = false;

    public BadgePresenterImpl(BadgeView BadgeView) {
        this.mBadgeView = BadgeView;
        this.mBadgeModel = BadgeModelImpl.newInstance(this);
    }

    public static BadgePresenter newInstance(BadgeView BadgeView) {
        return new BadgePresenterImpl(BadgeView);
    }

    @Override
    public void onCreateBadgeAdapter() {
        mBadgeView.setAdapter(mBadgeModel
                .getBadgeAdapter(mBadgeView.getContext()));
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