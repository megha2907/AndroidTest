package in.sportscafe.nostragamus.module.user.powerups;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import org.parceler.Parcels;

import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.user.badges.Badge;
import in.sportscafe.nostragamus.module.user.badges.BadgeAdapter;

public class PowerUpModelImpl implements PowerUpModel {

    private PowerUpModelListener mPowerUpModelListener;

    protected PowerUpModelImpl(PowerUpModelListener modelListener) {
        this.mPowerUpModelListener = modelListener;
    }

    public static PowerUpModel newInstance(PowerUpModelListener modelListener) {
        return new PowerUpModelImpl(modelListener);
    }

    @Override
    public void createPowerUpAdapter(Context context, Bundle bundle) {
        List<PowerUp> powerUpList = Parcels.unwrap(bundle.getParcelable(BundleKeys.POWERUPS));
        if(null == powerUpList || powerUpList.isEmpty()) {
            mPowerUpModelListener.onPowerUpsEmpty();
            return;
        }

        mPowerUpModelListener.onAdapterCreated(new PowerUpAdapter(context, powerUpList));
    }


    public interface PowerUpModelListener {

        void onPowerUpsEmpty();

        void onAdapterCreated(PowerUpAdapter powerUpAdapter);
    }
}