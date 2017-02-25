package in.sportscafe.nostragamus.module.user.powerups;

import android.os.Bundle;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.Alerts;

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
    public void onCreatePowerUps(Bundle bundle) {
        mPowerUpModel.createPowerUpAdapter(mPowerUpView.getContext(), bundle);
    }

    @Override
    public void onPowerUpsEmpty() {
        mPowerUpView.showInAppMessage(Alerts.NO_POWERUPS);
    }

    @Override
    public void onAdapterCreated(PowerUpAdapter powerUpAdapter) {
        mPowerUpView.setAdapter(powerUpAdapter);
    }
}