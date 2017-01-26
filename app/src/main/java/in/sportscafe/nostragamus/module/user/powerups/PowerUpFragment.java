package in.sportscafe.nostragamus.module.user.powerups;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusFragment;
import in.sportscafe.nostragamus.utils.ViewUtils;

/**
 * Created by deepanshi on 12/8/16.
 */

public class PowerUpFragment extends NostragamusFragment implements PowerUpView{

    private RecyclerView mRvPowerUp;

    private PowerUpPresenter mPowerUpPresenter;

    public static PowerUpFragment newInstance() {

        PowerUpFragment fragment = new PowerUpFragment();
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_powerup, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.mRvPowerUp = (RecyclerView) findViewById(R.id.powerup_rcv);
        this.mRvPowerUp.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        this.mRvPowerUp.setHasFixedSize(true);
        this.mPowerUpPresenter = PowerUpPresenterImpl.newInstance(this);
        this.mPowerUpPresenter.onCreatePowerUpAdapter();

    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        this.mRvPowerUp.setAdapter(adapter);
    }

}