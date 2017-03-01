package in.sportscafe.nostragamus.module.bank;

import android.os.Bundle;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashMap;

import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.Constants.Powerups;
import in.sportscafe.nostragamus.module.user.powerups.PowerUp;

/**
 * Created by Jeeva on 01/03/17.
 */

public class BankModelImpl implements BankModel {

    private HashMap<String, PowerUp> mPowerUpMaps = new HashMap<>();

    private OnBankModelListener mBankModelListener;

    private BankModelImpl(OnBankModelListener bankModelListener) {
        this.mBankModelListener = bankModelListener;
    }

    public static BankModel newInstance(OnBankModelListener bankModelListener) {
        return new BankModelImpl(bankModelListener);
    }

    @Override
    public void init(Bundle bundle) {
        if (bundle.containsKey(BundleKeys.POWERUPS)) {
            mPowerUpMaps = Parcels.unwrap(bundle.getParcelable(BundleKeys.POWERUPS));
        }

        int count;

        PowerUp powerUp = mPowerUpMaps.get(Powerups.XX);
        if(null == powerUp) {
            count = 0;
        } else {
            count = powerUp.getCount();
        }
        mBankModelListener.onGet2xPowerUp(count, count < 3);

        powerUp = mPowerUpMaps.get(Powerups.NO_NEGATIVE);
        if(null == powerUp) {
            count = 0;
        } else {
            count = powerUp.getCount();
        }
        mBankModelListener.onGetNonegsPowerUp(count, count < 3);

        powerUp = mPowerUpMaps.get(Powerups.AUDIENCE_POLL);
        if(null == powerUp) {
            count = 0;
        } else {
            count = powerUp.getCount();
        }
        mBankModelListener.onGetPollPowerUp(count, count < 3);
    }

    public interface OnBankModelListener {

        void onGet2xPowerUp(int count, boolean runningLow);

        void onGetNonegsPowerUp(int count, boolean runningLow);

        void onGetPollPowerUp(int count, boolean runningLow);
    }
}