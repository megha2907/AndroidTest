package in.sportscafe.scgame.module.user.powerups;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeeva.android.widgets.customfont.CustomButton;

import java.util.List;

import in.sportscafe.scgame.R;
import in.sportscafe.scgame.module.common.Adapter;

/**
 * Created by deepanshi on 23/8/16.
 */
public class PowerUpAdapter extends Adapter<PowerUp, PowerUpAdapter.ViewHolder> {

    public PowerUpAdapter(Context context, List<PowerUp> powerUpList) {
        super(context);
        addAll(powerUpList);
    }

    @Override
    public PowerUp getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(getLayoutInflater().inflate(R.layout.inflater_powerup_row, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        PowerUp powerUp = getItem(position);
        holder.mBtnPowerUpCount.setText(String.valueOf(powerUp.getCount()));
        holder.mTvPowerUpName.setText(powerUp.getName()+" Power Up");
        holder.mBtnPowerUpId.setText(powerUp.getName());
        holder.mTvPowerUpDesc.setText(powerUp.getName()+" Power Up");
    }

    class ViewHolder extends RecyclerView.ViewHolder  {

        int id;

        View mMainView;

        TextView mTvPowerUpName;

        TextView mTvPowerUpDesc;

        CustomButton mBtnPowerUpId;

        CustomButton mBtnPowerUpCount;

        public ViewHolder(View V) {
            super(V);
            mMainView = V;
            mTvPowerUpName = (TextView) V.findViewById(R.id.powerup_tv_1);
            mTvPowerUpDesc = (TextView) V.findViewById(R.id.powerup_tv_2);
            mBtnPowerUpId = (CustomButton) V.findViewById(R.id.powerup_btn);
            mBtnPowerUpCount = (CustomButton) V.findViewById(R.id.powerup_btn_powerup_count);

        }

    }

}