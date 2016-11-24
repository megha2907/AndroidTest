package in.sportscafe.scgame.module.user.powerups;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.jeeva.android.Log;

import in.sportscafe.scgame.ScGameDataHandler;

public class PowerUpModelImpl implements PowerUpModel {

    private ScGameDataHandler mScGameDataHandler;

    private PowerUpAdapter mPowerUpAdapter;

    private PowerUpModelListener mPowerUpModelListener;

    protected PowerUpModelImpl(PowerUpModelListener modelListener) {
        this.mPowerUpModelListener = modelListener;
        this.mScGameDataHandler = ScGameDataHandler.getInstance();
    }

    public static PowerUpModel newInstance(PowerUpModelListener modelListener) {
        return new PowerUpModelImpl(modelListener);
    }

    @Override
    public RecyclerView.Adapter getPowerUpAdapter(Context context) {

        mPowerUpAdapter = new PowerUpAdapter(context,
                mScGameDataHandler.getPowerUpList());
        return mPowerUpAdapter;
    }


    public interface PowerUpModelListener {

        void onNoInternet();

        void onFailed(String message);
    }
}