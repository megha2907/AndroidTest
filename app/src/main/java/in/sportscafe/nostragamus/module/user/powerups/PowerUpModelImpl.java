package in.sportscafe.nostragamus.module.user.powerups;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import in.sportscafe.nostragamus.NostragamusDataHandler;

public class PowerUpModelImpl implements PowerUpModel {

    private NostragamusDataHandler mNostragamusDataHandler;

    private PowerUpAdapter mPowerUpAdapter;

    private PowerUpModelListener mPowerUpModelListener;

    protected PowerUpModelImpl(PowerUpModelListener modelListener) {
        this.mPowerUpModelListener = modelListener;
        this.mNostragamusDataHandler = NostragamusDataHandler.getInstance();
    }

    public static PowerUpModel newInstance(PowerUpModelListener modelListener) {
        return new PowerUpModelImpl(modelListener);
    }

    @Override
    public RecyclerView.Adapter getPowerUpAdapter(Context context) {

        mPowerUpAdapter = new PowerUpAdapter(context,
                mNostragamusDataHandler.getPowerUpList());
        return mPowerUpAdapter;
    }


    public interface PowerUpModelListener {

        void onNoInternet();

        void onFailed(String message);
    }
}