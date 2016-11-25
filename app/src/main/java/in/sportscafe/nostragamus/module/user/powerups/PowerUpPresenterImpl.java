package in.sportscafe.nostragamus.module.user.powerups;

import in.sportscafe.nostragamus.Constants;

/**
 * Created by Deepanshi on 27/5/16.
 */
public class PowerUpPresenterImpl implements PowerUpPresenter, PowerUpModelImpl.PowerUpModelListener {

    private PowerUpView mPowerUpView;

    private PowerUpModel mPowerUpModel;

    private boolean mFromProfile = false;

    public PowerUpPresenterImpl(PowerUpView powerUpView) {
        this.mPowerUpView = powerUpView;
        this.mPowerUpModel = PowerUpModelImpl.newInstance(this);
    }

    public static PowerUpPresenter newInstance(PowerUpView powerUpView) {
        return new PowerUpPresenterImpl(powerUpView);
    }

    @Override
    public void onCreatePowerUpAdapter() {
        mPowerUpView.setAdapter(mPowerUpModel
                .getPowerUpAdapter(mPowerUpView.getContext()));
    }

    @Override
    public void onClickNext() {
        mPowerUpView.showProgressbar();
    }

    @Override
    public void onNoInternet() {
        mPowerUpView.dismissProgressbar();
        mPowerUpView.showMessage(Constants.Alerts.NO_NETWORK_CONNECTION);
    }

    @Override
    public void onFailed(String message) {
        mPowerUpView.dismissProgressbar();
        mPowerUpView.showMessage(message);
    }
}